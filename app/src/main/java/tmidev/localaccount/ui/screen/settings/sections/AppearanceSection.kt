package tmidev.localaccount.ui.screen.settings.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tmidev.localaccount.R
import tmidev.localaccount.domain.model.type.ThemeStyle
import tmidev.localaccount.util.LaIcons
import tmidev.localaccount.util.isCompatibleWithDynamicColors

/**
 * An composable to display the settings of appearance.
 *
 * @param modifier the [Modifier] to apply on the container of this composable.
 * @param useDynamicColors boolean value when the dynamic color is activated or not.
 * @param toggleDynamicColors callback to toggle the [useDynamicColors].
 * @param themeStyle the current [ThemeStyle] applied.
 * @param changeThemeStyle callback to change the [themeStyle].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceSection(
    modifier: Modifier = Modifier,
    useDynamicColors: Boolean,
    toggleDynamicColors: () -> Unit,
    themeStyle: ThemeStyle,
    changeThemeStyle: (ThemeStyle) -> Unit
) = Column(modifier = modifier) {
    Text(
        text = stringResource(id = R.string.appearance),
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.height(height = 8.dp))

    if (isCompatibleWithDynamicColors()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.useDynamicColors),
                style = MaterialTheme.typography.bodyLarge
            )

            Switch(
                checked = useDynamicColors,
                onCheckedChange = { toggleDynamicColors() }
            )
        }

        Spacer(modifier = Modifier.height(height = 8.dp))
    }

    Text(
        text = stringResource(id = R.string.themeStyle),
        style = MaterialTheme.typography.bodyLarge
    )

    InputChip(
        selected = themeStyle == ThemeStyle.FollowAndroidSystem,
        onClick = {
            if (themeStyle != ThemeStyle.FollowAndroidSystem)
                changeThemeStyle(ThemeStyle.FollowAndroidSystem)
        },
        label = { Text(text = stringResource(id = R.string.followAndroidSystem)) },
        leadingIcon = {
            Icon(
                modifier = Modifier.size(size = AssistChipDefaults.IconSize),
                imageVector = LaIcons.Android,
                contentDescription = null
            )
        }
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        InputChip(
            selected = themeStyle == ThemeStyle.LightMode,
            onClick = {
                if (themeStyle != ThemeStyle.LightMode)
                    changeThemeStyle(ThemeStyle.LightMode)
            },
            label = { Text(text = stringResource(id = R.string.lightMode)) },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(size = AssistChipDefaults.IconSize),
                    imageVector = LaIcons.LightMode,
                    contentDescription = null
                )
            }
        )

        InputChip(
            selected = themeStyle == ThemeStyle.DarkMode,
            onClick = {
                if (themeStyle != ThemeStyle.DarkMode)
                    changeThemeStyle(ThemeStyle.DarkMode)
            },
            label = { Text(text = stringResource(id = R.string.darkMode)) },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(size = AssistChipDefaults.IconSize),
                    imageVector = LaIcons.DarkMode,
                    contentDescription = null
                )
            }
        )
    }
}