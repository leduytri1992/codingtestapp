package com.practice.codingtestapp.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assignment")
data class Assignment(
    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo(name = "workout_id")
    val workoutId: String?,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "status")
    val status: Int,

    @ColumnInfo(name = "totalExercise")
    val totalExercise: Int,

    @ColumnInfo(name = "completed")
    var completed: Boolean
)
