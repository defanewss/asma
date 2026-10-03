package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EmeraldLightScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = EmeraldOnPrimary,
    primaryContainer = EmeraldPrimaryContainer,
    secondary = EmeraldSecondary,
    background = EmeraldBackground,
    surface = EmeraldSurface
)

private val TurquoiseLightScheme = lightColorScheme(
    primary = TurquoisePrimary,
    onPrimary = TurquoiseOnPrimary,
    primaryContainer = TurquoisePrimaryContainer,
    secondary = TurquoiseSecondary,
    background = TurquoiseBackground,
    surface = TurquoiseSurface
)

private val HaramLightScheme = lightColorScheme(
    primary = HaramPrimary,
    onPrimary = HaramOnPrimary,
    primaryContainer = HaramPrimaryContainer,
    secondary = HaramSecondary,
    background = HaramBackground,
    surface = HaramSurface
)

private val LightScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    secondary = LightSecondary,
    background = LightBackground,
    surface = LightSurface
)

private val NightDarkScheme = darkColorScheme(
    primary = NightSecondary,
    onPrimary = Color.Black,
    primaryContainer = NightPrimary,
    secondary = NightPrimary,
    background = NightBackground,
    surface = NightSurface
)

@Composable
fun ZekrSalawatTheme(
    themeName: String = "emerald",
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> NightDarkScheme
        else -> when (themeName.lowercase()) {
            "turquoise" -> TurquoiseLightScheme
            "haram" -> HaramLightScheme
            "light" -> LightScheme
            else -> EmeraldLightScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
