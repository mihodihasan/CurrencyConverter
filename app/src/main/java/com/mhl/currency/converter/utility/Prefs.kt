package com.mhl.currency.converter.utility

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    lateinit var context: Context
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    init {
        this.context=context
        sharedPreferences =
            context.getSharedPreferences("CURRENCY_SHARED_PREF", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun saveCurrentTimeToSharedPref(key:String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun getCurrentTimeFromSharedPref(key : String): String? {
        return sharedPreferences.getString(key, null)
    }
}