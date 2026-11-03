package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bubble_keywords",
    foreignKeys = [
        ForeignKey(
            entity = BubbleCategory::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class BubbleKeyword(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val categoryId: Long,
    val term: String,
    val targetPlatform: String = "ALL", // ALL, YOUTUBE, GOOGLE, TIKTOK
    val isEnabled: Boolean = true,
    val usageCount: Int = 0,
    val lastUsedAt: Long? = null
)
