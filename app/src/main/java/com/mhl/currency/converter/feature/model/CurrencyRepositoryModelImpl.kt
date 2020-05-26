package com.mhl.currency.converter.feature.model

import android.content.Context
import android.content.SharedPreferences
import com.mhl.currency.converter.common.RequestCompleteListener
import com.mhl.currency.converter.feature.model.data.Currency
import com.mhl.currency.converter.feature.model.data.ExchangeRate
import com.mhl.currency.converter.feature.model.local.LocalCurrencyRepoModelImpl
import com.mhl.currency.converter.feature.model.remote.RemoteCurrencyRepoModelImpl
import com.mhl.currency.converter.feature.viewmodel.RoomViewModel
import com.mhl.currency.converter.utility.Prefs
import java.text.SimpleDateFormat
import java.util.*

class CurrencyRepositoryModelImpl(private val context: Context, roomViewModel: RoomViewModel) : CurrencyRepositoryModel {
    lateinit var roomViewModel:RoomViewModel
    lateinit var dateFormat: SimpleDateFormat
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    init {
        this.roomViewModel=roomViewModel
        dateFormat = SimpleDateFormat(
            "MMM DD, yyyy hh:mm a",
            Locale.US
        )
        sharedPreferences =
            context.getSharedPreferences("CURRENCY_SHARED_PREF", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    override fun getCurrencyList(callback: RequestCompleteListener<Currency>) {

        val lastUpdate = Prefs(context).getCurrentTimeFromSharedPref("LAST_UPDATE_CURRENCY_LIST")
        if (lastUpdate!=null){
            val diffInMillis: Long =
                Date().time - Date(lastUpdate).time
            val diffInSec = diffInMillis / 1000
            val diffInMinutes = diffInSec / 60

            if (diffInMinutes>30){
                RemoteCurrencyRepoModelImpl(context, dateFormat, roomViewModel).getRemoteCurrencyList(callback)
            } else{
                LocalCurrencyRepoModelImpl(context, roomViewModel).getLocalCurrencyList(callback)
            }
        } else RemoteCurrencyRepoModelImpl(context, dateFormat, roomViewModel).getRemoteCurrencyList(callback)
    }

    override fun getExchangeRates(callback: RequestCompleteListener<ExchangeRate>) {

        val lastUpdate = Prefs(context).getCurrentTimeFromSharedPref("LAST_UPDATE_EXCHANGE_RATES")
        if (lastUpdate!=null){
            val diffInMillis: Long =
                Date().time - Date(lastUpdate).time
            val diffInSec = diffInMillis / 1000
            val diffInMinutes = diffInSec / 60

            if (diffInMinutes>30){
                RemoteCurrencyRepoModelImpl(context, dateFormat, roomViewModel).getRemoteExchangeRates(callback)
            } else{
                LocalCurrencyRepoModelImpl(context, roomViewModel).getLocalExchangeRates(callback)
            }
        } else RemoteCurrencyRepoModelImpl(context, dateFormat, roomViewModel).getRemoteExchangeRates(callback)
    }

}