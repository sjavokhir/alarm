package com.yourcompany.android.studdy.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yourcompany.android.studdy.StuddyApplication

class RescheduleAlarmsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action != null) {
            if (action == Intent.ACTION_BOOT_COMPLETED) {
                (context.applicationContext as StuddyApplication).exactAlarms.rescheduleAlarm()
                (context.applicationContext as StuddyApplication).inexactAlarms.rescheduleAlarms()
            }
        }
    }
}
