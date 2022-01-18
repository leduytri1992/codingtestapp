package com.practice.codingtestapp.main.view.adapter

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.View.*
import android.view.ViewGroup
import android.widget.TextView
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
    var today: Int = DEFAULT

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

    @SuppressLint("NotifyDataSetChanged")
    fun notifyDataChanged() {
        today = Utils.getCurrentDayOfWeek()
        notifyDataSetChanged()
    }

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
                setTextColor(binding.tvDay, R.color.purple)
                setTextColor(binding.tvDate, R.color.purple)
            } else {
                setTextColor(binding.tvDay, R.color.black)
                setTextColor(binding.tvDate, R.color.black_200)
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
         * Show assignment items of workout, and handle event clicked items.
         * <p>
         * Know issue: Limit number items show on UI.
         * FIXME: Fix and refactor use the Recyclerview to render assignment items.
         *
         * @param assignments the list assignment items
         * @param day the day of the workout data
         */
        private fun showAssignments(assignments: List<Assignment>?, day: Int) {
            if (assignments != null && assignments.isNotEmpty()) {
                // show item one
                var item = assignments[0]
                showItemView(binding.itemOne, item, day)

                // show item two
                if (assignments.size > 1) {
                    item = assignments[1]
                    showItemView(binding.itemTwo, item, day)
                }

                // show item three
                if (assignments.size > 2) {
                    item = assignments[2]
                    showItemView(binding.itemThree, item, day)
                }
            } else {
                binding.itemOne.viewWorkout.visibility = GONE
                binding.itemTwo.viewWorkout.visibility = GONE
                binding.itemThree.viewWorkout.visibility = GONE
            }
        }

        private fun showItemView(itemView: ItemWorkoutBinding, item: Assignment, day: Int) {
            updateStatus(itemView, item)
            updateAssignmentItem(itemView, item, day)
            handleEventItemClicked(itemView, item, day)
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
                setTextColor(itemView.tvTitle, R.color.white)
                itemView.tvStatus.apply {
                    text = context.getText(R.string.completed)
                    setTextColor(this, R.color.white)
                }
            } else {
                itemView.viewWorkout.setBackgroundResource(R.drawable.bg_item_workout_gray)
                itemView.imgIconCompleted.visibility = GONE
                if (day <= today) {
                    setTextColor(itemView.tvTitle, R.color.black)
                } else {
                    setTextColor(itemView.tvTitle, R.color.gray_200)
                }
                itemView.tvStatus.apply {
                    if (assignment.status != Status.MISSED.value) {
                        text = context.getString(R.string.exercises, assignment.totalExercise)
                    }
                    if (day <= today) {
                        setTextColor(this, R.color.black_200)
                    } else {
                        setTextColor(this, R.color.gray_200)
                    }
                }
            }
        }

        private fun setTextColor(textView: TextView, colorId: Int) {
            textView.apply {
                setTextColor(ContextCompat.getColor(context, colorId))
            }
        }

        /**
         * Handle event clicked on assignment item.
         *
         * @param itemView the view show item data
         * @param item the assignment item data
         * @param day the day of the workout data
         */
        private fun handleEventItemClicked(itemView: ItemWorkoutBinding, item: Assignment, day: Int) {
            itemView.viewWorkout.setOnClickListener {
                Timber.d("clicked item: ${item.id}")

                if (item.status == Status.MISSED.value ||
                    item.status == Status.COMPLETED.value ||
                    day > today
                ) {
                    return@setOnClickListener
                }

                if (item.completed) {
                    item.completed = false
                    updateAssignmentItem(itemView, item, day)
                } else {
                    item.completed = true
                    updateAssignmentItem(itemView, item, day)
                }
                listener.onItemClicked(item.id, item.completed)
            }
        }
    }

    interface AssignmentListener {
        /**
         * On item clicked event listener.
         *
         * @param id the {@link String} Assignment id
         * @param completed the {@link Boolean} completed info
         */
        fun onItemClicked(id: String, completed: Boolean)
    }

    companion object {
        const val DEFAULT = -1
    }
}