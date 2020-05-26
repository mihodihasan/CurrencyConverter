package com.mhl.currency.converter.feature.model.local

import com.mhl.currency.converter.common.RequestCompleteListener
import com.mhl.currency.converter.feature.model.data.Currency
import com.mhl.currency.converter.feature.model.data.ExchangeRate

interface LocalCurrencyRepoModel {
    fun getLocalCurrencyList(callback: RequestCompleteListener<Currency>)
    fun getLocalExchangeRates(callback: RequestCompleteListener<ExchangeRate>)
}