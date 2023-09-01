package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.UsersRepository
import tmidev.localaccount.domain.model.User
import javax.inject.Inject

/**
 * Use case to update an user.
 */
interface UpdateUserUseCase {
    /**
     * @param user the [User] to be matched and updated.
     */
    suspend operator fun invoke(user: User)
}

/**
 * Implementation of [UpdateUserUseCase] that uses [UsersRepository].
 */
class UpdateUserUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : UpdateUserUseCase {
    override suspend fun invoke(user: User) =
        usersRepository.updateUser(user = user)
}