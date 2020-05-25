package com.mhl.currency.converter.feature.model.data

data class ConvertedExchnageRate(
    var convertedAmount: Double,
    val currencyName: String,
    val exchangeRate: Double
)
