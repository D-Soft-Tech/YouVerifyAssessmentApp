package com.example.youverifyassessment.di

import android.content.Context
import com.example.youverifyassessment.domain.DeviceUtilsContract
import com.example.youverifyassessment.domain.DeviceUtilsUseCase
import com.example.youverifyassessment.domain.encryption.DataEncryptionContract
import com.example.youverifyassessment.domain.encryption.DataEncryptionUseCase
import com.example.youverifyassessment.utils.AppConstants.ENCRYPTION_ALG_TYPE_DI_NAME
import com.example.youverifyassessment.utils.AppConstants.ENCRYPTION_IV_DI_NAME
import com.example.youverifyassessment.utils.AppConstants.ENCRYPTION_PADDING_DI_NAME
import com.example.youverifyassessment.utils.AppConstants.ENCRYPTION_SECRET_KEY_DI_NAME
import com.example.youverifyassessment.utils.AppConstants.IO_DISPATCHER_DI_NAME
import com.example.youverifyassessment.utils.AppConstants.MAIN_THREAD_DISPATCHER_DI_NAME
import com.example.youverifyassessment.utils.AppParameters
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    @Named(IO_DISPATCHER_DI_NAME)
    fun providesIODispatchers(): CoroutineContext = Dispatchers.IO

    @Singleton
    @Provides
    @Named(MAIN_THREAD_DISPATCHER_DI_NAME)
    fun providesMainThreadDispatchers(): CoroutineContext = Dispatchers.Main

    @Singleton
    @Provides
    fun providesGson(): Gson = Gson()

    @Singleton
    @Provides
    @Named(ENCRYPTION_SECRET_KEY_DI_NAME)
    fun providesEncryptionSecretKey(): String = AppParameters.ENCRYPTION_KEY

    @Singleton
    @Provides
    @Named(ENCRYPTION_IV_DI_NAME)
    fun providesEncryptionIV(): String = AppParameters.ENCRYPTION_IV

    @Singleton
    @Provides
    @Named(ENCRYPTION_ALG_TYPE_DI_NAME)
    fun providesEncryptionAlgorithmType(): String = AppParameters.ENCRYPTION_ALGORITHM_TYPE

    @Singleton
    @Provides
    @Named(ENCRYPTION_PADDING_DI_NAME)
    fun providesEncryptionPadding(): String = AppParameters.ENCRYPTION_PADDING

    @Singleton
    @Provides
    fun providesDataEncryptionContract(
        @Named(ENCRYPTION_SECRET_KEY_DI_NAME) secretKey: String,
        @Named(ENCRYPTION_IV_DI_NAME) iv: String,
        @Named(ENCRYPTION_ALG_TYPE_DI_NAME) encryptionAlgType: String,
        @Named(ENCRYPTION_PADDING_DI_NAME) encryptionPaddingType: String
    ): DataEncryptionContract =
        DataEncryptionUseCase(secretKey, iv, encryptionAlgType, encryptionPaddingType)

    @Singleton
    @Provides
    fun providesDeviceUtilityContract(
        @ApplicationContext context: Context
    ): DeviceUtilsContract = DeviceUtilsUseCase(context)
}