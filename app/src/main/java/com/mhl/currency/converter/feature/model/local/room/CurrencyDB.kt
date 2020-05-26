package com.mhl.currency.converter.feature.model.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mhl.currency.converter.feature.model.data.Currency
import com.mhl.currency.converter.feature.model.data.UnitCurrency
import com.mhl.currency.converter.feature.model.data.UnitExchangeRate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@Database(entities = arrayOf(UnitExchangeRate::class, UnitCurrency::class), version = 1, exportSchema = false)
abstract class CurrencyDB : RoomDatabase() {

    abstract fun currencyDao():CurrencyDAO

    companion object{
        @Volatile
        private var INSTANCE : CurrencyDB? = null
        fun getDatabase(context: Context, scope: CoroutineScope): CurrencyDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CurrencyDB::class.java,
                    "word_database"
                )
//                    .addCallback(CurrencyDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class CurrencyDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.currencyDao())
                }
            }
        }

        suspend fun populateDatabase(dao: CurrencyDAO) {
            // Delete all content here.
            dao.deleteAllCurrencies()
            dao.deleteAllExchangeRates()

            // Add sample words.
            var currency = UnitCurrency("BDT","Bangladeshi Taka")
            dao.insert(currency)

        }
    }
}