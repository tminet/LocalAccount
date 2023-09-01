package tmidev.localaccount.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tmidev.localaccount.data.datasource.room.LocalAccountDatabase
import tmidev.localaccount.data.datasource.room.dao.UsersDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun providesAppDatabase(
        @ApplicationContext context: Context
    ): LocalAccountDatabase = Room.databaseBuilder(
        context, LocalAccountDatabase::class.java, "app_database"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providesUsersDao(
        database: LocalAccountDatabase
    ): UsersDao = database.usersDao
}