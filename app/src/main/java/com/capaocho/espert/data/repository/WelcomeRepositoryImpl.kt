package com.capaocho.espert.data.repository

import com.capaocho.espert.domain.common.Result
import com.capaocho.espert.domain.repository.WelcomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WelcomeRepositoryImpl @Inject constructor() : WelcomeRepository {
    override fun getWelcomeMessage(): Flow<Result<String>> = flow {
        // In a real app, this would come from a network or local data source
        emit(Result.Success("Welcome to the Espert App!"))
    }
}