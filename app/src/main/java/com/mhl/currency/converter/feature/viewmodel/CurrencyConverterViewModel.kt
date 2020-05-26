package com.mhl.currency.converter.feature.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mhl.currency.converter.common.RequestCompleteListener
import com.mhl.currency.converter.feature.model.CurrencyRepositoryModel
import com.mhl.currency.converter.feature.model.data.ConvertedExchnageRate
import com.mhl.currency.converter.feature.model.data.Currency
import com.mhl.currency.converter.feature.model.data.ExchangeRate
import com.mhl.currency.converter.feature.model.data.UnitExchangeRate

class CurrencyConverterViewModel : ViewModel() {
    val currencyLiveData = MutableLiveData<Currency>()
    val currencyFailureLiveData = MutableLiveData<String>()
    val exchangeRateLiveData = MutableLiveData<ExchangeRate>()
    val exchangeRateFailureLiveData = MutableLiveData<String>()
    val progressBarLiveData = MutableLiveData<Boolean>()
    val convertedCurrencies : MutableList<ConvertedExchnageRate> = mutableListOf()
    val convertedCurrenciesBackup : MutableList<ConvertedExchnageRate> = mutableListOf()
    val updatedRecyclerItemsList = MutableLiveData<MutableList<ConvertedExchnageRate>>()

    //TODO Use Dagger later

    fun getAvailableCurrencyList(model: CurrencyRepositoryModel) {
        progressBarLiveData.postValue(true)
        model.getCurrencyList(object :
            RequestCompleteListener<Currency> {
            override fun onRequestSuccess(data: Currency) {
                progressBarLiveData.postValue(false)
                currencyLiveData.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String) {
                progressBarLiveData.postValue(false)
                currencyFailureLiveData.postValue(errorMessage)
            }
        })
    }

    fun updateConvertedCurrenciesValues(
        amount: Double,
        selectedCurrency: String,
        model: CurrencyRepositoryModel
    ) {
        progressBarLiveData.postValue(true)

        var selectedExchangeRate: Double = 1.0
        run loop@{
            convertedCurrencies?.forEach {
                if (it.currencyName == "USD$selectedCurrency") {
                    selectedExchangeRate = it.exchangeRate
                    return@loop
                }
            }
        }
        convertedCurrencies?.forEach {
            it.convertedAmount = it.exchangeRate * amount/selectedExchangeRate
        }
        updatedRecyclerItemsList.postValue(convertedCurrencies)
        progressBarLiveData.postValue(false)
    }

    fun getAvailableExchangeRates(model: CurrencyRepositoryModel) {

        progressBarLiveData.postValue(true)
        exchangeRateFailureLiveData.postValue("Please wait... loading exchange rates..")
        model.getExchangeRates(object :
            RequestCompleteListener<ExchangeRate> {
            override fun onRequestSuccess(data: ExchangeRate) {
                progressBarLiveData.postValue(false)
                exchangeRateLiveData.postValue(data)
                val tempList : MutableList<ConvertedExchnageRate> = mutableListOf()
                data.exchangeRates.forEach {
                    tempList.add(ConvertedExchnageRate(0.0, it.currencyCode, it.rate))
                }
                convertedCurrencies.addAll(tempList)
                convertedCurrenciesBackup.addAll(tempList)
            }

            override fun onRequestFailed(errorMessage: String) {
                progressBarLiveData.postValue(false)
                exchangeRateFailureLiveData.postValue(errorMessage)
            }
        })
    }

    fun filterResults(
        searchKey: String
    ) {
        progressBarLiveData.postValue(true)
        convertedCurrencies.clear()
        if (searchKey.length>0){
            convertedCurrenciesBackup.forEach {
                if (it.currencyName.toLowerCase().contains(searchKey.toLowerCase())){
                    convertedCurrencies.add(it)
                }
            }
        } else{
            convertedCurrencies.addAll(convertedCurrenciesBackup)
        }
        updatedRecyclerItemsList.postValue(convertedCurrencies)
        progressBarLiveData.postValue(false)
    }
}
