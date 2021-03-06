package com.practice.codingtestapp.utils

import android.content.Context
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters.previousOrSame
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class Utils {
    companion object {
        /**
         * Gets the first day-of-week {@code int} value.
         *
         * @return the first day-of-week
         */
        fun getFirstDayOfWeek(): Int {
            val monday = LocalDate.now().with(previousOrSame(DayOfWeek.MONDAY)).toString()
            return monday.substring(monday.length - 2, monday.length).toInt()
        }

        /**
         * Gets the day-of-week {@code int} value.
         *
         * @return the today-of-week, from 0 (Monday) to 6 (Sunday)
         */
        fun getCurrentDayOfWeek(): Int {
            return LocalDateTime.now().dayOfWeek.value - 1
        }

        /**
         * Check Network is available or not.
         *
         * @return true if Network is available, else return false.
         */
        fun isNetworkAvailable(context: Context): Boolean {
            val result: Boolean
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
            return result
        }
    }
}