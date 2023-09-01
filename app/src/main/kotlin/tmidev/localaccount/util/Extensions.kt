package tmidev.localaccount.util

import android.graphics.Color
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import tmidev.localaccount.R
import tmidev.localaccount.domain.model.type.ThemeStyle
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Check if this device is compatible with Dynamic Colors for Material 3.
 *
 * @return true when this device is API 31 (Android 12) or up, false otherwise.
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun isCompatibleWithDynamicColors(): Boolean =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

/**
 * Map a ThemeStyle into a [Boolean].
 *
 * @param themeStyle the [ThemeStyle].
 *
 * @return the corresponding boolean value of this ThemeStyle.
 */
@Composable
fun shouldUseDarkTheme(
    themeStyle: ThemeStyle
): Boolean = when (themeStyle) {
    ThemeStyle.FollowAndroidSystem -> isSystemInDarkTheme()
    ThemeStyle.LightMode -> false
    ThemeStyle.DarkMode -> true
}

/**
 * Extension that get a string from [stringResource] for a provided [id].
 *
 * @param id the int value that represents the resource id.
 *
 * @return the string from that [id] or null if the [id] is null.
 */
@Composable
fun safeStringResource(@StringRes id: Int?): String? =
    if (id == null) null
    else stringResource(id = id)

/**
 * Compose an [IconButton] with [LaIcons.Clear].
 *
 * @param onClick called when this icon button is clicked.
 */
@Composable
fun ClearTrailingButton(
    onClick: () -> Unit
) = IconButton(onClick = onClick) {
    Icon(
        imageVector = LaIcons.Clear,
        contentDescription = stringResource(id = R.string.clear)
    )
}

/**
 * Compose an [IconButton] for text visibility.
 * Uses [LaIcons.Visibility] and [LaIcons.VisibilityOff].
 *
 * @param onClick called when this icon button is clicked.
 * @param isVisible boolean to determine witch icon should be displayed.
 */
@Composable
fun ToggleTextVisibilityTrailingButton(
    onClick: () -> Unit,
    isVisible: Boolean
) = IconButton(onClick = onClick) {
    Icon(
        imageVector = if (isVisible) LaIcons.Visibility else LaIcons.VisibilityOff,
        contentDescription = stringResource(
            id = if (isVisible) R.string.show else R.string.hide
        )
    )
}

/**
 * Creates a MD5 hashed string.
 *
 * @return string with MD5 hash.
 */
fun String.md5(): String {
    val byteArray = this.toByteArray()
    val messageDigest = MessageDigest.getInstance("MD5")
    val bigInteger = BigInteger(1, messageDigest.digest(byteArray))
    return bigInteger.toString(16).padStart(32, '0')
}

/**
 * Custom [enableEdgeToEdge] with transparent bars.
 *
 * @param darkMode when dark colors is used or not.
 */
fun ComponentActivity.transparentEdge(darkMode: Boolean) {
    fun systemBarStyle(
        darkMode: Boolean
    ): SystemBarStyle = if (darkMode) SystemBarStyle.dark(
        scrim = Color.TRANSPARENT
    ) else SystemBarStyle.light(
        scrim = Color.TRANSPARENT,
        darkScrim = Color.TRANSPARENT
    )

    enableEdgeToEdge(
        statusBarStyle = systemBarStyle(darkMode = darkMode),
        navigationBarStyle = systemBarStyle(darkMode = darkMode)
    )
}
