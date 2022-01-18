package com.practice.codingtestapp.repository

import androidx.annotation.WorkerThread
import com.practice.codingtestapp.api.ApiService
import com.practice.codingtestapp.core.CodingTestApplication
import com.practice.codingtestapp.db.dao.WorkoutDao
import com.practice.codingtestapp.db.model.Assignment
import com.practice.codingtestapp.db.model.Workout
import com.practice.codingtestapp.utils.Mapper.Companion.toAssignment
import com.practice.codingtestapp.utils.Mapper.Companion.toWorkout
import com.practice.codingtestapp.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val workoutDao: WorkoutDao
) : Repository {

    override suspend fun loadWorkouts(): List<Workout> {
        var workoutsResult: List<Workout> = emptyList()

        withContext(Dispatchers.IO) {
            // Get workouts from local
            val workoutsLocal = workoutDao.getAllWorkouts()

            if (workoutsLocal == null || workoutsLocal.isEmpty()) {
                // Check network available
                if (Utils.isNetworkAvailable(CodingTestApplication.appContext)) {
                    // Load workouts from api
                    val response = apiService.getWorkouts()

                    // Handle response data
                    when (response.isSuccessful) {
                        true -> {
                            val data = response.body()?.workouts
                            var workout: Workout
                            val workoutsList = mutableListOf<Workout>()
                            var assignments: MutableList<Assignment>

                            data?.forEach { it ->
                                val workoutId = it.id
                                assignments = mutableListOf()
                                it.assignments.forEach {
                                    assignments.add(it.toAssignment(workoutId))
                                }
                                workout = it.toWorkout(assignments)
                                workoutsList.add(workout)

                                // Save data to local
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
    override suspend fun updateAssignment(id: String, completed: Boolean) =
        withContext(Dispatchers.IO) {
            workoutDao.updateAssignmentById(id, completed)
        }
}