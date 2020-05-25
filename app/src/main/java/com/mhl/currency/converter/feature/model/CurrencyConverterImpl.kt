package com.mhl.currency.converter.feature.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mhl.currency.converter.R
import com.mhl.currency.converter.common.RequestCompleteListener
import com.mhl.currency.converter.feature.model.data.Currency
import com.mhl.currency.converter.feature.model.data.ExchangeRate
import com.mhl.currency.converter.feature.model.data.UnitCurrency
import com.mhl.currency.converter.feature.model.data.UnitExchangeRate
import com.mhl.currency.converter.network.RetrofitApiInterface
import com.mhl.currency.converter.network.RetrofitClient
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

//TODO Add Repository, Local, Remote Architecture Later
class CurrencyConverterImpl(private val context: Context) : CurrencyConverterModel {
    val dateFormat: SimpleDateFormat = SimpleDateFormat(
        "MMM DD, yyyy hh:mm a",
        Locale.US
    )

    override fun getCurrencyList(callback: RequestCompleteListener<Currency>) {
        val apiInterface: RetrofitApiInterface =
            RetrofitClient.client.create(RetrofitApiInterface::class.java)
        val call: Call<ResponseBody> = apiInterface.getAllAvailableCurrencyList()

        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val currency: Currency?
                val currencyList: MutableList<UnitCurrency> = mutableListOf()
                if (response.body() != null) {
                    try {
                        var res = response.body()!!.string()
                        val jsonObject = JSONObject(res)
                        if (jsonObject.has("success")) {
                            if (jsonObject.getBoolean("success")) {
                                jsonObject.remove("success")
                                if (jsonObject.has("terms")) jsonObject.remove("terms")
                                if (jsonObject.has("privacy")) jsonObject.remove("privacy")
                                res = jsonObject.toString()
                                val mapType =
                                    object :
                                        TypeToken<Map<String?, Map<String, String>?>?>() {}.type
                                val currenciesMap =
                                    Gson().fromJson<Map<String, Map<String, String>>>(res, mapType)
                                val currencyMap =
                                    currenciesMap[currenciesMap.keys.toTypedArray()[0]] ?: error("")
                                val itr = currencyMap.entries.iterator()
                                while (itr.hasNext()) {
                                    val entry = itr.next()
                                    currencyList.add(UnitCurrency(entry.key, entry.value))
                                }
                                currency = Currency(dateFormat.format(Date()), currencyList)
                                callback.onRequestSuccess(currency)
                            } else {
                                var res = response.body()!!.string()
                                val jsonObject = JSONObject(res)
                                callback.onRequestFailed(
                                    jsonObject.getJSONObject("error").getString("info")
                                )
                            }
                        } else callback.onRequestFailed(context.getString(R.string.weird_api_response))

                    } catch (e: IOException) {
                        callback.onRequestFailed(context.getString(R.string.weird_api_response))
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        callback.onRequestFailed(context.getString(R.string.weird_api_response))
                        e.printStackTrace()
                    }
                } else
                    callback.onRequestFailed(context.getString(R.string.weird_api_response))
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onRequestFailed(t.message!!)
            }
        })
    }

    override fun getExchangeRates(callback: RequestCompleteListener<ExchangeRate>) {
        val apiInterface: RetrofitApiInterface =
            RetrofitClient.client.create(RetrofitApiInterface::class.java)
        val call: Call<ResponseBody> = apiInterface.getAllAvailableExchnageRates()

        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val exchangeRate: ExchangeRate?
                val unitExchangeRateList: MutableList<UnitExchangeRate> = mutableListOf()
                if (response.body() != null) {
                    try {
                        var res = response.body()!!.string()
                        val jsonObject = JSONObject(res)
                        if (jsonObject.has("success")) {
                            if (jsonObject.getBoolean("success")) {
                                jsonObject.remove("success")
                                if (jsonObject.has("terms")) jsonObject.remove("terms")
                                if (jsonObject.has("privacy")) jsonObject.remove("privacy")
                                if (jsonObject.has("source")) jsonObject.remove("source")
                                if (jsonObject.has("timestamp")) jsonObject.remove("timestamp")
                                res = jsonObject.toString()
                                val mapType =
                                    object :
                                        TypeToken<Map<String?, Map<String, Double>?>?>() {}.type
                                val currenciesMap =
                                    Gson().fromJson<Map<String, Map<String, Double>>>(res, mapType)
                                val currencyMap =
                                    currenciesMap[currenciesMap.keys.toTypedArray()[0]] ?: error("")
                                val itr = currencyMap.entries.iterator()
                                while (itr.hasNext()) {
                                    val entry = itr.next()
                                    unitExchangeRateList.add(
                                        UnitExchangeRate(
                                            entry.key,
                                            entry.value
                                        )
                                    )
                                }
                                exchangeRate = ExchangeRate(Date().toString(), unitExchangeRateList)
                                callback.onRequestSuccess(exchangeRate)
                            } else {
                                var res = response.body()!!.string()
                                val jsonObject = JSONObject(res)
                                callback.onRequestFailed(
                                    jsonObject.getJSONObject("error").getString("info")
                                )
                            }
                        } else callback.onRequestFailed(context.getString(R.string.weird_api_response))

                    } catch (e: IOException) {
                        callback.onRequestFailed(context.getString(R.string.weird_api_response))
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        callback.onRequestFailed(context.getString(R.string.weird_api_response))
                        e.printStackTrace()
                    }
                } else
                    callback.onRequestFailed(context.getString(R.string.weird_api_response))
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }
}
