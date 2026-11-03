package com.example.data

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Guarda a meta diária do botão "Me tire da bolha" e quantas vezes
 * o usuário já usou hoje. Usa SharedPreferences (simples, sem mexer no Room).
 */
class BurstPrefs(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("burst_prefs", Context.MODE_PRIVATE)

    private fun today(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    fun countToday(): Int =
        if (prefs.getString("day", "") == today()) prefs.getInt("count", 0) else 0

    fun increment(): Int {
        val newCount = countToday() + 1
        prefs.edit().putString("day", today()).putInt("count", newCount).apply()
        return newCount
    }

    /** Modo avançado (NoiseEngine HTTP). Desligado por padrão. */
    fun isNoiseEngineEnabled(): Boolean = prefs.getBoolean("noise_engine_enabled", false)

    fun setNoiseEngineEnabled(value: Boolean) {
        prefs.edit().putBoolean("noise_engine_enabled", value).apply()
    }

    fun getGoal(): Int = prefs.getInt("goal", 3)

    fun setGoal(value: Int) {
        prefs.edit().putInt("goal", value.coerceIn(1, 20)).apply()
    }
}
