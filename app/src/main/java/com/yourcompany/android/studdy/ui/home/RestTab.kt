@file:Suppress("FunctionName")

package com.yourcompany.android.studdy.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourcompany.android.studdy.TimeFormat
import com.yourcompany.android.studdy.alarm.ExactAlarm
import com.yourcompany.android.studdy.alarm.InexactAlarms
import com.yourcompany.android.studdy.alarm.RepeatingAlarm
import com.yourcompany.android.studdy.alarm.WindowAlarm
import com.yourcompany.android.studdy.alarm.convertToAlarmTimeMillis
import com.yourcompany.android.studdy.isNotZero
import com.yourcompany.android.studdy.isValidHour
import com.yourcompany.android.studdy.isValidMinute
import com.yourcompany.android.studdy.isValidWindowLength
import com.yourcompany.android.studdy.toHour24Format
import com.yourcompany.android.studdy.toMillis
import com.yourcompany.android.studdy.toUserFriendlyText
import com.yourcompany.android.studdy.ui.composables.AlarmInput
import com.yourcompany.android.studdy.ui.composables.AlarmSetClearButtons
import com.yourcompany.android.studdy.ui.composables.AlarmWithIntervalInput

@Composable
fun RestTab(inexactAlarms: InexactAlarms) {
    val inexactAlarm by remember { inexactAlarms.getInexactAlarmState() }
    val windowAlarm by remember { inexactAlarms.getWindowAlarmState() }
    val repeatingAlarm by remember { inexactAlarms.getRepeatingAlarmState() }
    val is24HourFormat = TimeFormat.is24HourFormat

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            InexactAlarmInput(inexactAlarms, inexactAlarm)
            Spacer(modifier = Modifier.height(16.dp))
            WindowAlarmInput(inexactAlarms, windowAlarm)
            Spacer(modifier = Modifier.height(16.dp))
            RepeatingAlarmInput(inexactAlarms, repeatingAlarm)

            Column(
                modifier = Modifier
                    .weight(1f, true)
                    .fillMaxWidth()
            ) {
                if (inexactAlarm.isSet()) {
                    Text(
                        text = "Inexact alarm set: ${
                            toUserFriendlyText(inexactAlarm.triggerAtMillis, is24HourFormat)
                        }",
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                    )
                }

                if (windowAlarm.isSet()) {
                    Text(
                        text = "Window alarm set: ${
                            toUserFriendlyText(
                                windowAlarm.triggerAtMillis,
                                windowAlarm.windowLengthMillis,
                                is24HourFormat
                            )
                        }",
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                    )
                }

                if (repeatingAlarm.isSet()) {
                    Text(
                        text = "Repeating alarm set: ${
                            toUserFriendlyText(
                                repeatingAlarm.triggerAtMillis,
                                repeatingAlarm.intervalMillis,
                                is24HourFormat
                            )
                        }",
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
private fun InexactAlarmInput(
    inexactAlarms: InexactAlarms,
    inexactAlarm: ExactAlarm
) {
    Text(
        text = "Set Rest Alarm",
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        fontSize = 18.sp
    )

    var hourInput by remember { mutableStateOf("") }
    var minuteInput by remember { mutableStateOf("") }
    var showInputInvalidMessage by remember { mutableStateOf(false) }
    var isAm by remember { mutableStateOf(true) }
    val is24HourFormat = TimeFormat.is24HourFormat

    Row(modifier = Modifier.padding(top = 16.dp)) {
        AlarmInput(
            hourInput = hourInput,
            minuteInput = minuteInput,
            onHourInputChanged = { hourInput = it },
            onMinuteInputChanged = { minuteInput = it },
            showInputInvalidMessage = showInputInvalidMessage,
            is24HourFormat = is24HourFormat,
            isAm = isAm,
            onIsAmEvent = { isAmValue -> isAm = isAmValue }
        )

        Spacer(Modifier.weight(1F, true))

        val focusManager = LocalFocusManager.current
        AlarmSetClearButtons(
            shouldShowClearButton = inexactAlarm.isSet(),
            onSetClicked = {
                if (hourInput.isValidHour(is24HourFormat) && minuteInput.isValidMinute()) {
                    showInputInvalidMessage = false

                    val hour: Int = if (is24HourFormat) {
                        hourInput.toInt()
                    } else {
                        hourInput.toInt().toHour24Format(isAm)
                    }
                    scheduleAlarm(inexactAlarms, hour, minuteInput.toInt())
                    focusManager.clearFocus()
                } else {
                    showInputInvalidMessage = true
                }
            },
            onClearClicked = { inexactAlarms.clearInexactAlarm() }
        )
    }
}

@Composable
private fun WindowAlarmInput(
    inexactAlarms: InexactAlarms,
    windowAlarm: WindowAlarm
) {
    Text(
        text = "Set Rest Window (at least 10min)",
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        fontSize = 18.sp
    )

    var hourInput by remember { mutableStateOf("") }
    var minuteInput by remember { mutableStateOf("") }
    var windowInput by remember { mutableStateOf("") }
    var showInputInvalidMessage by remember { mutableStateOf(false) }
    var isAm by remember { mutableStateOf(true) }
    val is24HourFormat = TimeFormat.is24HourFormat

    Row(modifier = Modifier.padding(top = 16.dp)) {
        AlarmWithIntervalInput(
            hourInput = hourInput,
            minuteInput = minuteInput,
            intervalInput = windowInput,
            onHourInputChanged = { hourInput = it },
            onMinuteInputChanged = { minuteInput = it },
            onIntervalInputChanged = { windowInput = it },
            showInputInvalidMessage = showInputInvalidMessage,
            is24HourFormat = is24HourFormat,
            isAm = isAm,
            onIsAmEvent = { isAmValue -> isAm = isAmValue }
        )

        Spacer(Modifier.weight(1F, true))

        val focusManager = LocalFocusManager.current

        AlarmSetClearButtons(
            shouldShowClearButton = windowAlarm.isSet(),
            onSetClicked = {
                if (hourInput.isValidHour(is24HourFormat)
                    && minuteInput.isValidMinute()
                    && windowInput.isValidWindowLength()
                ) {
                    showInputInvalidMessage = false

                    val hour: Int = if (is24HourFormat) {
                        hourInput.toInt()
                    } else {
                        hourInput.toInt().toHour24Format(isAm)
                    }
                    scheduleWindowAlarm(
                        inexactAlarms,
                        hour,
                        minuteInput.toInt(),
                        windowInput.toInt()
                    )
                    focusManager.clearFocus()
                } else {
                    showInputInvalidMessage = true
                }
            },
            onClearClicked = { inexactAlarms.clearWindowAlarm() }
        )
    }
}

@Composable
private fun RepeatingAlarmInput(
    inexactAlarms: InexactAlarms,
    repeatingAlarm: RepeatingAlarm
) {
    Text(
        text = "Set Repeating Rest Alarm",
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        fontSize = 18.sp
    )

    var hourInput by remember { mutableStateOf("") }
    var minuteInput by remember { mutableStateOf("") }
    var intervalInput by remember { mutableStateOf("") }
    var showInputInvalidMessage by remember { mutableStateOf(false) }
    var isAm by remember { mutableStateOf(true) }
    val is24HourFormat = TimeFormat.is24HourFormat

    Row(modifier = Modifier.padding(top = 16.dp)) {
        AlarmWithIntervalInput(
            hourInput = hourInput,
            minuteInput = minuteInput,
            intervalInput = intervalInput,
            onHourInputChanged = { hourInput = it },
            onMinuteInputChanged = { minuteInput = it },
            onIntervalInputChanged = { intervalInput = it },
            showInputInvalidMessage = showInputInvalidMessage,
            is24HourFormat = is24HourFormat,
            isAm = isAm,
            onIsAmEvent = { isAmValue -> isAm = isAmValue }
        )

        Spacer(Modifier.weight(1F, true))

        val focusManager = LocalFocusManager.current

        AlarmSetClearButtons(
            shouldShowClearButton = repeatingAlarm.isSet(),
            onSetClicked = {
                if (hourInput.isValidHour(is24HourFormat)
                    && minuteInput.isValidMinute()
                    && intervalInput.isNotZero()
                ) {
                    showInputInvalidMessage = false

                    val hour: Int = if (is24HourFormat) {
                        hourInput.toInt()
                    } else {
                        hourInput.toInt().toHour24Format(isAm)
                    }
                    scheduleRepeatingAlarm(
                        inexactAlarms,
                        hour,
                        minuteInput.toInt(),
                        intervalInput.toInt()
                    )
                    focusManager.clearFocus()
                } else {
                    showInputInvalidMessage = true
                }
            },
            onClearClicked = { inexactAlarms.clearRepeatingAlarm() }
        )
    }
}

private fun scheduleAlarm(inexactAlarms: InexactAlarms, hour: Int, minute: Int) {
    val alarmTimeMillis: Long = convertToAlarmTimeMillis(hour, minute)
    inexactAlarms.scheduleInexactAlarm(ExactAlarm(alarmTimeMillis))
}

private fun scheduleWindowAlarm(
    inexactAlarms: InexactAlarms,
    hour: Int,
    minute: Int,
    minuteWindowLength: Int
) {
    val alarmTimeMillis: Long = convertToAlarmTimeMillis(hour, minute)
    val windowLengthMillis: Long = minuteWindowLength.toMillis()
    inexactAlarms.scheduleWindowAlarm(WindowAlarm(alarmTimeMillis, windowLengthMillis))
}

private fun scheduleRepeatingAlarm(
    inexactAlarms: InexactAlarms,
    hour: Int,
    minute: Int,
    minuteInterval: Int
) {
    val alarmTimeMillis: Long = convertToAlarmTimeMillis(hour, minute)
    val intervalMillis: Long = minuteInterval.toMillis()
    inexactAlarms.scheduleRepeatingAlarm(RepeatingAlarm(alarmTimeMillis, intervalMillis))
}
