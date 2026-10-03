package com.example.util

object NumberUtil {
    fun formatNumber(number: Long, usePersian: Boolean): String {
        val str = number.toString()
        if (!usePersian) return str
        val persianDigits = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
        val sb = StringBuilder()
        for (ch in str) {
            if (ch in '0'..'9') {
                sb.append(persianDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    fun formatNumber(number: Int, usePersian: Boolean): String {
        return formatNumber(number.toLong(), usePersian)
    }
}
