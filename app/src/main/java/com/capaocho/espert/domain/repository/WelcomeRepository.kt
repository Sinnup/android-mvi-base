package com.capaocho.espert.domain.repository

import com.capaocho.espert.domain.common.Result
import kotlinx.coroutines.flow.Flow

interface WelcomeRepository {
    fun getWelcomeMessage(): Flow<Result<String>>
}