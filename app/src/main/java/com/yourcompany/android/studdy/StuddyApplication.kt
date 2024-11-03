package com.yourcompany.android.studdy

import android.app.Application
import android.media.Ringtone
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.yourcompany.android.studdy.alarm.ExactAlarms
import com.yourcompany.android.studdy.alarm.InexactAlarms

const val SHARED_PREFS = "alarms"

/**
 * Application class responsible for dependency injection and holding app wide state.
 */
class StuddyApplication : Application() {

    val alarmRingtoneState: MutableState<Ringtone?> = mutableStateOf(null)

    lateinit var exactAlarms: ExactAlarms
    lateinit var inexactAlarms: InexactAlarms

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        exactAlarms = ExactAlarms(this, sharedPreferences)
        inexactAlarms = InexactAlarms(this, sharedPreferences)
    }
}