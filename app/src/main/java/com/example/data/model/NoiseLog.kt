package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "noise_logs")
data class NoiseLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val keyword: String,
    val categoryName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String, // SUCCESS, FAILED
    val httpCode: Int,
    val targetEngine: String, // Wikipedia API, DuckDuckGo API, Open Search
    val responseTimeMs: Long,
    val queryUrl: String = "",
    val details: String = ""
)
