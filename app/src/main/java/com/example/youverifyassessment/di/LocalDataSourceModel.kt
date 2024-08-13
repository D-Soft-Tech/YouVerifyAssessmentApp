package com.example.youverifyassessment.di

import android.content.Context
import com.example.youverifyassessment.data.local.datastore.PreferencesManager
import com.example.youverifyassessment.data.local.datastore.PreferencesManagerContract
import com.example.youverifyassessment.data.local.datastore.sessions.SessionManagerContract
import com.example.youverifyassessment.data.local.datastore.sessions.SessionManagerUseCase
import com.example.youverifyassessment.data.local.db.dao.ProductDao
import com.example.youverifyassessment.data.local.db.dao.ShoppingCartDao
import com.example.youverifyassessment.data.local.db.database.YouVerifyAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Singleton
    @Provides
    fun providesSessionManagerContract(
        sessionManagerUseCase: SessionManagerUseCase
    ): SessionManagerContract = sessionManagerUseCase

    @Singleton
    @Provides
    fun providesYouVerifyAppDatabase(
        @ApplicationContext context: Context
    ): YouVerifyAppDatabase = YouVerifyAppDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun providesProductDao(
        youVerifyAppDatabase: YouVerifyAppDatabase
    ): ProductDao = youVerifyAppDatabase.getProductsDao()

    @Singleton
    @Provides
    fun providesShoppingCartDao(
        youVerifyAppDatabase: YouVerifyAppDatabase
    ): ShoppingCartDao = youVerifyAppDatabase.getShoppingCartDao()
}