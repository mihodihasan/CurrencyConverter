package com.mhl.currency.converter.feature.model.local

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.mhl.currency.converter.common.RequestCompleteListener
import com.mhl.currency.converter.feature.model.data.Currency
import com.mhl.currency.converter.feature.model.data.ExchangeRate
import com.mhl.currency.converter.feature.model.data.UnitCurrency
import com.mhl.currency.converter.feature.model.data.UnitExchangeRate
import com.mhl.currency.converter.feature.model.local.room.CurrencyDB
import com.mhl.currency.converter.feature.model.local.room.RoomRepository
import com.mhl.currency.converter.feature.viewmodel.RoomViewModel
import com.mhl.currency.converter.utility.Prefs

class LocalCurrencyRepoModelImpl(context:Context, viewModel: RoomViewModel) : LocalCurrencyRepoModel {
    lateinit var roomViewModel:RoomViewModel
    lateinit var context: Context
    init {
        this.context = context
        this.roomViewModel = viewModel
    }

    override fun getLocalCurrencyList(callback: RequestCompleteListener<Currency>) {
        val lastUpdate:String?=Prefs(context).getCurrentTimeFromSharedPref("LAST_UPDATE_CURRENCY_LIST")
        var currencies = mutableListOf<UnitCurrency>()
        roomViewModel.allCurrencies.value?.forEach {
            currencies.add(it)
        }
        val currency : Currency = Currency(lastUpdate, currencies)
        callback.onRequestSuccess(currency)
    }

    override fun getLocalExchangeRates(callback: RequestCompleteListener<ExchangeRate>) {
        val lastUpdate = Prefs(context).getCurrentTimeFromSharedPref("LAST_UPDATE_EXCHANGE_RATES")
        var exchangeRates = mutableListOf<UnitExchangeRate>()
        roomViewModel.allExchangeRates.value?.forEach {
            exchangeRates.add(it)
        }
        val rates = ExchangeRate(lastUpdate, exchangeRates)
        callback.onRequestSuccess(rates)
    }
}