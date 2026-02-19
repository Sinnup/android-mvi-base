package com.capaocho.espert.domain.usecase

import com.capaocho.espert.domain.common.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

/**
 * Base class for all Use Cases in the domain layer.
 *
 * This abstraction standardizes the execution of business logic, handling thread
 * management with a [CoroutineDispatcher] and providing centralized error handling.
 *
 * @param P The type of input parameters for the use case.
 * @param R The type of the result data when the operation is successful.
 * @property dispatcher The [CoroutineDispatcher] on which the use case logic will run.
 */
abstract class BaseUseCase<in P, out R : Any>(
    private val dispatcher: CoroutineDispatcher
) {

    /**
     * Executes the use case with the provided parameters.
     *
     * It uses the [invoke] operator for a more idiomatic syntax in Kotlin.
     * Exceptions are caught and transformed into [Result.Error].
     *
     * @param params The parameters needed for the operation.
     * @return A [Flow] emitting the [Result] of the operation.
     */
    operator fun invoke(params: P): Flow<Result<R>> =
        execute(params)
            .catch { throwable -> emit(Result.Error(Exception(throwable))) }
            .flowOn(dispatcher)

    /**
     * Abstract method to be implemented with the specific logic of the use case.
     *
     * @param params The parameters needed for the operation.
     * @return A [Flow] emitting the [Result] of the operation.
     */
    protected abstract fun execute(params: P): Flow<Result<R>>
}
