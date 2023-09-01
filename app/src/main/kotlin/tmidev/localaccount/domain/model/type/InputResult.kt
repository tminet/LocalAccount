package tmidev.localaccount.domain.model.type

/**
 * Types of result for a input validation.
 */
sealed interface InputResult {
    data object Success : InputResult

    data class Error(
        val inputError: InputError
    ) : InputResult
}