package tmidev.localaccount.ui.component.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import tmidev.localaccount.R
import tmidev.localaccount.util.isCompatibleWithDynamicColors

/**
 * Local Account default theme.
 *
 * @param useDarkTheme when the theme should have dark colors. Default is [isSystemInDarkTheme].
 * @param useDynamicColors when the color scheme should use colors based on device wallpaper,
 * this will only be applied on API 31 and up. Default is true.
 * @param content the content for this theme.
 */
@Composable
fun LaTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicColors: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        isCompatibleWithDynamicColors() && useDynamicColors -> {
            val context = LocalContext.current
            if (useDarkTheme) dynamicDarkColorScheme(context = context)
            else dynamicLightColorScheme(context = context)
        }

        useDarkTheme -> darkColorScheme()

        else -> lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = LaTypography,
        shapes = LaShapes,
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = colorScheme.background,
                contentColor = colorScheme.onBackground,
                content = content
            )
        }
    )
}

/**
 * Local Account theme for splash. This composable uses the [LaTheme] to display
 * an presentation while some necessary information is loading.
 */
@Composable
fun LaThemeSplash() = LaTheme {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.appName),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(modifier = Modifier.height(height = 8.dp))

        Text(
            text = stringResource(id = R.string.appDescription),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
    }
}