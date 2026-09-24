package com.example.testapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Primary = Coral, secondary = Green, tertiary = Pink, surfaces = Neutral.
// Error colors keep Material's defaults since the palette has no dedicated red.
private val DarkColorScheme = darkColorScheme(
    primary = Coral30,
    onPrimary = Coral90,
    primaryContainer = Coral80,
    onPrimaryContainer = Coral20,
    inversePrimary = Coral70,
    secondary = Green30,
    onSecondary = Green90,
    secondaryContainer = Green80,
    onSecondaryContainer = Green20,
    tertiary = Pink30,
    onTertiary = Pink90,
    tertiaryContainer = Pink80,
    onTertiaryContainer = Pink20,
    background = Neutral100,
    onBackground = Neutral20,
    surface = Neutral100,
    onSurface = Neutral20,
    surfaceVariant = Neutral80,
    onSurfaceVariant = Neutral30,
    surfaceTint = Coral30,
    inverseSurface = Neutral20,
    inverseOnSurface = Neutral90,
    outline = Neutral50,
    outlineVariant = Neutral80,
    scrim = Color.Black,
    surfaceDim = Neutral100,
    surfaceBright = Neutral80,
    surfaceContainerLowest = Neutral100,
    surfaceContainerLow = Neutral100,
    surfaceContainer = Neutral90,
    surfaceContainerHigh = Neutral90,
    surfaceContainerHighest = Neutral80
)

private val LightColorScheme = lightColorScheme(
    primary = Coral70,
    onPrimary = Color.White,
    primaryContainer = Coral20,
    onPrimaryContainer = Coral100,
    inversePrimary = Coral30,
    secondary = Green70,
    onSecondary = Color.White,
    secondaryContainer = Green20,
    onSecondaryContainer = Green100,
    tertiary = Pink70,
    onTertiary = Color.White,
    tertiaryContainer = Pink20,
    onTertiaryContainer = Pink100,
    background = BackgroundLight,
    onBackground = Neutral100,
    surface = SurfaceLight,
    onSurface = Neutral100,
    surfaceVariant = Neutral20,
    onSurfaceVariant = Neutral80,
    surfaceTint = Coral70,
    inverseSurface = Neutral90,
    inverseOnSurface = Neutral10,
    outline = Neutral60,
    outlineVariant = Neutral30,
    scrim = Color.Black,
    surfaceDim = Neutral20,
    surfaceBright = BackgroundLight,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = BackgroundLight,
    surfaceContainer = SurfaceLight,
    surfaceContainerHigh = Neutral10,
    surfaceContainerHighest = Neutral20
)

@Composable
fun TestAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}