package com.firebaseserviceandroidapp.core.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticlesItem(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

)