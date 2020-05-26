package com.mhl.currency.converter.feature.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mhl.currency.converter.feature.model.data.UnitCurrency
import com.mhl.currency.converter.feature.model.data.UnitExchangeRate
import com.mhl.currency.converter.feature.model.local.room.CurrencyDB
import com.mhl.currency.converter.feature.model.local.room.RoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RoomRepository
    val allCurrencies: LiveData<List<UnitCurrency>>
    val allExchangeRates: LiveData<List<UnitExchangeRate>>

    init {
        val dao = CurrencyDB.getDatabase(application, viewModelScope).currencyDao()
        repository = RoomRepository(dao)
        allCurrencies = repository.allCurrencyList
        allExchangeRates = repository.allExchangeRates
    }

    fun insertExchangeRate(exchangeRate: UnitExchangeRate) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertExchangeRate(exchangeRate)
    }

    fun insertCurrency(currency: UnitCurrency) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertCurrency(currency)
    }

    fun deleteAllCurrency() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllCurrency()
    }

    fun deleteAllExchangeRates() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllExchangeRate()
    }


}