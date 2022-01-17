package com.practice.codingtestapp.api

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("data")
    val workouts: List<WorkoutResponse>
)

data class WorkoutResponse(
    @SerializedName("_id")
    val id: String,
    @SerializedName("day")
    val day: Int,
    @SerializedName("assignments")
    val assignments: List<AssignmentResponse>
)

data class AssignmentResponse(
    @SerializedName("_id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("total_exercise")
    val totalExercise: Int
)