package tmidev.localaccount.domain.model

import tmidev.localaccount.domain.model.type.ThemeStyle

data class AppConfiguration(
    val useDynamicColors: Boolean,
    val themeStyle: ThemeStyle
)