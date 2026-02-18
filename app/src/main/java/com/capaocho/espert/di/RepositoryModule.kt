package com.capaocho.espert.di

import com.capaocho.espert.data.repository.WelcomeRepositoryImpl
import com.capaocho.espert.domain.repository.WelcomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindWelcomeRepository(repository: WelcomeRepositoryImpl): WelcomeRepository
}