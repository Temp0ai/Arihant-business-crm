package com.teacoffeecrm.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = TeaBrown,
    onPrimary = CreamWhite,
    primaryContainer = CreamWhite,
    onPrimaryContainer = TeaBrownDark,
    secondary = GoldAccent,
    onSecondary = TeaBrownDark,
    secondaryContainer = CreamWhite,
    onSecondaryContainer = CoffeeBlack,
    background = SurfaceLight,
    onBackground = CoffeeBlack,
    surface = SurfaceLight,
    onSurface = CoffeeBlack,
    error = ErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = TeaBrownLight,
    onPrimary = CoffeeBlack,
    primaryContainer = TeaBrownDark,
    onPrimaryContainer = CreamWhite,
    secondary = GoldAccent,
    onSecondary = CoffeeBlack,
    background = SurfaceDark,
    onBackground = CreamWhite,
    surface = SurfaceDark,
    onSurface = CreamWhite,
    error = ErrorRed
)

@Composable
fun TeaCoffeeCRMTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
