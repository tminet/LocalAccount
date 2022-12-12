package tmidev.localaccount.data.datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tmidev.localaccount.data.datasource.room.model.UserEntity

/**
 * Data Access Object to deal with [UserEntity].
 */
@Dao
interface UsersDao {
    /**
     * Get a data stream of nullable user entity by id.
     *
     * @param id the [Int] id of user to be get.
     *
     * @return [Flow] of [UserEntity] or null if there is no user found for the provided [id].
     */
    @Query(value = "SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserByIdStream(id: Int): Flow<UserEntity?>

    /**
     * Get a nullable user entity by id.
     *
     * @param id the [Int] id of user to be get.
     *
     * @return [UserEntity] or null if there is no user found for the provided [id].
     */
    @Query(value = "SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): UserEntity?

    /**
     * Get a nullable user entity by email.
     *
     * @param email the [String] email of user to be get.
     *
     * @return [UserEntity] or null if there is no user found for the provided [email].
     */
    @Query(value = "SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    /**
     * Verify if an email is saved on database.
     *
     * @param email the [String] email to be checked.
     *
     * @return true if the [email] is saved, false if the [email] is not saved.
     */
    @Query(value = "SELECT COUNT() FROM users WHERE email = :email LIMIT 1")
    suspend fun isEmailInUse(email: String): Boolean

    /**
     * Insert an user with [OnConflictStrategy.IGNORE].
     *
     * @param userEntity the [UserEntity] to be inserted.
     *
     * @return an [Long] value that represents the current id of the inserted user.
     * -1 is returned if this operation fails.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserOrIgnore(userEntity: UserEntity): Long

    /**
     * Update an user.
     *
     * @param userEntity the [UserEntity] to be matched and updated.
     */
    @Update
    suspend fun updateUser(userEntity: UserEntity)
}