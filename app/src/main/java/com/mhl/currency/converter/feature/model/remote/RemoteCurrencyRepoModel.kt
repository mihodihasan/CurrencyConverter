package com.mhl.currency.converter.feature.model.remote

import com.mhl.currency.converter.common.RequestCompleteListener
import com.mhl.currency.converter.feature.model.CurrencyRepositoryModel
import com.mhl.currency.converter.feature.model.data.Currency
import com.mhl.currency.converter.feature.model.data.ExchangeRate

interface RemoteCurrencyRepoModel {

    fun getRemoteCurrencyList(callback: RequestCompleteListener<Currency>)
    fun getRemoteExchangeRates(callback: RequestCompleteListener<ExchangeRate>)

}