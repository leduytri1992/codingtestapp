package com.practice.codingtestapp.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Assignment(
    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "status")
    val status: Int,

    @ColumnInfo(name = "totalExercise")
    val totalExercise: Int,

    @ColumnInfo(name = "completed")
    val completed: Boolean
)
