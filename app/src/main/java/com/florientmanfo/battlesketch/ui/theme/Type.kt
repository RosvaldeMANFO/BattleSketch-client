package com.florientmanfo.battlesketch.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.florientmanfo.battlesketch.R

private val baseline = Typography()

@Composable
fun getTypography(): Typography {
    val bodyFontFamily = FontFamily(
        Font(R.font.arima_regular)
    )

    val headlineFontFamily = FontFamily(
        Font(R.font.montserrat_regular)
    )

    return Typography(
        headlineLarge = baseline.headlineLarge.copy(fontFamily = headlineFontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = headlineFontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = headlineFontFamily),
        displayLarge = baseline.displayLarge.copy(fontFamily = bodyFontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = bodyFontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = bodyFontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = bodyFontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = bodyFontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = bodyFontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
    )
}