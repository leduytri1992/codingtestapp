package com.practice.codingtestapp.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.codingtestapp.api.Assignment
import com.practice.codingtestapp.databinding.ActivityMainBinding
import com.practice.codingtestapp.main.view.adapter.MainAdapter
import com.practice.codingtestapp.main.viewmodel.MainViewModel
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

    /**
     * Function setup UI.
     */
    private fun setUpUI() {
        mainAdapter.listener = this
        binding.mainRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mainAdapter
        }
    }

    /**
     * Function setup observers workout live data.
     */
    private fun setUpObservers() {
        mainViewModel.getWorkouts().observe(this, { workoutsList ->
            workoutsList?.let {
                binding.mainRecyclerview.apply {
                    with(adapter as MainAdapter) {
                        workouts = it
                        notifyDataSetChanged()
                    }
                }
            }
        })
    }

    override fun onItemClicked(item: Assignment) {
        TODO("Not yet implemented")
    }
}