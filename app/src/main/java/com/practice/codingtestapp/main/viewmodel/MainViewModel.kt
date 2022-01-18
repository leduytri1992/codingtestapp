package com.practice.codingtestapp.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.codingtestapp.db.model.Workout
import com.practice.codingtestapp.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: RepositoryImpl) : ViewModel() {
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
    fun loadWorkouts() {
        viewModelScope.launch {
            val workouts = repository.loadWorkouts()
            workoutsLiveData.postValue(workouts)
        }
    }

    /**
     * Update the completed status for assignment by id.
     *
     * @param id the {@link String} Assignment id
     * @param completed the {@link Boolean} completed status info
     */
    fun updateAssignment(id: String, completed: Boolean) {
        viewModelScope.launch {
            repository.updateAssignment(id, completed)
        }
    }
}