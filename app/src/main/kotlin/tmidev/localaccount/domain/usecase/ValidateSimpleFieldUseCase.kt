package tmidev.localaccount.domain.usecase

import tmidev.localaccount.domain.model.type.InputError
import tmidev.localaccount.domain.model.type.InputResult
import javax.inject.Inject

/**
 * Use case to validate an string.
 */
interface ValidateSimpleFieldUseCase {
    /**
     * @param string the [String] to be validated.
     * @param minChar optional check if the [string] needs to have a minimum char size.
     * If null, the min char size check will be ignored. Default is null.
     * @param maxChar optional check if the [string] needs to have a maximum char size.
     * If null, the max char size check will be ignored. Default is null.
     *
     * @return [InputResult].
     */
    operator fun invoke(
        string: String,
        minChar: Int? = null,
        maxChar: Int? = null
    ): InputResult
}

/**
 * Implementation of [ValidateSimpleFieldUseCase].
 */
class ValidateSimpleFieldUseCaseImpl @Inject constructor() : ValidateSimpleFieldUseCase {
    override fun invoke(string: String, minChar: Int?, maxChar: Int?): InputResult {
        if (string.isBlank()) return InputResult.Error(
            inputError = InputError.FieldEmpty
        )

        if (minChar != null && string.length < minChar) return InputResult.Error(
            inputError = InputError.FieldLessMinCharacters
        )

        if (maxChar != null && string.length > maxChar) return InputResult.Error(
            inputError = InputError.FieldMoreMaxCharacters
        )

        return InputResult.Success
    }
}