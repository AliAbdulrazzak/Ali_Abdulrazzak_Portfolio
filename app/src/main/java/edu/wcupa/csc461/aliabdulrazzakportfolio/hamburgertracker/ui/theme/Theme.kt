package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = BurgerAmber80,
    secondary = BurgerBrown80,
    tertiary = BurgerRed80,
    background = Color(0xFF1A120B),
    surface = Color(0xFF2C1F10),
    onPrimary = Color(0xFF3E2700),
    onSecondary = Color(0xFF2C1600),
    onBackground = Color(0xFFF5DEB3),
    onSurface = Color(0xFFF5DEB3)
)

private val LightColorScheme = lightColorScheme(
    primary = BurgerBrown40,
    secondary = BurgerAmber40,
    tertiary = BurgerRed40,
    background = BurgerLightBun,
    surface = Color(0xFFFFFBF5),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF3E2700),
    onSurface = Color(0xFF3E2700)
)

@Composable
fun HamburgertrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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