package com.yourcompany.android.studdy.alarm

import android.content.SharedPreferences
import com.yourcompany.android.studdy.currentTimeMillis
import com.yourcompany.android.studdy.plusOneDay
import com.yourcompany.android.studdy.setHourAndMinute
import java.util.Calendar

private const val EXACT_ALARM_PREFERENCES_KEY = "exact_alarm"
private const val INEXACT_ALARM_PREFERENCES_KEY = "inexact_alarm"

private const val INEXACT_WINDOW_ALARM_TRIGGER_PREFERENCES_KEY = "inexact_window_alarm_trigger"
private const val INEXACT_WINDOW_ALARM_WINDOW_PREFERENCES_KEY = "inexact_window_alarm_window"

private const val INEXACT_REPEATING_ALARM_TRIGGER_PREFERENCES_KEY =
    "inexact_repeating_alarm_trigger"
private const val INEXACT_REPEATING_ALARM_INTERVAL_PREFERENCES_KEY =
    "inexact_repeating_alarm_window"

private const val ALARM_NOT_SET = -1L

fun SharedPreferences.getExactAlarm(): ExactAlarm {
    val triggerAtMillis = getLong(EXACT_ALARM_PREFERENCES_KEY, ALARM_NOT_SET)
    return ExactAlarm(triggerAtMillis)
}

fun SharedPreferences.putExactAlarm(exactAlarm: ExactAlarm) {
    edit().putLong(EXACT_ALARM_PREFERENCES_KEY, exactAlarm.triggerAtMillis).apply()
}

fun SharedPreferences.clearExactAlarm() {
    edit().putLong(EXACT_ALARM_PREFERENCES_KEY, ALARM_NOT_SET).apply()
}

fun SharedPreferences.getInexactAlarm(): ExactAlarm {
    val triggerAtMillis = getLong(INEXACT_ALARM_PREFERENCES_KEY, ALARM_NOT_SET)
    return ExactAlarm(triggerAtMillis)
}

fun SharedPreferences.putInexactAlarm(inexactAlarm: ExactAlarm) {
    edit().putLong(INEXACT_ALARM_PREFERENCES_KEY, inexactAlarm.triggerAtMillis).apply()
}

fun SharedPreferences.clearInexactAlarm() {
    edit().putLong(INEXACT_ALARM_PREFERENCES_KEY, ALARM_NOT_SET).apply()
}

fun SharedPreferences.getWindowAlarm(): WindowAlarm {
    val triggerAtMillis = getLong(INEXACT_WINDOW_ALARM_TRIGGER_PREFERENCES_KEY, ALARM_NOT_SET)
    val windowMillis = getLong(INEXACT_WINDOW_ALARM_WINDOW_PREFERENCES_KEY, ALARM_NOT_SET)

    return WindowAlarm(triggerAtMillis, windowMillis)
}

fun SharedPreferences.putWindowAlarm(windowAlarm: WindowAlarm) {
    edit()
        .putLong(INEXACT_WINDOW_ALARM_TRIGGER_PREFERENCES_KEY, windowAlarm.triggerAtMillis)
        .putLong(INEXACT_WINDOW_ALARM_WINDOW_PREFERENCES_KEY, windowAlarm.windowLengthMillis)
        .apply()
}

fun SharedPreferences.clearWindowAlarm() {
    edit()
        .putLong(INEXACT_WINDOW_ALARM_TRIGGER_PREFERENCES_KEY, ALARM_NOT_SET)
        .putLong(INEXACT_WINDOW_ALARM_WINDOW_PREFERENCES_KEY, ALARM_NOT_SET)
        .apply()
}

fun SharedPreferences.getRepeatingAlarm(): RepeatingAlarm {
    val triggerAtMillis = getLong(INEXACT_REPEATING_ALARM_TRIGGER_PREFERENCES_KEY, ALARM_NOT_SET)
    val intervalMillis = getLong(INEXACT_REPEATING_ALARM_INTERVAL_PREFERENCES_KEY, ALARM_NOT_SET)

    return RepeatingAlarm(triggerAtMillis, intervalMillis)
}

fun SharedPreferences.putRepeatingAlarm(repeatingAlarm: RepeatingAlarm) {
    edit()
        .putLong(INEXACT_REPEATING_ALARM_TRIGGER_PREFERENCES_KEY, repeatingAlarm.triggerAtMillis)
        .putLong(INEXACT_REPEATING_ALARM_INTERVAL_PREFERENCES_KEY, repeatingAlarm.intervalMillis)
        .apply()
}

fun SharedPreferences.clearRepeatingAlarm() {
    edit()
        .putLong(INEXACT_REPEATING_ALARM_TRIGGER_PREFERENCES_KEY, ALARM_NOT_SET)
        .putLong(INEXACT_REPEATING_ALARM_INTERVAL_PREFERENCES_KEY, ALARM_NOT_SET)
        .apply()
}

fun convertToAlarmTimeMillis(hour: Int, minute: Int): Long {
    val calendar = Calendar.getInstance()
    val currentTimeMillis = calendar.timeInMillis
    val proposedTimeMillis = calendar.setHourAndMinute(hour, minute).timeInMillis

    val alarmTimeMillis: Long = if (proposedTimeMillis > currentTimeMillis) {
        proposedTimeMillis
    } else {
        proposedTimeMillis.plusOneDay()
    }

    return alarmTimeMillis
}

data class ExactAlarm(val triggerAtMillis: Long) {

    companion object {

        val NOT_SET = ExactAlarm(ALARM_NOT_SET)
    }

    fun isSet(): Boolean = triggerAtMillis != ALARM_NOT_SET

    fun isNotInPast(): Boolean = triggerAtMillis > currentTimeMillis()
}

data class WindowAlarm(val triggerAtMillis: Long, val windowLengthMillis: Long) {

    companion object {

        val NOT_SET = WindowAlarm(ALARM_NOT_SET, ALARM_NOT_SET)
    }

    fun isSet(): Boolean = triggerAtMillis != ALARM_NOT_SET && windowLengthMillis != ALARM_NOT_SET

    fun isNotInPast(): Boolean = triggerAtMillis > currentTimeMillis()
}

data class RepeatingAlarm(val triggerAtMillis: Long, val intervalMillis: Long) {

    companion object {

        val NOT_SET = RepeatingAlarm(ALARM_NOT_SET, ALARM_NOT_SET)
    }

    fun isSet(): Boolean = triggerAtMillis != ALARM_NOT_SET && intervalMillis != ALARM_NOT_SET
}