package com.example.collegeschedulemahotkinmobil.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.collegeschedulemahotkinmobil.data.dto.GroupDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoritesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun addFavorite(group: GroupDto) {
        val favorites = getFavorites().toMutableList()
        if (favorites.none { it.groupId == group.groupId }) {
            favorites.add(group)
            saveFavorites(favorites)
        }
    }

    fun removeFavorite(groupId: Int) {
        val favorites = getFavorites().filter { it.groupId != groupId }
        saveFavorites(favorites)
    }

    fun isFavorite(groupId: Int): Boolean {
        return getFavorites().any { it.groupId == groupId }
    }

    fun getFavorites(): List<GroupDto> {
        val json = prefs.getString("favorites", "[]")
        val type = object : TypeToken<List<GroupDto>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveFavorites(favorites: List<GroupDto>) {
        val json = gson.toJson(favorites)
        prefs.edit().putString("favorites", json).apply()
    }
}