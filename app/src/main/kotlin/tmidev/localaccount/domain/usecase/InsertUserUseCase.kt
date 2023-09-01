package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.UsersRepository
import tmidev.localaccount.domain.model.User
import javax.inject.Inject

/**
 * Use case to insert an user.
 */
interface InsertUserUseCase {
    /**
     * @param user the [User] to be inserted.
     *
     * @return an [Int] value that represents the current id of the inserted user.
     * -1 is returned if this operation fails.
     */
    suspend operator fun invoke(user: User): Int
}

/**
 * Implementation of [InsertUserUseCase] that uses [UsersRepository].
 */
class InsertUserUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : InsertUserUseCase {
    override suspend fun invoke(user: User): Int =
        usersRepository.insertUser(user = user)
}