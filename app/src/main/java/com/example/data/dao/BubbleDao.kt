package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.model.BubbleCategory
import com.example.data.model.BubbleKeyword
import com.example.data.model.CategoryWithKeywords
import com.example.data.model.NoiseLog
import kotlinx.coroutines.flow.Flow

@Dao
interface BubbleDao {

    @Query("SELECT * FROM bubble_categories ORDER BY id ASC")
    fun getAllCategories(): Flow<List<BubbleCategory>>

    @Transaction
    @Query("SELECT * FROM bubble_categories ORDER BY id ASC")
    fun getCategoriesWithKeywords(): Flow<List<CategoryWithKeywords>>

    @Transaction
    @Query("SELECT * FROM bubble_categories ORDER BY id ASC")
    suspend fun getCategoriesWithKeywordsSync(): List<CategoryWithKeywords>

    @Query("SELECT COUNT(*) FROM bubble_categories")
    suspend fun getCategoryCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: BubbleCategory): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<BubbleCategory>): List<Long>

    @Update
    suspend fun updateCategory(category: BubbleCategory)

    @Delete
    suspend fun deleteCategory(category: BubbleCategory)

    @Query("SELECT * FROM bubble_keywords WHERE isEnabled = 1")
    suspend fun getAllEnabledKeywords(): List<BubbleKeyword>

    @Query("SELECT * FROM bubble_keywords WHERE categoryId = :categoryId")
    fun getKeywordsForCategory(categoryId: Long): Flow<List<BubbleKeyword>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeyword(keyword: BubbleKeyword): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeywords(keywords: List<BubbleKeyword>): List<Long>

    @Update
    suspend fun updateKeyword(keyword: BubbleKeyword)

    @Delete
    suspend fun deleteKeyword(keyword: BubbleKeyword)

    @Query("UPDATE bubble_keywords SET usageCount = usageCount + 1, lastUsedAt = :timestamp WHERE id = :id")
    suspend fun incrementKeywordUsage(id: Long, timestamp: Long)

    @Query("SELECT * FROM noise_logs ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentNoiseLogs(limit: Int = 100): Flow<List<NoiseLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoiseLog(log: NoiseLog): Long

    @Query("DELETE FROM noise_logs")
    suspend fun clearNoiseLogs()

    @Query("SELECT COUNT(*) FROM noise_logs")
    fun getTotalNoiseCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM noise_logs")
    suspend fun getTotalNoiseCount(): Int
}
