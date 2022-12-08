package com.example.primerparcial.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.primerparcial.R

class SettingsFragment : PreferenceFragmentCompat() {
    lateinit var lang : String

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        lang = prefs.getString("language", "en")!!
    }

    override fun onDestroy() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        //Me fijo si cambio el idioma para recargar el activity
        if(lang != prefs.getString("language", "en")) {
            activity?.recreate()
        }
        super.onDestroy()
    }



}