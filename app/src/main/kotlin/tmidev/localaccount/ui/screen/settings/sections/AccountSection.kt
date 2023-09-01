package tmidev.localaccount.ui.screen.settings.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import tmidev.localaccount.R
import tmidev.localaccount.ui.component.LaButton
import tmidev.localaccount.ui.component.LaOutlinedTextField
import tmidev.localaccount.util.ClearTrailingButton
import tmidev.localaccount.util.LaIcons
import tmidev.localaccount.util.safeStringResource

/**
 * An composable to display the user account data.
 *
 * @param modifier the [Modifier] to apply on the container of this composable.
 * @param name string that represents the user name.
 * @param nameErrorResId nullable int that represents an string resource to display when something
 * is wrong with the [name].
 * @param changeName callback to change the [name].
 * @param email string that represents the user email.
 * @param emailErrorResId nullable int that represents an string resource to display when something
 * is wrong with the [email].
 * @param changeEmail callback to change the [email].
 * @param saveAccountData callback to save all information from this user.
 */
@Composable
fun AccountSection(
    modifier: Modifier = Modifier,
    name: String,
    nameErrorResId: Int?,
    changeName: (String) -> Unit,
    email: String,
    emailErrorResId: Int?,
    changeEmail: (String) -> Unit,
    saveAccountData: () -> Unit
) = Column(modifier = modifier) {
    val focusManager = LocalFocusManager.current

    Text(
        text = stringResource(id = R.string.account),
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.height(height = 8.dp))

    LaOutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = name,
        onValueChange = { changeName(it) },
        labelString = stringResource(id = R.string.name),
        placeholderString = stringResource(id = R.string.namePlaceholder),
        errorMessage = safeStringResource(id = nameErrorResId),
        leadingIcon = {
            Icon(imageVector = LaIcons.Person, contentDescription = null)
        },
        trailingIcon = if (name.isEmpty()) null else {
            { ClearTrailingButton(onClick = { changeName("") }) }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Down) }
        )
    )

    Spacer(modifier = Modifier.height(height = 16.dp))

    LaOutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = email,
        onValueChange = { changeEmail(it) },
        labelString = stringResource(id = R.string.email),
        placeholderString = stringResource(id = R.string.emailPlaceholder),
        errorMessage = safeStringResource(id = emailErrorResId),
        leadingIcon = {
            Icon(imageVector = LaIcons.Email, contentDescription = null)
        },
        trailingIcon = if (email.isEmpty()) null else {
            { ClearTrailingButton(onClick = { changeEmail("") }) }
        },
        enableWhiteSpace = false,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        )
    )

    Spacer(modifier = Modifier.height(height = 16.dp))

    LaButton(
        modifier = Modifier.align(alignment = Alignment.End),
        text = stringResource(id = R.string.saveChanges),
        onClick = saveAccountData
    )

    Spacer(modifier = Modifier.height(height = 16.dp))
}