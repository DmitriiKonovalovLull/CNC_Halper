package com.konchak.cnc_halper.core.utils

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateUtils @Inject constructor() {

    fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp)
        val now = Date()

        return when {
            isToday(date) -> {
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
            }
            isYesterday(date) -> {
                "Вчера, " + SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
            }
            isThisYear(date) -> {
                SimpleDateFormat("d MMM, HH:mm", Locale.getDefault()).format(date)
            }
            else -> {
                SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault()).format(date)
            }
        }
    }

    fun formatDateOnly(timestamp: Long): String {
        return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(timestamp))
    }

    fun formatTimeOnly(timestamp: Long): String {
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
    }

    fun formatDuration(milliseconds: Long): String {
        val seconds = milliseconds / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        return when {
            hours > 0 -> String.format("%dч %dм", hours, minutes % 60)
            minutes > 0 -> String.format("%dм %dс", minutes, seconds % 60)
            else -> String.format("%dс", seconds)
        }
    }

    fun isRecent(timestamp: Long, thresholdMinutes: Int = 60): Boolean {
        val now = System.currentTimeMillis()
        return (now - timestamp) < (thresholdMinutes * 60 * 1000)
    }

    private fun isToday(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val today = Calendar.getInstance()

        return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
    }

    private fun isYesterday(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, 1)

        return isToday(calendar.time)
    }

    private fun isThisYear(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        return calendar.get(Calendar.YEAR) == currentYear
    }
}