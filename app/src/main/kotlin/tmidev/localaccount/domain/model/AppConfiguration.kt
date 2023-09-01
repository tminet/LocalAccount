package tmidev.localaccount.domain.model

import tmidev.localaccount.domain.model.type.ThemeStyle

/**
 * Model class for the app's preferences.
 */
data class AppConfiguration(
    val useDynamicColors: Boolean,
    val themeStyle: ThemeStyle
)