package com.mhl.currency.converter.feature.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mhl.currency.converter.common.RequestCompleteListener
import com.mhl.currency.converter.feature.model.CurrencyConverterModel
import com.mhl.currency.converter.feature.model.data.ConvertedExchnageRate
import com.mhl.currency.converter.feature.model.data.Currency
import com.mhl.currency.converter.feature.model.data.ExchangeRate
import com.mhl.currency.converter.feature.model.data.UnitExchangeRate

//TODO this file is completely Android SDK dependency free
class CurrencyConverterViewModel : ViewModel() {
    val currencyLiveData = MutableLiveData<Currency>()
    val currencyFailureLiveData = MutableLiveData<String>()
    val exchangeRateLiveData = MutableLiveData<ExchangeRate>()
    val exchangeRateFailureLiveData = MutableLiveData<String>()
    val progressBarLiveData = MutableLiveData<Boolean>()
    val convertedCurrencies : MutableList<ConvertedExchnageRate> = mutableListOf()
    val updatedRecyclerItemsList = MutableLiveData<MutableList<ConvertedExchnageRate>>()

    //TODO Use Dagger to remove this model dependency and make this loosely coupled

    fun getAvailableCurrencyList(model: CurrencyConverterModel) {
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
        model: CurrencyConverterModel
    ) {
        progressBarLiveData.postValue(true)

        Log.d("LSN_ERR_ER", convertedCurrencies.toString())
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
            Log.d("LSN_ERR_EV", ""+it.exchangeRate + " "+amount+" "+selectedExchangeRate)
            it.convertedAmount = it.exchangeRate * amount/selectedExchangeRate
        }
        updatedRecyclerItemsList.postValue(convertedCurrencies)
        progressBarLiveData.postValue(false)
    }

    fun getAvailableExchangeRates(model: CurrencyConverterModel) {

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
            }

            override fun onRequestFailed(errorMessage: String) {
                progressBarLiveData.postValue(false)
                exchangeRateFailureLiveData.postValue(errorMessage)
            }
        })
    }
}
