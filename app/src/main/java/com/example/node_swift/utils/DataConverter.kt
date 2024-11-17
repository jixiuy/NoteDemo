package com.example.node_swift.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object DateConverter {

    /**
     * 将字符串格式的日期转换为Unix时间戳（毫秒级）
     * @param dateString 字符串格式的日期，格式应为 "yyyy-MM-dd"
     * @param timeZone 时区，默认为系统默认时区
     * @return 对应的Unix时间戳（毫秒级）
     */
    fun convertToTimestamp(dateString: String, timeZone: ZoneId = ZoneId.systemDefault()): Long {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDate = LocalDate.parse(dateString, formatter)
        val zonedDateTime = localDate.atStartOfDay(timeZone)
        return zonedDateTime.toInstant().toEpochMilli()
    }

    /**
     * 将Unix时间戳（毫秒级）转换为字符串格式的日期
     * @param timestamp Unix时间戳（毫秒级）
     * @param timeZone 时区，默认为系统默认时区
     * @return 格式为 "yyyy-MM-dd" 的日期字符串
     */
    fun convertToDateString(timestamp: Long, timeZone: ZoneId = ZoneId.systemDefault()): String {
        val instant = Instant.ofEpochMilli(timestamp)
        val zonedDateTime = instant.atZone(timeZone)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return zonedDateTime.format(formatter)
    }

    /**
     * 获取给定时间戳（以秒为单位）的日期是周几。
     *
     * @param timestamp 时间戳（以秒为单位）
     * @param locale 用于获取周几的文本表示的本地化信息
     * @return 周几的文本表示
     */
    fun getDayOfWeek(timestamp: Long, locale: Locale = Locale.getDefault()): String {
        // 将时间戳转换为 Instant
        val instant = Instant.ofEpochSecond(timestamp)

        // 将 Instant 转换为 LocalDate，指定时区
        val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()

        // 获取当前天是周几
        val dayOfWeek = localDate.dayOfWeek

        // 获取周几的文本表示
        return dayOfWeek.getDisplayName(TextStyle.FULL, locale)
    }

    /**
     * 获取给定日期字符串的日期是周几。
     *
     * @param dateString 日期字符串，格式为 "yyyy-MM-dd"
     * @param locale 用于获取周几的文本表示的本地化信息
     * @return 周几的文本表示
     */
    fun getDayOfWeek(dateString: String, locale: Locale = Locale.getDefault()): String {
        val localDate = LocalDate.parse(dateString)
        val dayOfWeek = localDate.dayOfWeek
        return dayOfWeek.getDisplayName(TextStyle.FULL, locale)
    }
}
