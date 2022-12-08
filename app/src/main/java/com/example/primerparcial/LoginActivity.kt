package com.example.primerparcial

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.primerparcial.utils.LanguageConfig

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun attachBaseContext(newBase: Context?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(newBase!!)

        val lang = prefs.getString("language", "en")

        val context: Context = LanguageConfig.changeLanguage(newBase!!, lang!!)
        super.attachBaseContext(context)
    }
}