package tmidev.localaccount.ui.component

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

/**
 * Compose a [Button].
 *
 * @param modifier the [Modifier] to be applied on this Button.
 * @param text string to be displayed in this Button.
 * @param onClick called when this button is clicked.
 * @param enabled controls the enabled state of this Button. Default value is true.
 */
@Composable
fun LaButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) = Button(
    modifier = modifier,
    onClick = onClick,
    enabled = enabled
) {
    Text(text = text)
}

/**
 * Compose a [TextButton].
 *
 * @param modifier the [Modifier] to be applied on this Button.
 * @param text string to be displayed in this Button.
 * @param onClick called when this button is clicked.
 * @param enabled controls the enabled state of this Button. Default value is true.
 */
@Composable
fun LaTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) = TextButton(
    modifier = modifier,
    onClick = onClick,
    enabled = enabled
) {
    Text(text = text, fontWeight = FontWeight.Bold)
}