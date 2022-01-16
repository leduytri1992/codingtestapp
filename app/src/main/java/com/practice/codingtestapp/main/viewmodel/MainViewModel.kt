package com.practice.codingtestapp.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.codingtestapp.api.Workout
import com.practice.codingtestapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val workoutLiveData = MutableLiveData<List<Workout>?>()

    init {
        loadWorkouts()
    }

    /**
     * Get workout live data.
     */
    fun getWorkouts() = workoutLiveData

    /**
     * Load list workouts.
     */
    private fun loadWorkouts() {
        viewModelScope.launch {
            // Get workouts from repository
            val response = repository.getWorkouts()

            // Handle response data
            when (response.isSuccessful) {
                true -> {
                    val workouts = response.body()?.workouts
                    workouts?.let {
                        workoutLiveData.postValue(workouts)
                    }
                }
                else -> {
                    // Logging the response message
                    Timber.e(response.message())
                }
            }
        }
    }
}