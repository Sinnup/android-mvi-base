package com.capaocho.espert.domain.usecase

import com.capaocho.espert.domain.common.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class BaseUseCase<in P, out R : Any>(
    private val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(params: P): Flow<Result<R>> =
        execute(params)
            .catch { throwable -> emit(Result.Error(Exception(throwable))) }
            .flowOn(dispatcher)

    protected abstract fun execute(params: P): Flow<Result<R>>
}