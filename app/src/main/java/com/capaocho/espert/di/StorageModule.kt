package com.capaocho.espert.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.capaocho.espert.data.storage.DataStorePreferenceStorage
import com.capaocho.espert.domain.storage.PreferenceStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    @Binds
    @Singleton
    abstract fun bindPreferenceStorage(
        dataStorePreferenceStorage: DataStorePreferenceStorage
    ): PreferenceStorage

    companion object {
        private const val USER_PREFERENCES = "user_preferences"

        @Provides
        @Singleton
        fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
            return PreferenceDataStoreFactory.create(
                produceFile = { appContext.preferencesDataStoreFile(USER_PREFERENCES) }
            )
        }
    }
}
