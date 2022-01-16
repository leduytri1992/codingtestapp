package com.practice.codingtestapp.repository

import com.practice.codingtestapp.api.ApiService

class Repository(private val apiService: ApiService) {
    suspend fun getWorkouts() = apiService.getWorkouts()
}