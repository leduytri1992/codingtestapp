package com.practice.codingtestapp.main.view.adapter

import android.view.LayoutInflater
import android.view.View.*
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.practice.codingtestapp.R
import com.practice.codingtestapp.api.Assignment
import com.practice.codingtestapp.api.Workout
import com.practice.codingtestapp.databinding.ItemMainBinding
import timber.log.Timber
import javax.inject.Inject

class MainAdapter @Inject constructor() : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    var workouts: List<Workout> = emptyList()
    lateinit var listener: AssignmentListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemMainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(workouts[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = workouts.size

    inner class ViewHolder(private val binding: ItemMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            /*binding.workoutsRecyclerview.apply {
                layoutManager = CustomLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                isNestedScrollingEnabled = false
                adapter = workoutsAdapter
            }*/
        }

        fun bind(workout: Workout) {
            binding.apply {
                workout.also { (id, day, assignments) ->
                    Timber.d("data: $id, $day, $assignments")
                    showDay(day)
                    showDate(day)
                    showAssignments(assignments)
                }
            }
        }

        private fun showDay(day: Int) {
            binding.tvDay.apply {
                when (day) {
                    0 -> text = context.getString(R.string.monday)
                    1 -> text = context.getString(R.string.tuesday)
                    2 -> text = context.getString(R.string.wednesday)
                    3 -> text = context.getString(R.string.thursday)
                    4 -> text = context.getString(R.string.friday)
                    5 -> text = context.getString(R.string.saturday)
                    6 -> text = context.getString(R.string.sunday)
                }
            }
        }

        private fun showDate(day: Int) {
            binding.tvDate.apply {
                when (day) {
                    0 -> text = "1" // first date of the current weekend
                    1 -> text = "2"
                    2 -> text = "3"
                    3 -> text = "4"
                    4 -> text = "5"
                    5 -> text = "6"
                    6 -> text = "7"
                }
            }
        }

        /**
         * FIXME: Fix and refactor use the Recyclerview to render assignment items.
         */
        private fun showAssignments(assignments: List<Assignment>) {
            if (assignments.isNotEmpty()) {
                // Display item one
                val itemOne = assignments[0]
                binding.itemOne.viewWorkout.visibility = VISIBLE
                binding.itemOne.tvTitle.text = itemOne.title
                "${itemOne.totalExercise} exercises".also {
                    binding.itemOne.tvStatus.text = it
                }
                binding.itemOne.viewWorkout.setOnClickListener {
                    Timber.d("clicked ${itemOne.id}")
                    binding.itemOne.viewWorkout.setBackgroundResource(R.drawable.bg_item_workout_completed)
                    binding.itemOne.imgIconCompleted.visibility = VISIBLE
                    binding.itemOne.tvTitle.apply {
                        setTextColor(ContextCompat.getColor(context, R.color.white))
                    }
                    binding.itemOne.tvStatus.apply {
                        setTextColor(ContextCompat.getColor(context, R.color.white))
                    }

                    listener.onItemClicked(itemOne)
                }

                // Display item two
                if (assignments.size > 1) {
                    val itemTwo = assignments[1]
                    binding.itemTwo.viewWorkout.visibility = VISIBLE
                    binding.itemTwo.tvTitle.text = itemTwo.title
                    "${itemTwo.totalExercise} exercises".also {
                        binding.itemTwo.tvStatus.text = it
                    }
                    binding.itemTwo.viewWorkout.setOnClickListener {
                        Timber.d("clicked ${itemTwo.id}")
                        binding.itemTwo.viewWorkout.setBackgroundResource(R.drawable.bg_item_workout_completed)
                        binding.itemTwo.imgIconCompleted.visibility = VISIBLE
                        binding.itemTwo.tvTitle.apply {
                            setTextColor(ContextCompat.getColor(context, R.color.white))
                        }
                        binding.itemTwo.tvStatus.apply {
                            setTextColor(ContextCompat.getColor(context, R.color.white))
                        }

                        listener.onItemClicked(itemTwo)
                    }
                }
            } else {
                binding.itemOne.viewWorkout.visibility = GONE
                binding.itemTwo.viewWorkout.visibility = GONE
            }
        }
    }

    interface AssignmentListener {
        fun onItemClicked(item: Assignment)
    }
}