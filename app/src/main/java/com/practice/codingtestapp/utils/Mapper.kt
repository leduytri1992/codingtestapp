package com.practice.codingtestapp.utils

import com.practice.codingtestapp.api.AssignmentResponse
import com.practice.codingtestapp.api.WorkoutResponse
import com.practice.codingtestapp.db.model.Assignment
import com.practice.codingtestapp.db.model.Workout

class Mapper {
    companion object {

        /**
         * Mapping from workout response to model.
         *
         * @param assignments The {@link List<Assignment>} of the workout model
         */
        fun WorkoutResponse.toWorkout(assignments: List<Assignment>) = Workout(
            id = id,
            day = day,
            assignments = assignments // use to render UI
        )

        /**
         * Mapping from assignment response to model.
         *
         * @param workoutId The {@link String} workout id
         */
        fun AssignmentResponse.toAssignment(workoutId: String?) = Assignment(
            id = id,
            workoutId = workoutId,
            title = title,
            status = status,
            totalExercise = totalExercise,
            completed = false
        )
    }
}