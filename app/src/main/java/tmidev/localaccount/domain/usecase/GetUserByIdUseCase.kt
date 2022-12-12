package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.UsersRepository
import tmidev.localaccount.domain.model.User
import javax.inject.Inject

/**
 * Use case to get the user by id.
 */
interface GetUserByIdUseCase {
    /**
     * @param id the [Int] id of user to be get.
     *
     * @return [User] or null if there is no user found for the provided [id].
     */
    suspend operator fun invoke(id: Int): User?
}

/**
 * Implementation of [GetUserByIdUseCase] that uses [UsersRepository].
 */
class GetUserByIdUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : GetUserByIdUseCase {
    override suspend fun invoke(id: Int): User? =
        usersRepository.getUserById(id = id)
}