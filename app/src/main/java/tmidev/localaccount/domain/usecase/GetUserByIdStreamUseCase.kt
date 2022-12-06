package tmidev.localaccount.domain.usecase

import kotlinx.coroutines.flow.Flow
import tmidev.localaccount.data.repository.UsersRepository
import tmidev.localaccount.domain.model.User
import javax.inject.Inject

interface GetUserByIdStreamUseCase {
    operator fun invoke(id: Int): Flow<User?>
}

class GetUserByIdStreamUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : GetUserByIdStreamUseCase {
    override fun invoke(id: Int): Flow<User?> =
        usersRepository.getUserByIdStream(id = id)
}