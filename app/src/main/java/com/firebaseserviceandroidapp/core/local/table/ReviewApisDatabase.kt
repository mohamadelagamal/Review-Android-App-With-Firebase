package com.firebaseserviceandroidapp.core.local.table

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.firebaseserviceandroidapp.core.constants.Constants
import com.firebaseserviceandroidapp.core.local.dao.ArticlesDao
import com.firebaseserviceandroidapp.core.local.entity.ArticlesItem

@Database(entities = [ArticlesItem::class], version = 1)
//@TypeConverters(Converters::class)
abstract class ReviewApisDatabase : RoomDatabase() {
    abstract fun articlesDao(): ArticlesDao

    companion object {
        private var INSTANCE: ReviewApisDatabase? = null

        fun getDatabase(context: Context): ReviewApisDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReviewApisDatabase::class.java,
                    Constants.DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}