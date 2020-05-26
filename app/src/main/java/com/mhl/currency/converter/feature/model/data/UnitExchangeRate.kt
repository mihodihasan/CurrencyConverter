package com.mhl.currency.converter.feature.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_exchange_rate")
data class UnitExchangeRate(@PrimaryKey @ColumnInfo(name = "currency_code") val currencyCode: String, @ColumnInfo(name = "currency_rate") var rate: Double)
