package com.yourcompany.android.studdy.alarm

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri

fun playRingtone(context: Context): Ringtone {
    var alarmUri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    if (alarmUri == null) {
        alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    }
    val ringtone = RingtoneManager.getRingtone(context.applicationContext, alarmUri)
    ringtone.play()
    return ringtone
}