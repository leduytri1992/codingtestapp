package com.practice.codingtestapp.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.practice.codingtestapp.databinding.ActivityMainBinding
import com.practice.codingtestapp.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUI()
        setUpObservers()
    }

    /**
     * Function setup UI.
     */
    private fun setUpUI() {
        // TODO: Implement later
    }

    /**
     * Function setup observers workout live data.
     */
    private fun setUpObservers() {
        mainViewModel.getWorkouts().observe(this, { workoutsList ->
            workoutsList?.let {
                // TODO: Binding data to UI
            }
        })
    }
}