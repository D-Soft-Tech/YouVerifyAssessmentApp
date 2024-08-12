package com.example.youverifyassessment.di

import com.example.youverifyassessment.data.local.datastore.PreferencesManager
import com.example.youverifyassessment.data.local.datastore.PreferencesManagerContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModel {

    @Singleton
    @Provides
    fun providesPreferencesManagerContract(
        prefManager: PreferencesManager
    ): PreferencesManagerContract = prefManager
}