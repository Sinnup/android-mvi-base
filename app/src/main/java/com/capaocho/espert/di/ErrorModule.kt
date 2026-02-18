package com.capaocho.espert.di

import com.capaocho.espert.data.error.ErrorHandlerImpl
import com.capaocho.espert.domain.error.ErrorHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorModule {

    @Binds
    abstract fun bindErrorHandler(errorHandler: ErrorHandlerImpl): ErrorHandler
}