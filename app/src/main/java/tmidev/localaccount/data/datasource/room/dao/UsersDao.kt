package tmidev.localaccount.data.datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tmidev.localaccount.data.datasource.room.model.UserEntity

@Dao
interface UsersDao {
    @Query(value = "SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserByIdStream(id: Int): Flow<UserEntity?>

    @Query(value = "SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): UserEntity?

    @Query(value = "SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query(value = "SELECT COUNT() FROM users WHERE email = :email LIMIT 1")
    suspend fun isEmailInUse(email: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserOrIgnore(userEntity: UserEntity): Long

    @Update
    suspend fun updateUser(userEntity: UserEntity)
}