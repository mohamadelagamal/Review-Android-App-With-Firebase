package com.firebaseserviceandroidapp.core.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.firebaseserviceandroidapp.core.local.entity.ArticlesItem

@Dao
interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: List<ArticlesItem>)

    @Query("SELECT * FROM articles")
    suspend fun getAllArticles(): List<ArticlesItem>
}