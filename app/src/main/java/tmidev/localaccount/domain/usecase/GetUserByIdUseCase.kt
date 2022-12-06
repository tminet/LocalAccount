package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.UsersRepository
import tmidev.localaccount.domain.model.User
import javax.inject.Inject

interface GetUserByIdUseCase {
    suspend operator fun invoke(id: Int): User?
}

class GetUserByIdUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : GetUserByIdUseCase {
    override suspend fun invoke(id: Int): User? =
        usersRepository.getUserById(id = id)
}