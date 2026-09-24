package com.example.testapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

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

private val HighContrastLightColorScheme = LightColorScheme.copy(
    primary = Coral90,
    secondary = Green90,
    tertiary = Pink90,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Neutral10,
    onSurfaceVariant = Neutral100,
    outline = Neutral100,
    outlineVariant = Neutral80,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color.White,
    surfaceContainer = Color.White,
    surfaceContainerHigh = Neutral10,
    surfaceContainerHighest = Neutral10
)

private val HighContrastDarkColorScheme = DarkColorScheme.copy(
    primary = Coral20,
    secondary = Green20,
    tertiary = Pink20,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    surfaceVariant = Neutral100,
    onSurfaceVariant = Neutral10,
    outline = Neutral10,
    outlineVariant = Neutral30,
    surfaceContainerLowest = Color.Black,
    surfaceContainerLow = Color.Black,
    surfaceContainer = Color.Black,
    surfaceContainerHigh = Neutral100,
    surfaceContainerHighest = Neutral100
)

private const val LARGE_TEXT_SCALE = 1.25f

@Composable
fun TestAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    highContrast: Boolean = false,
    largeText: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        highContrast && darkTheme -> HighContrastDarkColorScheme
        highContrast -> HighContrastLightColorScheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val density = LocalDensity.current
    val fontScale = if (largeText) density.fontScale * LARGE_TEXT_SCALE else density.fontScale

    CompositionLocalProvider(LocalDensity provides Density(density.density, fontScale)) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
