package com.mhl.currency.converter.feature.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.mhl.currency.converter.R
import com.mhl.currency.converter.feature.model.data.UnitCurrency
import kotlinx.android.synthetic.main.item_spinner_layout.view.*

class SpinnerCustomAdapter(private val context:Context, private val list:MutableList<UnitCurrency>):BaseAdapter(){

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_spinner_layout, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.fullCurrencyNameTv.text = list[position].currencyName
        vh.shortCurrencyNameTv.text = list[position].currencyCode

        return view
    }

    override fun getItem(position: Int): Any? {
        return list[position];
    }

    override fun getCount(): Int {
        return list.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(row: View?) {
        val fullCurrencyNameTv: TextView = row?.findViewById(R.id.full_currency_name_tv) as TextView
        val shortCurrencyNameTv: TextView = row?.findViewById(R.id.short_currency_name_tv) as TextView

    }

    /*override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_spinner_layout, parent, false)
        } else {
            view = convertView
            view.full_currency_name_tv.text = list[position].currencyName
            view.short_currency_name_tv.text = list[position].currencyCode
        }
        return view
    }

    override fun getItem(position: Int): UnitCurrency {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list.size
    }*/
}