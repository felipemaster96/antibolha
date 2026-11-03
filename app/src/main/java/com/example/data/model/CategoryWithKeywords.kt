package com.example.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithKeywords(
    @Embedded
    val category: BubbleCategory,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val keywords: List<BubbleKeyword>
)
