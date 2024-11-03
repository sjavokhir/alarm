package com.yourcompany.android.studdy

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.format.DateFormat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.yourcompany.android.R
import com.yourcompany.android.studdy.ui.home.HomeScreen
import com.yourcompany.android.studdy.ui.theme.StuddyTheme

/**
 * Main activity that holds the UI and servers as a starting activity.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Studdy)
        super.onCreate(savedInstanceState)
        val exactAlarms = (application as StuddyApplication).exactAlarms.apply {
            rescheduleAlarm()
        }
        val inexactAlarms = (application as StuddyApplication).inexactAlarms.apply {
            rescheduleAlarms()
        }
        setContent {
            StuddyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val alarmRingtoneState = (application as StuddyApplication).alarmRingtoneState

                    HomeScreen(
                        exactAlarms = exactAlarms,
                        inexactAlarms = inexactAlarms,
                        onSchedulingAlarmNotAllowed = { openSettings() },
                        showStopAlarmButton = alarmRingtoneState.value != null,
                        onStopAlarmClicked = {
                            alarmRingtoneState.value?.stop()
                            alarmRingtoneState.value = null
                        }
                    )
                }
            }
        }
    }

    private fun openSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent().apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
            }
            applicationContext.startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        TimeFormat.is24HourFormat = DateFormat.is24HourFormat(this)
    }
}
