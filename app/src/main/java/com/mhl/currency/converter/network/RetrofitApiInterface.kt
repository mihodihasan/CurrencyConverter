package com.mhl.currency.converter.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitApiInterface {

    @GET("list")
    fun getAllAvailableCurrencyList(): Call<ResponseBody>

    @GET("live")
    fun getAllAvailableExchnageRates(): Call<ResponseBody>

}