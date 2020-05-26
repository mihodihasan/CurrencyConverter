package com.mhl.currency.converter.feature.model.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mhl.currency.converter.feature.model.data.UnitCurrency
import com.mhl.currency.converter.feature.model.data.UnitExchangeRate

@Dao
interface CurrencyDAO {

    @Query("SELECT * from tb_currency_name")
    suspend fun getCurrencyNames(): MutableList<UnitCurrency>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currency: UnitCurrency)

    @Query("DELETE FROM tb_currency_name")
    suspend fun deleteAllCurrencies()

    @Query("SELECT * from tb_exchange_rate")
    suspend fun getExchangeRates(): MutableList<UnitExchangeRate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchangeRate: UnitExchangeRate)

    @Query("DELETE FROM tb_exchange_rate")
    suspend fun deleteAllExchangeRates()
}