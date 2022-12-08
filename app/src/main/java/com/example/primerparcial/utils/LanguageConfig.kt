package com.example.primerparcial.utils

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import java.util.*

class LanguageConfig {
    companion object {
        public fun changeLanguage(context : Context, languageCode : String) : ContextWrapper {
            val resources = context.resources
            val configuration = resources.configuration
            var systemLocale : Locale
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                systemLocale = configuration.locales.get(0);
            } else {
                systemLocale = configuration.locale;
            }
            if (languageCode != "" && systemLocale.language != languageCode) {
                val locale = Locale(languageCode)
                Locale.setDefault(locale)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    configuration.setLocale(locale)
                } else {
                    configuration.locale = locale
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    return ContextWrapper(context.createConfigurationContext(configuration))
                } else {
                    context.resources.updateConfiguration(
                        configuration,
                        context.resources.displayMetrics
                    )
                }
            }
            return ContextWrapper(context)
        }
    }
}