package com.yourcompany.android.studdy.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.yourcompany.android.studdy.StuddyApplication

private const val NOTIFICATION_ID = 1002
private const val NOTIFICATION_CHANNEL_ID = "rest_alarm"
private const val NOTIFICATION_CHANNEL_NAME = "Rest Alarms"

class InexactAlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(
            "InexactAlarmBroadcastReceiver",
            "Alarm with request code ${intent.getIntExtra(ALARM_REQUEST_CODE_EXTRA, -1)} triggered"
        )
        showNotification(
            context,
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NOTIFICATION_ID,
            "Don't forget to stretch and rest a bit! :]"
        )
        (context.applicationContext as StuddyApplication).apply {
            when (intent.getIntExtra(ALARM_REQUEST_CODE_EXTRA, -1)) {
                INEXACT_ALARM_REQUEST_CODE -> inexactAlarms.clearInexactAlarm()
                INEXACT_ALARM_WINDOW_REQUEST_CODE -> inexactAlarms.clearWindowAlarm()
                INEXACT_REPEATING_ALARM_REQUEST_CODE -> {
                    // Do nothing
                }
            }
            alarmRingtoneState.value = playRingtone(context)
        }
    }
}
