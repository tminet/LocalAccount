package tmidev.localaccount.domain.usecase

import tmidev.localaccount.data.repository.UsersRepository
import javax.inject.Inject

/**
 * Use case to verify if the email is not registered with any user.
 */
interface IsEmailAvailableUseCase {
    /**
     * @param email the [String] email to be checked.
     *
     * @return true if the [email] is available to use, false otherwise.
     */
    suspend operator fun invoke(email: String): Boolean
}

/**
 * Implementation of [IsEmailAvailableUseCase] that uses [UsersRepository].
 */
class IsEmailAvailableUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : IsEmailAvailableUseCase {
    override suspend fun invoke(email: String): Boolean =
        usersRepository.isEmailAvailable(email = email)
}