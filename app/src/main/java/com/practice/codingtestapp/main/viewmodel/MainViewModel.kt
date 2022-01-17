package com.practice.codingtestapp.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.codingtestapp.api.WorkoutResponse
import com.practice.codingtestapp.db.model.Workout
import com.practice.codingtestapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private var workoutsLiveData = MutableLiveData<List<Workout>>()

    init {
        loadWorkouts()
    }

    /**
     * Get workout live data.
     */
    fun getWorkouts() = workoutsLiveData

    /**
     * Load list workouts from repository.
     */
    private fun loadWorkouts() {
        viewModelScope.launch {
            val workouts = repository.loadWorkouts()
            workoutsLiveData.postValue(workouts)
        }
    }

    fun updateAssignment(id: String, completed: Boolean) {
        viewModelScope.launch {
            repository.updateAssignment(id, completed)
        }
    }
}