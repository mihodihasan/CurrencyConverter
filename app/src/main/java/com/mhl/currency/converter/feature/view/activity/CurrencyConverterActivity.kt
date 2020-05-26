package com.mhl.currency.converter.feature.view.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhl.currency.converter.R
import com.mhl.currency.converter.feature.model.CurrencyRepositoryModel
import com.mhl.currency.converter.feature.model.CurrencyRepositoryModelImpl
import com.mhl.currency.converter.feature.model.data.ConvertedExchnageRate
import com.mhl.currency.converter.feature.model.data.UnitCurrency
import com.mhl.currency.converter.feature.view.adapter.ExchangeRateRecyclerAdapter
import com.mhl.currency.converter.feature.view.adapter.SpinnerCustomAdapter
import com.mhl.currency.converter.feature.viewmodel.CurrencyConverterViewModel
import com.mhl.currency.converter.feature.viewmodel.RoomViewModel
import kotlinx.android.synthetic.main.activity_main.*

class CurrencyConverterActivity : AppCompatActivity() {
    private lateinit var roomViewModel: RoomViewModel
    lateinit var spinnerAdapter: SpinnerCustomAdapter
    lateinit var recyclerAdapter: ExchangeRateRecyclerAdapter
    private lateinit var currencyList: MutableList<UnitCurrency>
    private lateinit var convertedValues: MutableList<ConvertedExchnageRate>
    var spinnerSelectedPosition: Int = 0

    private lateinit var model: CurrencyRepositoryModel
    private lateinit var viewModel: CurrencyConverterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        roomViewModel = ViewModelProvider(this).get(RoomViewModel::class.java)
        model = CurrencyRepositoryModelImpl(applicationContext, roomViewModel)
        viewModel = ViewModelProvider(this).get(CurrencyConverterViewModel::class.java)

        setUi()
        setLiveDataListeners()

        viewModel.getAvailableCurrencyList(model)
        viewModel.getAvailableExchangeRates(model)
    }

    private fun setLiveDataListeners() {
        viewModel.currencyLiveData.observe(this,
            Observer {
                currencyList.clear()
                /*it.currencies.forEach { v ->
                    currencyList.add(v.currencyName)
                }*/
                currencyList.addAll(it.currencies)
                spinnerAdapter.notifyDataSetChanged()
            })
        viewModel.currencyFailureLiveData.observe(this,
            Observer {
                error_tv.text = it
                error_tv.visibility = VISIBLE
                search_results_recycler.visibility = GONE
            })
        viewModel.exchangeRateLiveData.observe(this,
            Observer {
                search_results_recycler.visibility= VISIBLE
                error_tv.visibility= GONE
                last_updated_tv.text="Rates Last Updated On "+it.lastUpdated
            })
        viewModel.exchangeRateFailureLiveData.observe(this,
            Observer {
                search_results_recycler.visibility= GONE
                error_tv.text=it
                error_tv.visibility= VISIBLE
            })
        viewModel.progressBarLiveData.observe(this,
            Observer {
                if (it) showLoading()
                else hideLoading()
            })

        viewModel.updatedRecyclerItemsList.observe(this, Observer {
            convertedValues.clear()
            if (it != null) convertedValues.addAll(it)
            recyclerAdapter.notifyDataSetChanged()
        })
    }

    private fun setUi() {
        //prepare spinner
        currencyList = mutableListOf()
        spinnerAdapter = SpinnerCustomAdapter(
            this, currencyList
        )
        selected_currency_spinner.adapter = spinnerAdapter
        selected_currency_spinner.setSelection(0)
        selected_currency_spinner?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerSelectedPosition = position
                viewModel.currencyLiveData.value?.currencies?.get(position)?.currencyCode?.let {
                    var input: Double = 0.0
                    if (input_number_et.text.toString() != null && input_number_et.text.toString() != "") {
                        input = input_number_et.text.toString().toDouble()
                    }
                    viewModel.updateConvertedCurrenciesValues(
                        input,
                        it, model
                    )
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        //prepare recycler
        convertedValues = mutableListOf()
        recyclerAdapter = ExchangeRateRecyclerAdapter(this, convertedValues)
        search_results_recycler.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = recyclerAdapter
        }

        input_number_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(input_number_et.text.toString()!=null && input_number_et.text.toString()!="")
                viewModel.updateConvertedCurrenciesValues(input_number_et.text.toString().toDouble(), currencyList[spinnerSelectedPosition].currencyCode, model)
                else viewModel.updateConvertedCurrenciesValues(0.0, currencyList[spinnerSelectedPosition].currencyCode, model)
            }
        })

        filter_search_result_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.filterResults(p0.toString())
            }
        })

    }

    private fun showLoading() {
        loading_progress.visibility = VISIBLE
    }

    private fun hideLoading() {
        loading_progress.visibility = GONE
    }


}
