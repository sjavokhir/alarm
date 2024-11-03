package com.yourcompany.android.studdy.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = Color(0xFF006837),
    primaryVariant = Color(0xFF004012),
    secondary = Color(0xFFc75f00),
    onPrimary = Color(0xFFffffff)
)

@Composable
fun StuddyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
