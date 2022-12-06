package tmidev.localaccount.domain.model

/**
 * User model class.
 */
data class User(
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String
)