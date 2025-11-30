package com.konchak.cnc_halper.core.utils

import java.util.Calendar
import java.util.Date

/**
 * Determines the current shift (1, 2, or 3) based on the current date.
 * The logic is a simple rotation:
 * - Day 1: Shift 1
 * - Day 2: Shift 2
 * - Day 3: Shift 3
 * - Day 4: Shift 1
 * ... and so on.
 *
 * This assumes a 3-day cycle for shifts.
 */
fun getCurrentShiftBasedOnDate(date: Date = Date()): String {
    val calendar = Calendar.getInstance()
    calendar.time = date

    // Get the day of the year (1 to 365/366)
    val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)

    // Use modulo 3 to get a cycle of 0, 1, 2
    // Add 1 to make it 1, 2, 3
    val shiftNumber = (dayOfYear % 3) + 1

    return shiftNumber.toString()
}
