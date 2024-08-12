package com.example.youverifyassessment.utils

object AppParameters {
    init {
        System.loadLibrary("api-keys")
    }

    private external fun getBaseUrl(): String
    val BASE_URL = getBaseUrl()

    private external fun getGoogleKey(): String
    val GOOGLE_API_KEY = getGoogleKey()

    private external fun getEncryptionAlgorithmKeyType(): String
    val ENCRYPTION_ALGORITHM_TYPE = getEncryptionAlgorithmKeyType()

    private external fun encryptionKey(): String
    val ENCRYPTION_KEY = encryptionKey()

    private external fun encryptionPadding(): String
    val ENCRYPTION_PADDING = encryptionPadding()

    private external fun getIv(): String
    val ENCRYPTION_IV = getIv()
}