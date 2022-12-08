package com.example.primerparcial

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.example.primerparcial.utils.LanguageConfig
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavView : BottomNavigationView
    private lateinit var navHostFragment : NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        bottomNavView = findViewById(R.id.bottomNavigationView)
        NavigationUI.setupWithNavController(bottomNavView, navHostFragment.navController)


    }

    override fun attachBaseContext(newBase: Context?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(newBase!!)
        val lang = prefs.getString("language", "en")

        val context: Context = LanguageConfig.changeLanguage(newBase!!, lang!!)
        super.attachBaseContext(context)
    }

}