package app.taplink.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TapLinkColors = darkColorScheme(
    primary = Color(0xFF83A7FF),
    onPrimary = Color(0xFF10131A),
    background = Color(0xFF10131A),
    surface = Color(0xFF1A1F2A),
    onSurface = Color(0xFFF5F7FC),
    secondary = Color(0xFF9DB8F5)
)

@Composable
fun TapLinkTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TapLinkColors,
        content = content
    )
}
