package tmidev.localaccount.data.datasource.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import tmidev.localaccount.domain.model.User

/**
 * Room entity for [User].
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val email: String,
    val password: String
)