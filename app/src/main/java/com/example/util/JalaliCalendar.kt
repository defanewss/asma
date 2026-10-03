package com.example.util

import java.util.Calendar

data class JalaliDate(val year: Int, val month: Int, val day: Int, val monthName: String)

object JalaliCalendar {
    private val persianMonths = listOf(
        "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
    )

    fun getCurrentJalaliDate(): JalaliDate {
        val cal = Calendar.getInstance()
        val gYear = cal.get(Calendar.YEAR)
        val gMonth = cal.get(Calendar.MONTH) + 1
        val gDay = cal.get(Calendar.DAY_OF_MONTH)
        return gregorianToJalali(gYear, gMonth, gDay)
    }

    fun gregorianToJalali(gy: Int, gm: Int, gd: Int): JalaliDate {
        val gDaysInMonth = intArrayOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        val jDaysInMonth = intArrayOf(0, 31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)

        var gy2 = if (gm > 2) gy + 1 else gy
        var days = 355666 + (365 * gy) + ((gy2 + 3) / 4) - ((gy2 + 99) / 100) + ((gy2 + 399) / 400) + gd
        for (i in 0 until gm) {
            days += gDaysInMonth[i]
        }
        var jy = -1595 + (33 * (days / 12053))
        days %= 12053
        jy += 4 * (days / 1461)
        days %= 1461
        if (days > 365) {
            jy += (days - 1) / 365
            days = (days - 1) % 365
        }
        val jm = when {
            days < 186 -> 1 + (days / 31)
            else -> 7 + ((days - 186) / 30)
        }
        val jd = 1 + when {
            days < 186 -> days % 31
            else -> (days - 186) % 30
        }
        val mName = if (jm in 1..12) persianMonths[jm - 1] else ""
        return JalaliDate(jy, jm, jd, mName)
    }

    fun getFormattedDateString(year: Int, month: Int, day: Int): String {
        return String.format("%04d-%02d-%02d", year, month, day)
    }
}
