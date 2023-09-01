package tmidev.localaccount.data.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import tmidev.localaccount.data.datasource.room.dao.UsersDao
import tmidev.localaccount.data.datasource.room.model.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LocalAccountDatabase : RoomDatabase() {
    abstract val usersDao: UsersDao
}