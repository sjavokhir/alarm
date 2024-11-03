package com.yourcompany.android.studdy.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yourcompany.android.studdy.StuddyApplication

private const val NOTIFICATION_ID = 1001
private const val NOTIFICATION_CHANNEL_ID = "study_alarm"
private const val NOTIFICATION_CHANNEL_NAME = "Study Alarms"

class ExactAlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        showNotification(
            context,
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NOTIFICATION_ID,
            "Time to study!"
        )

        (context.applicationContext as StuddyApplication).apply {
            exactAlarms.clearExactAlarm()
            alarmRingtoneState.value = playRingtone(context)
        }
    }
}
