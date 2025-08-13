package com.example.recipeapp.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object FavoritesStore {
    private lateinit var prefs: android.content.SharedPreferences

    private val _ids = MutableStateFlow<Set<Int>>(emptySet())
    val ids: StateFlow<Set<Int>> = _ids.asStateFlow()

    fun init(context: Context) {
        prefs = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
        val saved = prefs.getStringSet("favorites_ids", emptySet())?.map { it.toInt() }?.toSet() ?: emptySet()
        _ids.value = saved
    }

    fun isFavorite(id: Int): Boolean = _ids.value.contains(id)

    fun toggle(id: Int) {
        val s = _ids.value.toMutableSet()
        if (!s.add(id)) s.remove(id)
        _ids.value = s
        saveToPrefs(s)
    }

    private fun saveToPrefs(ids: Set<Int>) {
        prefs.edit().putStringSet("favorites_ids", ids.map { it.toString() }.toSet()).apply()
    }
}
