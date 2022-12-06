package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.UsersRepository
import tmidev.localaccount.domain.model.User
import javax.inject.Inject

interface UpdateUserUseCase {
    suspend operator fun invoke(user: User)
}

class UpdateUserUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : UpdateUserUseCase {
    override suspend fun invoke(user: User) =
        usersRepository.updateUser(user = user)
}