package com.practice.codingtestapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practice.codingtestapp.db.dao.WorkoutDao
import com.practice.codingtestapp.db.model.Assignment
import com.practice.codingtestapp.db.model.Workout

@Database(
    entities = [Workout::class, Assignment::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getWorkoutDao(): WorkoutDao
}