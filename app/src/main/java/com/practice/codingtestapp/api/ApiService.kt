package com.practice.codingtestapp.api

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("workouts")
    suspend fun getWorkouts(): Response<ApiResponse>
}