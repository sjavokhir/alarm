package com.yourcompany.android.studdy.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

const val INEXACT_ALARM_REQUEST_CODE = 1002
const val INEXACT_ALARM_WINDOW_REQUEST_CODE = 1003
const val INEXACT_REPEATING_ALARM_REQUEST_CODE = 1004

const val ALARM_REQUEST_CODE_EXTRA = "alarm_request_code_extra"

class InexactAlarms(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private var inexactAlarmState = mutableStateOf(ExactAlarm.NOT_SET)
    private var windowAlarmState = mutableStateOf(WindowAlarm.NOT_SET)
    private var repeatingAlarmState = mutableStateOf(RepeatingAlarm.NOT_SET)

    fun getInexactAlarmState(): State<ExactAlarm> = inexactAlarmState

    fun getWindowAlarmState(): State<WindowAlarm> = windowAlarmState

    fun getRepeatingAlarmState(): State<RepeatingAlarm> = repeatingAlarmState

    fun rescheduleAlarms() {
        val inexactAlarm = sharedPreferences.getInexactAlarm()
        if (inexactAlarm.isSet() && inexactAlarm.isNotInPast()) {
            scheduleInexactAlarm(inexactAlarm)
        } else {
            clearInexactAlarm()
        }

        val windowAlarm = sharedPreferences.getWindowAlarm()
        if (windowAlarm.isSet() && windowAlarm.isNotInPast()) {
            scheduleWindowAlarm(windowAlarm)
        } else {
            clearWindowAlarm()
        }

        val repeatingAlarm = sharedPreferences.getRepeatingAlarm()
        if (repeatingAlarm.isSet()) {
            scheduleRepeatingAlarm(repeatingAlarm)
        } else {
            clearRepeatingAlarm()
        }
    }

    fun scheduleInexactAlarm(inexactAlarm: ExactAlarm) {
        val pendingIntent = createInexactAlarmIntent(INEXACT_ALARM_REQUEST_CODE)
        alarmManager.set(AlarmManager.RTC_WAKEUP, inexactAlarm.triggerAtMillis, pendingIntent)

        sharedPreferences.putInexactAlarm(inexactAlarm)
        inexactAlarmState.value = inexactAlarm
    }

    fun clearInexactAlarm() {
        val pendingIntent = createInexactAlarmIntent(INEXACT_ALARM_REQUEST_CODE)
        alarmManager.cancel(pendingIntent)

        sharedPreferences.clearInexactAlarm()
        inexactAlarmState.value = ExactAlarm.NOT_SET
    }

    fun scheduleWindowAlarm(windowAlarm: WindowAlarm) {
        val pendingIntent = createInexactAlarmIntent(INEXACT_ALARM_WINDOW_REQUEST_CODE)
        alarmManager.setWindow(
            AlarmManager.RTC_WAKEUP,
            windowAlarm.triggerAtMillis,
            windowAlarm.windowLengthMillis,
            pendingIntent
        )

        sharedPreferences.putWindowAlarm(windowAlarm)
        windowAlarmState.value = windowAlarm
    }

    fun clearWindowAlarm() {
        val pendingIntent = createInexactAlarmIntent(INEXACT_ALARM_WINDOW_REQUEST_CODE)
        alarmManager.cancel(pendingIntent)

        sharedPreferences.clearWindowAlarm()
        windowAlarmState.value = WindowAlarm.NOT_SET
    }

    fun scheduleRepeatingAlarm(repeatingAlarm: RepeatingAlarm) {
        val pendingIntent = createInexactAlarmIntent(INEXACT_REPEATING_ALARM_REQUEST_CODE)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            repeatingAlarm.triggerAtMillis,
            repeatingAlarm.intervalMillis,
            pendingIntent
        )

        sharedPreferences.putRepeatingAlarm(repeatingAlarm)
        repeatingAlarmState.value = repeatingAlarm
    }

    fun clearRepeatingAlarm() {
        val pendingIntent = createInexactAlarmIntent(INEXACT_REPEATING_ALARM_REQUEST_CODE)
        alarmManager.cancel(pendingIntent)

        sharedPreferences.clearRepeatingAlarm()
        repeatingAlarmState.value = RepeatingAlarm.NOT_SET
    }

    private fun createInexactAlarmIntent(alarmRequestCode: Int): PendingIntent {
        val intent = Intent(context, InexactAlarmBroadcastReceiver::class.java).apply {
            putExtra(ALARM_REQUEST_CODE_EXTRA, alarmRequestCode)
        }
        return PendingIntent.getBroadcast(
            context,
            alarmRequestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

}
