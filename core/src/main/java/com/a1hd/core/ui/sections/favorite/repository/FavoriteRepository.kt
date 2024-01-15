package com.a1hd.core.ui.sections.favorite.repository

import android.content.SharedPreferences
import com.a1hd.core.api.repository.MoviesDetailsDataModel
import com.a1hd.core.etc.extensions.mutableList
import com.google.gson.Gson
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

private const val FAVORITE_LIST = "FAVORITE_LIST"

@Singleton
class FavoriteRepository @Inject constructor(prefs: SharedPreferences, gson: Gson) {

    private var favorites: MutableList<MoviesDetailsDataModel> by prefs.mutableList(FAVORITE_LIST, gson, "")

    fun fetchAllFavorites(): MutableList<MoviesDetailsDataModel> {
        return favorites.sortedByDescending { it.addedAt }.toMutableList()
    }

    fun favorite(movie: MoviesDetailsDataModel) {
        if (hasMovie(movie)) {
            remove(movie)
        } else {
            save(movie)
        }
    }

    fun hasMovie(movie: MoviesDetailsDataModel): Boolean {
        return favorites.contains(movie)
    }

    private fun save(movie: MoviesDetailsDataModel) {
        val favoritesList = mutableListOf<MoviesDetailsDataModel>()
        favoritesList.addAll(favorites)
        movie.addedAt = Date().time
        favoritesList.add(movie)
        favorites = favoritesList
    }

    private fun remove(movie: MoviesDetailsDataModel) {
        val favoritesList = mutableListOf<MoviesDetailsDataModel>()
        favoritesList.addAll(favorites)
        favoritesList.remove(movie)
        favorites = favoritesList
    }
}