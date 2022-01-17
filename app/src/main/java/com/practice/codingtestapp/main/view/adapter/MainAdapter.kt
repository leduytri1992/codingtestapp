package com.practice.codingtestapp.main.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View.*
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.practice.codingtestapp.R
import com.practice.codingtestapp.databinding.ItemMainBinding
import com.practice.codingtestapp.databinding.ItemWorkoutBinding
import com.practice.codingtestapp.db.model.Assignment
import com.practice.codingtestapp.db.model.Workout
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

        fun bind(workout: Workout) {
            binding.apply {
                workout.also { (id, day, assignments) ->
                    Timber.d("Workout: $id, $day, $assignments")
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
        private fun showAssignments(assignments: List<Assignment>?) {
            if (assignments != null && assignments.isNotEmpty()) {
                // Display item one
                val itemOne = assignments[0]
                updateAssignmentItem(binding.itemOne, itemOne)
                // item clicked event
                binding.itemOne.viewWorkout.setOnClickListener {
                    Timber.d("clicked ${itemOne.id}")

                    if (itemOne.completed) {
                        itemOne.completed = false
                        updateAssignmentItem(binding.itemOne, itemOne)
                    } else {
                        itemOne.completed = true
                        updateAssignmentItem(binding.itemOne, itemOne)
                    }
                    listener.onItemClicked(itemOne.id, itemOne.completed)
                }

                // Display item two
                if (assignments.size > 1) {
                    val itemTwo = assignments[1]
                    updateAssignmentItem(binding.itemTwo, itemTwo)
                    // item clicked event
                    binding.itemTwo.viewWorkout.setOnClickListener {
                        Timber.d("clicked ${itemTwo.id}")

                        if (itemTwo.completed) {
                            itemTwo.completed = false
                            updateAssignmentItem(binding.itemTwo, itemTwo)
                        } else {
                            itemTwo.completed = true
                            updateAssignmentItem(binding.itemTwo, itemTwo)
                        }
                        listener.onItemClicked(itemTwo.id, itemTwo.completed)
                    }
                }
            } else {
                binding.itemOne.viewWorkout.visibility = GONE
                binding.itemTwo.viewWorkout.visibility = GONE
            }
        }

        @SuppressLint("SetTextI18n")
        private fun updateAssignmentItem(itemView: ItemWorkoutBinding, assignment: Assignment) {
            // update title
            itemView.viewWorkout.visibility = VISIBLE
            itemView.tvTitle.text = assignment.title

            // Update background color, icon, text views
            if (assignment.completed) {
                itemView.viewWorkout.setBackgroundResource(R.drawable.bg_item_workout_completed)
                itemView.imgIconCompleted.visibility = VISIBLE
                itemView.tvTitle.apply {
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                itemView.tvStatus.apply {
                    text = context.getText(R.string.completed)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            } else {
                itemView.viewWorkout.setBackgroundResource(R.drawable.bg_item_workout_gray)
                itemView.imgIconCompleted.visibility = GONE
                itemView.tvTitle.apply {
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                }
                itemView.tvStatus.apply {
                    text = context.getString(R.string.exercises, assignment.totalExercise)
                    setTextColor(ContextCompat.getColor(context, R.color.black_200))
                }
            }
        }
    }

    interface AssignmentListener {
        fun onItemClicked(id: String, completed: Boolean)
    }
}