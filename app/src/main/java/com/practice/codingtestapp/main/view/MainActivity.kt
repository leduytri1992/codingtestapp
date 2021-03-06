package com.practice.codingtestapp.main.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.codingtestapp.databinding.ActivityMainBinding
import com.practice.codingtestapp.db.model.Workout
import com.practice.codingtestapp.main.view.adapter.MainAdapter
import com.practice.codingtestapp.main.view.adapter.MainAdapter.Companion.DEFAULT
import com.practice.codingtestapp.main.viewmodel.MainViewModel
import com.practice.codingtestapp.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainAdapter.AssignmentListener {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUI()
        setUpObservers()
    }

    override fun onResume() {
        super.onResume()
        // Check current day changed in system, then notify data changed
        val today = mainAdapter.today
        if (today != DEFAULT && today != Utils.getCurrentDayOfWeek()) {
            with(mainAdapter) {
                notifyDataChanged()
            }
        }
    }

    /**
     * Setup and initialize UI.
     */
    private fun setUpUI() {
        mainAdapter.listener = this
        binding.mainRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mainAdapter
            with(adapter as MainAdapter) {
                workouts = initWorkouts()
                notifyDataChanged()
            }
        }
    }

    /**
     * Setup observers workout live data.
     */
    private fun setUpObservers() {
        mainViewModel.getWorkouts().observe(this, { workoutsList ->
            workoutsList?.let {
                if (it.isNotEmpty()) {
                    binding.mainRecyclerview.apply {
                        with(adapter as MainAdapter) {
                            workouts = it
                            notifyDataChanged()
                        }
                    }
                }
            }
        })
    }

    /**
     * Init workouts items to render empty data UI.
     */
    private fun initWorkouts(): List<Workout> {
        var workout: Workout
        val workoutsList = mutableListOf<Workout>()
        (0..6).forEach { i ->
            workout = Workout("", i, emptyList())
            workoutsList.add(workout)
        }
        return workoutsList
    }

    /**
     * On event clicked item assignment.
     */
    override fun onItemClicked(id: String, completed: Boolean) {
        mainViewModel.updateAssignment(id, completed)
    }
}