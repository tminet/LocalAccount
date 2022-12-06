package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.UsersRepository
import tmidev.localaccount.domain.model.User
import javax.inject.Inject

interface GetUserByEmailUseCase {
    suspend operator fun invoke(email: String): User?
}

class GetUserByEmailUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : GetUserByEmailUseCase {
    override suspend fun invoke(email: String): User? =
        usersRepository.getUserByEmail(email = email)
}