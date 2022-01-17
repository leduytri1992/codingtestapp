package com.practice.codingtestapp.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practice.codingtestapp.api.ApiService
import com.practice.codingtestapp.db.dao.WorkoutDao
import com.practice.codingtestapp.db.model.Assignment
import com.practice.codingtestapp.db.model.Workout
import com.practice.codingtestapp.utils.Mapper.Companion.toAssignment
import com.practice.codingtestapp.utils.Mapper.Companion.toWorkout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService: ApiService,
    private val workoutDao: WorkoutDao
) {

    suspend fun getWorkouts() = apiService.getWorkouts()

    suspend fun loadWorkouts(): List<Workout> {
        var workoutsResult: List<Workout> = emptyList()

        withContext(Dispatchers.IO) {
            val workoutsLocal = workoutDao.getAllWorkouts()
            if (workoutsLocal == null || workoutsLocal.isEmpty()) {
                // Load workouts from api
                val response = apiService.getWorkouts()

                // Handle response data
                when (response.isSuccessful) {
                    true -> {
                        val data = response.body()?.workouts
                        var workout : Workout
                        val workoutsList = mutableListOf<Workout>()
                        var assignments : MutableList<Assignment>

                        data?.forEach { it ->
                            val workoutId = it.id
                            assignments = mutableListOf()
                            it.assignments.forEach {
                                assignments.add(it.toAssignment(workoutId))
                            }
                            workout = it.toWorkout(assignments)
                            workoutsList.add(workout)
                            workoutDao.insertWorkout(workout)
                            workoutDao.insertAssignments(assignments)
                        }
                        workoutsResult = workoutsList
                    }
                    else -> {
                        // Logging the response message
                        Timber.e(response.message())
                    }
                }
            } else {
                workoutsLocal.forEach {
                    // Get list assignments for it
                    val assignments = workoutDao.getAssignmentsBy(it.id)
                    it.assignments = assignments
                }
                workoutsResult = workoutsLocal
            }
        }
        return workoutsResult
    }

    @WorkerThread
    suspend fun updateAssignment(id: String, completed: Boolean) = withContext(Dispatchers.IO) {
        workoutDao.updateAssignmentById(id, completed)
    }
}