package eu.caraus.kmp.samplearch

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val typography =
        Typography(
            bodyMedium =
                TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                ),
        )
    val shapes =
        Shapes(
            small = RoundedCornerShape(4.dp),
            medium = RoundedCornerShape(4.dp),
            large = RoundedCornerShape(0.dp),
        )

    MaterialTheme(
        colorScheme = colorScheme(darkTheme),
        typography = typography,
        shapes = shapes,
        content = content,
    )
}

fun colorScheme(darkTheme: Boolean): ColorScheme {
    val colors: Colors =
        if (darkTheme) {
            DarkColors
        } else {
            LightColors
        }
    return if (darkTheme) {
        darkColorScheme(
            primary = colors.primary,
            secondary = colors.secondary,
            tertiary = colors.tertiary,
        )
    } else {
        lightColorScheme(
            primary = colors.primary,
            secondary = colors.secondary,
            tertiary = colors.tertiary,
        )
    }
}

interface Colors {
    val primary: Color
    val secondary: Color
    val tertiary: Color
}

@Suppress("MagicNumber")
object DarkColors : Colors {
    override val primary: Color = Color(0xFFBB86FC)
    override val secondary: Color = Color(0xFF03DAC5)
    override val tertiary: Color = Color(0xFF3700B3)
}

@Suppress("MagicNumber")
object LightColors : Colors {
    override val primary = Color(0xFF6200EE)
    override val secondary = Color(0xFF03DAC5)
    override val tertiary = Color(0xFF3700B3)
}
