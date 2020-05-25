package com.mhl.currency.converter.network

import android.provider.UserDictionary.Words.APP_ID
import com.mhl.currency.converter.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class QueryParameterAddInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val url = chain.request().url().newBuilder()
            .addQueryParameter("access_key", BuildConfig.API_KEY)
            .build()

        val request = chain.request().newBuilder()
            // .addHeader("Authorization", "Bearer token")
            .url(url)
            .build()

        return chain.proceed(request)
    }
}