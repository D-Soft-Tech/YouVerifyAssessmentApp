package com.example.youverifyassessment.di

import com.example.youverifyassessment.data.remote.apis.ProductsApiService
import com.example.youverifyassessment.data.remote.interceptors.AuthInterceptor
import com.example.youverifyassessment.data.remote.networkUtils.NetworkUtils
import com.example.youverifyassessment.utils.AppConstants.API_KEY_DI_NAME
import com.example.youverifyassessment.utils.AppConstants.AUTH_INTERCEPTOR_DI_NAME
import com.example.youverifyassessment.utils.AppConstants.BASE_URL_DI_NAME
import com.example.youverifyassessment.utils.AppConstants.GOOGLE_API_KEY_DI_NAME
import com.example.youverifyassessment.utils.AppConstants.LOGGING_INTERCEPTOR_DI_NAME
import com.example.youverifyassessment.utils.AppConstants.TIME_OUT_20
import com.example.youverifyassessment.utils.AppParameters
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named(BASE_URL_DI_NAME)
    fun providesAppBaseUrl(): String = AppParameters.BASE_URL

    @Provides
    @Singleton
    @Named(API_KEY_DI_NAME)
    fun providesApiToken(): String = "TO_BE_PROVIDED"

    @Provides
    @Singleton
    @Named(GOOGLE_API_KEY_DI_NAME)
    fun providesGoogleKey(): String = AppParameters.GOOGLE_API_KEY

    @Singleton
    @Provides
    @Named(LOGGING_INTERCEPTOR_DI_NAME)
    fun providesLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    @Named(AUTH_INTERCEPTOR_DI_NAME)
    fun providesInterceptor(authInterceptor: AuthInterceptor): Interceptor = authInterceptor

    @Singleton
    @Provides
    fun providesOKHTTPClient(
        @Named(LOGGING_INTERCEPTOR_DI_NAME) loggingInterceptor: Interceptor,
    ): OkHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(TIME_OUT_20, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT_20, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT_20, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .addInterceptor(loggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun providesRetrofitForAppNetworkCall(
        okHttpClient: OkHttpClient,
        @Named(BASE_URL_DI_NAME) baseUrl: String,
    ): Retrofit =
        Retrofit.Builder().baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun providesTMDBRequestsApiService(
        retrofit: Retrofit
    ): ProductsApiService = retrofit.create(ProductsApiService::class.java)

    @Singleton
    @Provides
    fun providesNetworkUtils(): NetworkUtils = NetworkUtils()

    @Singleton
    @Provides
    fun providesGoogleSignInOptions(
        @Named(GOOGLE_API_KEY_DI_NAME) googleApiKey: String
    ): GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(googleApiKey)
        .requestEmail()
        .requestProfile()
        .build()

    @Singleton
    @Provides
    fun providesFireBaseInstance(): FirebaseAuth = FirebaseAuth.getInstance()
}