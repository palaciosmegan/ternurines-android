package com.example.testapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.testapp.R

@OptIn(ExperimentalTextApi::class)
private fun quicksand(weight: FontWeight) = Font(
    resId = R.font.quicksand,
    weight = weight,
    variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight))
)

val Quicksand = FontFamily(
    quicksand(FontWeight.Light),
    quicksand(FontWeight.Normal),
    quicksand(FontWeight.Medium),
    quicksand(FontWeight.SemiBold),
    quicksand(FontWeight.Bold)
)

private val BaseTypography = Typography()

val Typography = Typography(
    displayLarge = BaseTypography.displayLarge.copy(fontFamily = Quicksand),
    displayMedium = BaseTypography.displayMedium.copy(fontFamily = Quicksand),
    displaySmall = BaseTypography.displaySmall.copy(fontFamily = Quicksand),
    headlineLarge = BaseTypography.headlineLarge.copy(fontFamily = Quicksand),
    headlineMedium = BaseTypography.headlineMedium.copy(fontFamily = Quicksand),
    headlineSmall = BaseTypography.headlineSmall.copy(fontFamily = Quicksand),
    titleLarge = BaseTypography.titleLarge.copy(fontFamily = Quicksand),
    titleMedium = BaseTypography.titleMedium.copy(fontFamily = Quicksand),
    titleSmall = BaseTypography.titleSmall.copy(fontFamily = Quicksand),
    bodyLarge = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = BaseTypography.bodyMedium.copy(fontFamily = Quicksand),
    bodySmall = BaseTypography.bodySmall.copy(fontFamily = Quicksand),
    labelLarge = BaseTypography.labelLarge.copy(fontFamily = Quicksand),
    labelMedium = BaseTypography.labelMedium.copy(fontFamily = Quicksand),
    labelSmall = BaseTypography.labelSmall.copy(fontFamily = Quicksand)
)
