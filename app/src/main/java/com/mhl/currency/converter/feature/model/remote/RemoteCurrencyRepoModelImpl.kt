package com.mhl.currency.converter.feature.model.remote

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mhl.currency.converter.R
import com.mhl.currency.converter.common.RequestCompleteListener
import com.mhl.currency.converter.feature.model.data.Currency
import com.mhl.currency.converter.feature.model.data.ExchangeRate
import com.mhl.currency.converter.feature.model.data.UnitCurrency
import com.mhl.currency.converter.feature.model.data.UnitExchangeRate
import com.mhl.currency.converter.feature.viewmodel.RoomViewModel
import com.mhl.currency.converter.network.RetrofitApiInterface
import com.mhl.currency.converter.network.RetrofitClient
import com.mhl.currency.converter.utility.Prefs
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class RemoteCurrencyRepoModelImpl(context: Context, roomViewModel: RoomViewModel) :
    RemoteCurrencyRepoModel {
    lateinit var context: Context
    lateinit var roomViewModel: RoomViewModel

    init {
        this.context = context
        this.roomViewModel = roomViewModel
    }

    override fun getRemoteCurrencyList(callback: RequestCompleteListener<Currency>) {
        val apiInterface: RetrofitApiInterface =
            RetrofitClient.client.create(RetrofitApiInterface::class.java)
        val call: Call<ResponseBody> = apiInterface.getAllAvailableCurrencyList()

        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Prefs(context).saveCurrentTimeToSharedPref("LAST_UPDATE_CURRENCY_LIST", Date().toString())
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
                                roomViewModel.deleteAllCurrency()
                                while (itr.hasNext()) {
                                    val entry = itr.next()
                                    val unitCurrency = UnitCurrency(entry.key, entry.value)
                                    currencyList.add(unitCurrency)
                                    roomViewModel.insertCurrency(unitCurrency)
                                }
                                currency = Currency(Date().toString(), currencyList)
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

    override fun getRemoteExchangeRates(callback: RequestCompleteListener<ExchangeRate>) {
        val apiInterface: RetrofitApiInterface =
            RetrofitClient.client.create(RetrofitApiInterface::class.java)
        val call: Call<ResponseBody> = apiInterface.getAllAvailableExchnageRates()

        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Prefs(context).saveCurrentTimeToSharedPref("LAST_UPDATE_EXCHANGE_RATES", Date().toString())
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
                                roomViewModel.deleteAllExchangeRates()
                                while (itr.hasNext()) {
                                    val entry = itr.next()
                                    val unitExchangeRate=UnitExchangeRate(entry.key, entry.value)
                                    unitExchangeRateList.add(unitExchangeRate)
                                    roomViewModel.insertExchangeRate(unitExchangeRate)
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