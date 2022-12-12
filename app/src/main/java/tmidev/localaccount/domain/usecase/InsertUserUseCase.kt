package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.UsersRepository
import tmidev.localaccount.domain.model.User
import javax.inject.Inject

interface InsertUserUseCase {
    suspend operator fun invoke(user: User): Int
}

class InsertUserUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : InsertUserUseCase {
    override suspend fun invoke(user: User): Int =
        usersRepository.insertUser(user = user)
}