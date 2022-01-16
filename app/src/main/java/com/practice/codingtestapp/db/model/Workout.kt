package com.practice.codingtestapp.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Workout(
    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo(name = "day")
    val day: Int,

    @ColumnInfo(name = "assignments")
    val assignments: List<Assignment>
)