package com.konchak.cnc_halper.core.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import java.util.Locale

object LocaleHelper {

    private const val SELECTED_LANGUAGE = "Locale.Helper.SelectedLanguage"

    fun setLocale(context: Context, language: String): Context {
        persist(context, language)
        return updateResources(context, language)
    }

    fun getPersistedLocale(context: Context): String {
        val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return preferences.getString(SELECTED_LANGUAGE, "en") ?: "en" // Default to English
    }

    private fun persist(context: Context, language: String) {
        val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        preferences.edit().putString(SELECTED_LANGUAGE, language).apply()
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocales(LocaleList(locale))
            context.createConfigurationContext(configuration)
        } else {
            @Suppress("DEPRECATION")
            configuration.locale = locale
            @Suppress("DEPRECATION")
            resources.updateConfiguration(configuration, resources.displayMetrics)
            context
        }
    }

    fun onAttach(context: Context): Context {
        val lang = getPersistedLocale(context)
        return setLocale(context, lang)
    }
}
