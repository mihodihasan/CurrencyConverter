package com.mhl.currency.converter.feature.model.data

data class ExchangeRate(val lastUpdated: String?, val exchangeRates: MutableList<UnitExchangeRate>)