package com.example.youverifyassessment.data.remote.interceptors

import com.example.youverifyassessment.data.remote.annotations.AuthorizedRequest
import com.example.youverifyassessment.utils.AppConstants.API_KEY_DI_NAME
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    @Named(API_KEY_DI_NAME) private val apiKey: String,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder: Request.Builder = chain.request().newBuilder()
        val allAnnotations: ArrayList<Annotation> = arrayListOf(AuthorizedRequest())
        allAnnotations.forEach { _ ->
            val annotation = grabAnnotation<Annotation>(chain.request())
            annotation?.let {
                addAppropriateTokenForEachAnnotation(requestBuilder, it)
            }
        }
        return chain.proceed(requestBuilder.build())
    }

    private inline fun <reified T> grabAnnotation(request: Request): T? {
        return request.tag(Invocation::class.java)
            ?.method()
            ?.annotations
            ?.filterIsInstance(T::class.java)
            ?.firstOrNull()
    }

    private fun addAppropriateTokenForEachAnnotation(
        requestBuilder: Request.Builder,
        annotation: Annotation,
    ): Request.Builder {
        when (annotation) {
            is AuthorizedRequest -> {
                requestBuilder.addHeader("Authorization", "Bearer $apiKey")
            }

            else -> {
                /* Do nothing */
            }
        }
        return requestBuilder
    }
}
