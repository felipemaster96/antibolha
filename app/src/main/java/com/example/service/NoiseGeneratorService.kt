package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.data.AppDatabase
import com.example.engine.NoiseEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class NoiseGeneratorService : Service() {

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)
    private var loopJob: Job? = null

    private lateinit var noiseEngine: NoiseEngine
    private var intervalMinutes: Int = 10

    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getInstance(applicationContext)
        noiseEngine = NoiseEngine(db.bubbleDao())
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action ?: ACTION_START

        when (action) {
            ACTION_STOP -> {
                stopForegroundService()
                return START_NOT_STICKY
            }
            ACTION_UPDATE_INTERVAL -> {
                val newInterval = intent?.getIntExtra(EXTRA_INTERVAL_MINUTES, intervalMinutes) ?: intervalMinutes
                if (newInterval > 0) {
                    intervalMinutes = newInterval
                    _intervalMinutesFlow.value = newInterval
                    restartLoop()
                }
            }
            ACTION_START -> {
                val reqInterval = intent?.getIntExtra(EXTRA_INTERVAL_MINUTES, 10) ?: 10
                intervalMinutes = reqInterval
                _intervalMinutesFlow.value = reqInterval
                startInForeground()
                startLoop()
            }
        }

        return START_STICKY
    }

    private fun startInForeground() {
        val notification = buildNotification("Iniciando gerador de ruído...", "Intervalo: ${intervalMinutes} min")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
        _isServiceRunning.value = true
    }

    private fun startLoop() {
        loopJob?.cancel()
        loopJob = serviceScope.launch {
            // First execution right away
            runCycleAndUpdateNotification()

            while (isActive) {
                val delayMs = intervalMinutes * 60 * 1000L
                delay(delayMs)
                runCycleAndUpdateNotification()
            }
        }
    }

    private fun restartLoop() {
        startLoop()
        updateNotification("Intervalo atualizado: ${intervalMinutes} min", "Proteção de ruído ativa")
    }

    private suspend fun runCycleAndUpdateNotification() {
        val result = noiseEngine.executeNoiseCycle()
        if (result.success) {
            _lastNoiseResult.value = "${result.keyword} • ${result.categoryName}"
            updateNotification(
                "Ruído injetado: \"${result.keyword}\"",
                "${result.categoryName} • ${result.target} (HTTP ${result.httpCode})"
            )
        } else {
            updateNotification(
                "Tentativa de ruído: ${result.keyword}",
                result.details
            )
        }
    }

    private fun updateNotification(title: String, text: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, buildNotification(title, text))
    }

    private fun buildNotification(title: String, text: String): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val stopIntent = Intent(this, NoiseGeneratorService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            1,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .addAction(0, "Pausar Robô", stopPendingIntent)
            .build()
    }

    private fun stopForegroundService() {
        loopJob?.cancel()
        _isServiceRunning.value = false
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        serviceJob.cancel()
        _isServiceRunning.value = false
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "AntiBolha Background Noise",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notificações do serviço de injeção de ruído para quebra de bolhas algorítmicas"
                setShowBadge(false)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "antibolha_noise_channel"
        const val NOTIFICATION_ID = 4041

        const val ACTION_START = "com.example.service.START"
        const val ACTION_STOP = "com.example.service.STOP"
        const val ACTION_UPDATE_INTERVAL = "com.example.service.UPDATE_INTERVAL"
        const val EXTRA_INTERVAL_MINUTES = "extra_interval_minutes"

        private val _isServiceRunning = MutableStateFlow(false)
        val isServiceRunning: StateFlow<Boolean> = _isServiceRunning.asStateFlow()

        private val _intervalMinutesFlow = MutableStateFlow(10)
        val intervalMinutesFlow: StateFlow<Int> = _intervalMinutesFlow.asStateFlow()

        private val _lastNoiseResult = MutableStateFlow<String?>(null)
        val lastNoiseResult: StateFlow<String?> = _lastNoiseResult.asStateFlow()

        fun startService(context: Context, intervalMinutes: Int = 10) {
            val intent = Intent(context, NoiseGeneratorService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_INTERVAL_MINUTES, intervalMinutes)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopService(context: Context) {
            val intent = Intent(context, NoiseGeneratorService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }

        fun updateInterval(context: Context, newIntervalMinutes: Int) {
            val intent = Intent(context, NoiseGeneratorService::class.java).apply {
                action = ACTION_UPDATE_INTERVAL
                putExtra(EXTRA_INTERVAL_MINUTES, newIntervalMinutes)
            }
            context.startService(intent)
        }
    }
}
