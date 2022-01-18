package com.practice.codingtestapp.main.view.adapter

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.View.*
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.practice.codingtestapp.R
import com.practice.codingtestapp.databinding.ItemMainBinding
import com.practice.codingtestapp.databinding.ItemWorkoutBinding
import com.practice.codingtestapp.db.model.Assignment
import com.practice.codingtestapp.db.model.Workout
import com.practice.codingtestapp.utils.Utils
import timber.log.Timber
import javax.inject.Inject

class MainAdapter @Inject constructor() : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    var workouts: List<Workout> = emptyList()
    lateinit var listener: AssignmentListener
    val today = Utils.getCurrentDayOfWeek()

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
                    showAssignments(assignments, day)
                }
            }
        }

        private fun showDay(day: Int) {
            if (day == today) {
                binding.tvDay.apply {
                    setTextColor(ContextCompat.getColor(context, R.color.purple))
                }
                binding.tvDate.apply {
                    setTextColor(ContextCompat.getColor(context, R.color.purple))
                }
            }

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

        /**
         * Show date number of the current week.
         */
        private fun showDate(day: Int) {
            var date = Utils.getFirstDayOfWeek() // monday
            when (day) {
                1 -> date += 1 // Tue
                2 -> date += 2 // Wed
                3 -> date += 3 // Thur
                4 -> date += 4 // Fri
                5 -> date += 5 // Sat
                6 -> date += 6 // Sun
            }
            binding.tvDate.text = date.toString()
        }

        /**
         * FIXME: Fix and refactor use the Recyclerview to render assignment items.
         */
        private fun showAssignments(assignments: List<Assignment>?, day: Int) {
            if (assignments != null && assignments.isNotEmpty()) {
                // Display item one
                val itemOne = assignments[0]
                updateStatus(binding.itemOne, itemOne)
                updateAssignmentItem(binding.itemOne, itemOne, day)

                // item clicked event
                binding.itemOne.viewWorkout.setOnClickListener {
                    Timber.d("clicked ${itemOne.id}")

                    if (itemOne.status == Status.MISSED.value ||
                        itemOne.status == Status.COMPLETED.value ||
                        day > today
                    ) {
                        return@setOnClickListener
                    }

                    if (itemOne.completed) {
                        itemOne.completed = false
                        updateAssignmentItem(binding.itemOne, itemOne, day)
                    } else {
                        itemOne.completed = true
                        updateAssignmentItem(binding.itemOne, itemOne, day)
                    }
                    listener.onItemClicked(itemOne.id, itemOne.completed)
                }

                // Display item two
                if (assignments.size > 1) {
                    val itemTwo = assignments[1]
                    updateStatus(binding.itemTwo, itemTwo)
                    updateAssignmentItem(binding.itemTwo, itemTwo, day)

                    // item clicked event
                    binding.itemTwo.viewWorkout.setOnClickListener {
                        Timber.d("clicked ${itemTwo.id}")

                        if (itemTwo.status == Status.MISSED.value ||
                            itemTwo.status == Status.COMPLETED.value ||
                            day > today
                        ) {
                            return@setOnClickListener
                        }

                        if (itemTwo.completed) {
                            itemTwo.completed = false
                            updateAssignmentItem(binding.itemTwo, itemTwo, day)
                        } else {
                            itemTwo.completed = true
                            updateAssignmentItem(binding.itemTwo, itemTwo, day)
                        }
                        listener.onItemClicked(itemTwo.id, itemTwo.completed)
                    }
                }
            } else {
                binding.itemOne.viewWorkout.visibility = GONE
                binding.itemTwo.viewWorkout.visibility = GONE
            }
        }

        @SuppressLint("WrongConstant")
        private fun updateStatus(itemView: ItemWorkoutBinding, assignment: Assignment) {
            // update status
            itemView.tvStatus.apply {
                when (assignment.status) {
                    Status.MISSED.value -> {
                        text = HtmlCompat.fromHtml(
                            context.getString(
                                R.string.missed_exercises,
                                assignment.totalExercise
                            ),
                            Html.FROM_HTML_MODE_LEGACY
                        )
                    }
                    Status.ASSIGNED.value -> {
                        text = context.getString(R.string.exercises, assignment.totalExercise)
                    }
                }
            }
        }

        @SuppressLint("SetTextI18n")
        private fun updateAssignmentItem(
            itemView: ItemWorkoutBinding,
            assignment: Assignment,
            day: Int
        ) {
            // update title
            itemView.viewWorkout.visibility = VISIBLE
            itemView.tvTitle.text = assignment.title

            // Update background color, icon, text views
            if (assignment.completed || assignment.status == Status.COMPLETED.value) {
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
                    if (day <= today) {
                        setTextColor(ContextCompat.getColor(context, R.color.black))
                    } else {
                        setTextColor(ContextCompat.getColor(context, R.color.gray_200))
                    }
                }
                itemView.tvStatus.apply {
                    if (assignment.status != Status.MISSED.value) {
                        text = context.getString(R.string.exercises, assignment.totalExercise)
                    }
                    if (day <= today) {
                        setTextColor(ContextCompat.getColor(context, R.color.black_200))
                    } else {
                        setTextColor(ContextCompat.getColor(context, R.color.gray_200))
                    }
                }
            }
        }
    }

    interface AssignmentListener {
        fun onItemClicked(id: String, completed: Boolean)
    }
}