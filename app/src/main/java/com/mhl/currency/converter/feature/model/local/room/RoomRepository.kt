package com.mhl.currency.converter.feature.model.local.room

import androidx.lifecycle.LiveData
import com.mhl.currency.converter.feature.model.data.UnitCurrency
import com.mhl.currency.converter.feature.model.data.UnitExchangeRate

class RoomRepository(private val dao : CurrencyDAO) {

    val allCurrencyList:LiveData<List<UnitCurrency>> = dao.getCurrencyNames()
    val allExchangeRates:LiveData<List<UnitExchangeRate>> = dao.getExchangeRates()

    suspend fun insertCurrency(currency: UnitCurrency) {
        dao.insert(currency)
    }

    suspend fun insertExchangeRate(exchangeRate: UnitExchangeRate) {
        dao.insert(exchangeRate)
    }

    suspend fun deleteAllExchangeRate() {
        dao.deleteAllExchangeRates()
    }

    suspend fun deleteAllCurrency() {
        dao.deleteAllCurrencies()
    }


}