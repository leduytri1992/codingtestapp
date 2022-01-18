package com.practice.codingtestapp.db.dao

import androidx.room.*
import com.practice.codingtestapp.db.model.Assignment
import com.practice.codingtestapp.db.model.Workout

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workouts: Workout)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignments(assignments: List<Assignment>)

    @Query("SELECT * FROM workout ORDER BY day ASC")
    suspend fun getAllWorkouts(): MutableList<Workout>?

    @Query("SELECT * FROM assignment WHERE workout_id = :workoutId")
    suspend fun getAssignmentsBy(workoutId: String): List<Assignment>?

    /**
     * Updating only completed info
     * By assignment id
     */
    @Query("UPDATE assignment SET completed = :completed WHERE id = :id")
    suspend fun updateAssignmentById(id: String, completed: Boolean)
}