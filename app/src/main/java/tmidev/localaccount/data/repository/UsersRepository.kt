package tmidev.localaccount.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmidev.localaccount.data.datasource.room.dao.UsersDao
import tmidev.localaccount.data.datasource.room.model.UserEntity
import tmidev.localaccount.domain.model.User
import javax.inject.Inject

/**
 * Repository interface for [User].
 */
interface UsersRepository {
    /**
     * Get a data stream of nullable user by id.
     *
     * @param id the [Int] id of user to be get.
     *
     * @return [Flow] of [User] or null if there is no user found for the provided [id].
     */
    fun getUserByIdStream(id: Int): Flow<User?>

    /**
     * Get a nullable user by id.
     *
     * @param id the [Int] id of user to be get.
     *
     * @return [User] or null if there is no user found for the provided [id].
     */
    suspend fun getUserById(id: Int): User?

    /**
     * Get a nullable user by email.
     *
     * @param email the [String] email of user to be get.
     *
     * @return [User] or null if there is no user found for the provided [email].
     */
    suspend fun getUserByEmail(email: String): User?

    /**
     * Checks if the email is not registered with any user.
     *
     * @param email the [String] email to be checked.
     *
     * @return true if the [email] is available to use, false otherwise.
     */
    suspend fun isEmailAvailable(email: String): Boolean

    /**
     * Insert an user.
     *
     * @param user the [User] to be inserted.
     *
     * @return an [Int] value that represents the current id of the inserted user.
     * -1 is returned if this operation fails.
     */
    suspend fun insertUser(user: User): Int

    /**
     * Update an user.
     *
     * @param user the [User] to be matched and updated.
     */
    suspend fun updateUser(user: User)
}

/**
 * Implementation of [UsersRepository] that uses [UsersDao].
 */
class UsersRepositoryImpl @Inject constructor(
    private val usersDao: UsersDao
) : UsersRepository {
    override fun getUserByIdStream(id: Int): Flow<User?> =
        usersDao.getUserByIdStream(id = id).map { userEntity ->
            userEntity?.asUser()
        }

    override suspend fun getUserById(id: Int): User? {
        val userEntity = usersDao.getUserById(id = id)
        return userEntity?.asUser()
    }

    override suspend fun getUserByEmail(email: String): User? {
        val userEntity = usersDao.getUserByEmail(email = email)
        return userEntity?.asUser()
    }

    override suspend fun isEmailAvailable(email: String): Boolean =
        !usersDao.isEmailInUse(email = email)

    override suspend fun insertUser(user: User): Int {
        val userEntity = user.asUserEntity()
        return usersDao.insertUserOrIgnore(userEntity = userEntity).toInt()
    }

    override suspend fun updateUser(user: User) {
        val userEntity = user.asUserEntity()
        usersDao.updateUser(userEntity = userEntity)
    }

    /**
     * Extension to map [UserEntity] into [User].
     */
    private fun UserEntity.asUser() = User(
        id = id,
        name = name,
        email = email,
        password = password
    )

    /**
     * Extension to map [User] into [UserEntity].
     */
    private fun User.asUserEntity() = UserEntity(
        id = id,
        name = name,
        email = email,
        password = password
    )
}