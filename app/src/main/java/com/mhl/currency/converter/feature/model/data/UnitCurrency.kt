package com.mhl.currency.converter.feature.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_currency_name")
data class UnitCurrency(@PrimaryKey @ColumnInfo(name = "currency_code") val currencyCode: String, @ColumnInfo(name = "currency_name") val currencyName: String)