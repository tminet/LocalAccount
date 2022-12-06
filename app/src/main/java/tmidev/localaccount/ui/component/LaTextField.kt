package tmidev.localaccount.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Compose [OutlinedTextField] with custom information.
 *
 * @param modifier the [Modifier] to be applied to this container.
 * @param value string to be shown in the text field.
 * @param onValueChange callback that is triggered when value changes.
 * @param labelString string that is displayed as label.
 * @param placeholderString optional string that is displayed as placeholder.
 * @param description optional string that is displayed on top of the text field.
 * @param errorMessage string that indicates an error, it's displayed bellow of the text field.
 * @param enableVisualError if enabled will paint this text field as error when a
 * [errorMessage] is not null. Default is true.
 * @param leadingIcon optional [IconButton] to be displayed at the
 * beginning of the text field.
 * @param trailingIcon optional [IconButton] to be displayed at the end of the text field.
 * @param enable controls the enabled state of this text field. Default is true.
 * @param readOnly controls when the text should be modified. Default is false.
 * @param enableWhiteSpace controls when whitespace is allowed. Default is true.
 * @param singleLine controls when this text field should becomes a single horizontally
 * scrolling text field instead of wrapping onto multiple lines. Default is true.
 * @param visualExpandLines the maximum height in terms of visible lines,
 * this param will be set to 1 if [singleLine] is true. Default is 1.
 * @param hideText if set to true, [PasswordVisualTransformation] will be applied to
 * this. Default is false.
 * @param keyboardOptions software keyboard options that contains configuration
 * @param keyboardActions when the input service emits an IME action, the corresponding
 * callback is called.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    labelString: String,
    placeholderString: String? = null,
    description: String? = null,
    errorMessage: String? = null,
    enableVisualError: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enable: Boolean = true,
    readOnly: Boolean = false,
    enableWhiteSpace: Boolean = true,
    singleLine: Boolean = true,
    visualExpandLines: Int = 1,
    hideText: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) = Column(modifier = modifier) {
    val visualTransformation =
        if (hideText) PasswordVisualTransformation() else VisualTransformation.None

    description?.let {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = it,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.height(height = 2.dp))
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = { value ->
            if (enableWhiteSpace) onValueChange(value.clearDoubleWhitespace())
            else onValueChange(value.clearAllWhitespace())
        },
        label = { Text(text = labelString) },
        placeholder = if (placeholderString == null) null else {
            { Text(text = placeholderString) }
        },
        isError = errorMessage != null && enableVisualError,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        enabled = enable,
        readOnly = readOnly,
        maxLines = visualExpandLines,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )

    errorMessage?.let {
        Spacer(modifier = Modifier.height(height = 2.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = it,
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

private fun String.clearDoubleWhitespace(): String {
    val regex = "\\s{2,}".toRegex()
    return this.replace(regex = regex, replacement = " ")
}

private fun String.clearAllWhitespace(): String {
    return this.filterNot { it.isWhitespace() }
}