package com.practice.codingtestapp.repository

import com.practice.codingtestapp.db.model.Workout

interface Repository {

    /**
     * Load workouts data api service first time, and then cached to local db.
     *
     * @return {@link List<Workout>}
     */
    suspend fun loadWorkouts(): List<Workout>

    /**
     * Update the completed status for assignment record by id.
     *
     * @param id the {@link String} Assignment id
     * @param completed the {@link Boolean} completed status info
     */
    suspend fun updateAssignment(id: String, completed: Boolean)
}