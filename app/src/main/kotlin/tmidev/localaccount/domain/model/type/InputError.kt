package tmidev.localaccount.domain.model.type

/**
 * Types of error for input.
 */
enum class InputError {
    FieldEmpty,
    FieldInvalid,
    FieldLessMinCharacters,
    FieldMoreMaxCharacters
}