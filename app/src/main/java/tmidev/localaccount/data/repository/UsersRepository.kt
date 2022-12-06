package tmidev.localaccount.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmidev.localaccount.data.datasource.room.dao.UsersDao
import tmidev.localaccount.data.datasource.room.model.UserEntity
import tmidev.localaccount.domain.model.User
import javax.inject.Inject

interface UsersRepository {
    fun getUserByIdStream(id: Int): Flow<User?>

    suspend fun getUserById(id: Int): User?

    suspend fun getUserByEmail(email: String): User?

    suspend fun isEmailAvailable(email: String): Boolean

    suspend fun insertUser(user: User): Long

    suspend fun updateUser(user: User)
}

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

    override suspend fun insertUser(user: User): Long {
        val userEntity = user.asUserEntity()
        return usersDao.insertUserOrIgnore(userEntity = userEntity)
    }

    override suspend fun updateUser(user: User) {
        val userEntity = user.asUserEntity()
        usersDao.updateUser(userEntity = userEntity)
    }

    private fun UserEntity.asUser() = User(
        id = id,
        name = name,
        email = email,
        password = password
    )

    private fun User.asUserEntity() = UserEntity(
        id = id,
        name = name,
        email = email,
        password = password
    )
}