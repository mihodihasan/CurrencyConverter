package com.mhl.currency.converter.feature.model

import com.mhl.currency.converter.common.RequestCompleteListener
import com.mhl.currency.converter.feature.model.data.Currency
import com.mhl.currency.converter.feature.model.data.ExchangeRate

interface CurrencyRepositoryModel {
    fun getCurrencyList(callback: RequestCompleteListener<Currency>)
    fun getExchangeRates(callback: RequestCompleteListener<ExchangeRate>)
}