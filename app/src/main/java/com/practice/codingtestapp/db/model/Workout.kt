package com.practice.codingtestapp.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "workout")
data class Workout(
    @PrimaryKey(autoGenerate = false)
    var id: String,

    @ColumnInfo(name = "day")
    var day: Int,

    @Ignore
    var assignments: List<Assignment>? // use to render UI
) {
    constructor() : this("", 0, null)
}