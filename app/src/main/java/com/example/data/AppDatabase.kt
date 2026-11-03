package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.BubbleDao
import com.example.data.model.BubbleCategory
import com.example.data.model.BubbleKeyword
import com.example.data.model.NoiseLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [BubbleCategory::class, BubbleKeyword::class, NoiseLog::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bubbleDao(): BubbleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "antibolha_database"
                )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            seedDatabase(getInstance(context).bubbleDao())
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun seedDatabase(dao: BubbleDao) {
            val count = dao.getCategoryCount()
            if (count == 0) {
                InitialBubbleData.seedData.forEach { seed ->
                    val categoryId = dao.insertCategory(seed.category.copy(id = 0))
                    val keywords = seed.keywords.map { term ->
                        BubbleKeyword(
                            categoryId = categoryId,
                            term = term
                        )
                    }
                    dao.insertKeywords(keywords)
                }
            }
        }
    }
}
