package tmidev.localaccount.domain.usecase

import kotlinx.coroutines.flow.Flow
import tmidev.localaccount.data.repository.UsersRepository
import tmidev.localaccount.domain.model.User
import javax.inject.Inject

/**
 * Use case to get an stream data of user by id.
 */
interface GetUserByIdStreamUseCase {
    /**
     * @param id the [Int] id of user to be get.
     *
     * @return [Flow] of [User] or null if there is no user found for the provided [id].
     */
    operator fun invoke(id: Int): Flow<User?>
}

/**
 * Implementation of [GetUserByIdStreamUseCase] that uses [UsersRepository].
 */
class GetUserByIdStreamUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : GetUserByIdStreamUseCase {
    override fun invoke(id: Int): Flow<User?> =
        usersRepository.getUserByIdStream(id = id)
}