package com.practice.codingtestapp.di

import android.content.Context
import androidx.room.Room
import com.practice.codingtestapp.api.ApiService
import com.practice.codingtestapp.db.AppDatabase
import com.practice.codingtestapp.db.dao.WorkoutDao
import com.practice.codingtestapp.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://demo2187508.mockable.io/"
    private const val DATABASE_NAME = "workouts_db"

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun providesAppDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun providesWorkoutDao(db: AppDatabase) = db.getWorkoutDao()

    @Singleton
    @Provides
    fun providesRepository(apiService: ApiService, workoutDao: WorkoutDao) =
        RepositoryImpl(apiService, workoutDao)
}