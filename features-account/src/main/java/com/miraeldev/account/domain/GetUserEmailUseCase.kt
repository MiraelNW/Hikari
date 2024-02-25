package com.miraeldev.account.domain

import com.miraeldev.account.data.AccountRepository
import me.tatarka.inject.annotations.Inject

@Inject
class GetUserEmailUseCase(private val repository: AccountRepository) {
    suspend operator fun invoke() = repository.getUserEmail()
}