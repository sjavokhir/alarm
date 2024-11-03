package com.yourcompany.android.studdy.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AlarmInput(
    hourInput: String,
    minuteInput: String,
    onHourInputChanged: (String) -> Unit,
    onMinuteInputChanged: (String) -> Unit,
    showInputInvalidMessage: Boolean,
    is24HourFormat: Boolean = false,
    isAm: Boolean = true,
    onIsAmEvent: (Boolean) -> Unit = {}
) {
    Column {
        Row {
            if (is24HourFormat.not()) {
                AmPmSelector(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    isAm = isAm,
                    onIsAmEvent = onIsAmEvent
                )
            }
            TimeInput(
                inputState = hourInput,
                onInputChanged = { onHourInputChanged.invoke(it) },
                placeHolderValue = "12"
            )
            Text(modifier = Modifier.align(alignment = Alignment.CenterVertically), text = ":")
            TimeInput(
                inputState = minuteInput,
                onInputChanged = { onMinuteInputChanged.invoke(it) },
                placeHolderValue = "00"
            )
        }
        if (showInputInvalidMessage) {
            Text(text = "Enter a valid time")
        } else {
            Text(text = "")
        }
    }
}

@Composable
fun AlarmWithIntervalInput(
    hourInput: String,
    minuteInput: String,
    intervalInput: String,
    onHourInputChanged: (String) -> Unit,
    onMinuteInputChanged: (String) -> Unit,
    onIntervalInputChanged: (String) -> Unit,
    showInputInvalidMessage: Boolean,
    is24HourFormat: Boolean = false,
    isAm: Boolean = true,
    onIsAmEvent: (Boolean) -> Unit = {}
) {
    Column {
        Row {
            if (is24HourFormat.not()) {
                AmPmSelector(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    isAm = isAm,
                    onIsAmEvent = onIsAmEvent
                )
            }
            TimeInput(
                inputState = hourInput,
                onInputChanged = { onHourInputChanged.invoke(it) },
                placeHolderValue = "12"
            )
            Text(modifier = Modifier.align(alignment = Alignment.CenterVertically), text = ":")
            TimeInput(
                inputState = minuteInput,
                onInputChanged = { onMinuteInputChanged.invoke(it) },
                placeHolderValue = "00"
            )
            Text(modifier = Modifier.align(alignment = Alignment.CenterVertically), text = "+")
            TimeInput(
                inputState = intervalInput,
                onInputChanged = { onIntervalInputChanged.invoke(it) },
                placeHolderValue = "10"
            )
        }
        if (showInputInvalidMessage) {
            Text(text = "Enter a valid time")
        } else {
            Text(text = "")
        }
    }
}

@Composable
fun TimeInput(
    inputState: String,
    onInputChanged: (String) -> Unit,
    placeHolderValue: String
) {
    TextField(
        modifier = Modifier.width(52.dp),
        value = inputState,
        onValueChange = {
            if (it.isEmpty() || (it.length <= 2 && it.matches("\\d+(\\.\\d+)?".toRegex()))) {
                onInputChanged.invoke(it)
            }
        },
        placeholder = { Text(text = placeHolderValue) },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.background
        )
    )
}

@Composable
fun AlarmSetClearButtons(
    shouldShowClearButton: Boolean,
    onSetClicked: () -> Unit,
    onClearClicked: () -> Unit
) {
    Row {
        Button(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = { onSetClicked.invoke() },
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("Set")
        }

        if (shouldShowClearButton) {
            Spacer(modifier = Modifier.width(2.dp))
            Button(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { onClearClicked.invoke() },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Clear")
            }
        }
    }
}

@Composable
fun AmPmSelector(
    modifier: Modifier = Modifier,
    isAm: Boolean,
    onIsAmEvent: (Boolean) -> Unit
) {
    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .width(40.dp)
                .height(40.dp)
                .clickable { onIsAmEvent.invoke(isAm.not()) }
        ) {
            Text(
                text = if (isAm) "AM" else "PM",
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
            )
        }
    }
}