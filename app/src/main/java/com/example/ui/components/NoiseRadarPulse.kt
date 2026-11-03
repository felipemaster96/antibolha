package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VpnLock
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.LuminousViolet

@Composable
fun NoiseRadarPulse(
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "RadarTransition")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PulseScale"
    )

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PulseAlpha"
    )

    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SweepAngle"
    )

    Box(
        modifier = modifier.size(160.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(160.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val maxRadius = size.minDimension / 2

            // Static concentric rings
            val ringColor = if (isActive) CyberCyan.copy(alpha = 0.25f) else Color.Gray.copy(alpha = 0.15f)
            drawCircle(color = ringColor, radius = maxRadius * 0.35f, center = center, style = Stroke(width = 1.5.dp.toPx()))
            drawCircle(color = ringColor, radius = maxRadius * 0.65f, center = center, style = Stroke(width = 1.5.dp.toPx()))
            drawCircle(color = ringColor, radius = maxRadius * 0.95f, center = center, style = Stroke(width = 1.5.dp.toPx()))

            // Crosshairs
            drawLine(
                color = ringColor,
                start = Offset(center.x, center.y - maxRadius),
                end = Offset(center.x, center.y + maxRadius),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = ringColor,
                start = Offset(center.x - maxRadius, center.y),
                end = Offset(center.x + maxRadius, center.y),
                strokeWidth = 1.dp.toPx()
            )

            if (isActive) {
                // Expanding wave 1
                drawCircle(
                    color = CyberCyan.copy(alpha = pulseAlpha),
                    radius = maxRadius * pulseScale,
                    center = center,
                    style = Stroke(width = 2.dp.toPx())
                )

                // Expanding wave 2 (offset)
                val secondPulseScale = ((pulseScale + 0.5f) % 1f)
                val secondPulseAlpha = (1f - secondPulseScale) * 0.7f
                drawCircle(
                    color = LuminousViolet.copy(alpha = secondPulseAlpha),
                    radius = maxRadius * secondPulseScale,
                    center = center,
                    style = Stroke(width = 2.dp.toPx())
                )

                // Rotating radar beam
                val beamBrush = Brush.sweepGradient(
                    0.0f to Color.Transparent,
                    0.85f to Color.Transparent,
                    1.0f to CyberCyan.copy(alpha = 0.35f),
                    center = center
                )
                drawArc(
                    brush = beamBrush,
                    startAngle = sweepAngle,
                    sweepAngle = 60f,
                    useCenter = true,
                    topLeft = Offset(center.x - maxRadius, center.y - maxRadius),
                    size = androidx.compose.ui.geometry.Size(maxRadius * 2, maxRadius * 2)
                )
            }
        }

        // Central Shield / Icon
        Icon(
            imageVector = if (isActive) Icons.Default.VpnLock else Icons.Default.Shield,
            contentDescription = if (isActive) "Robô AntiBolha Ativo" else "Robô AntiBolha Pausado",
            tint = if (isActive) EmeraldGreen else Color.Gray,
            modifier = Modifier.size(36.dp)
        )
    }
}
