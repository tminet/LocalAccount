package tmidev.localaccount.domain.model

/**
 * Model class that represents an user.
 */
data class User(
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String
)