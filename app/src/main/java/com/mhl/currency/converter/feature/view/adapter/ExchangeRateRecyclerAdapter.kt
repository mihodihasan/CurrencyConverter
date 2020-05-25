package com.mhl.currency.converter.feature.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mhl.currency.converter.R
import com.mhl.currency.converter.feature.model.data.ConvertedExchnageRate
import kotlinx.android.synthetic.main.item_search_result_layout.view.*

class ExchangeRateRecyclerAdapter(
    private val context: Context,
    var list: MutableList<ConvertedExchnageRate>
) : RecyclerView.Adapter<ExchangeRateRecyclerAdapter.MyExchangeRateVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyExchangeRateVH {
        return MyExchangeRateVH(
            LayoutInflater.from(context).inflate(R.layout.item_search_result_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyExchangeRateVH, position: Int) {
        holder.itemView.converted_tv.text = list[position].convertedAmount.toString()
        holder.itemView.currency_name.text = list[position].currencyName
        holder.itemView.exchange_rate.text = list[position].exchangeRate.toString()
    }

    class MyExchangeRateVH(itemView: View) : RecyclerView.ViewHolder(itemView)
}