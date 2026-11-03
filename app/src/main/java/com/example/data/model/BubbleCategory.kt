package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bubble_categories")
data class BubbleCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val iconEmoji: String,
    val colorHex: String,
    val isEnabled: Boolean = true,
    val instagramTag: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
