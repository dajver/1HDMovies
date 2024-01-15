package com.a1hd.core.ui.sections.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a1hd.core.api.repository.MoviesDetailsDataModel
import com.a1hd.core.etc.extensions.launch
import com.a1hd.core.ui.sections.favorite.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
): ViewModel() {

    private val fetchFavoritesMutableLiveData = MutableLiveData<MutableList<MoviesDetailsDataModel>>()
    val fetchFavoritesLiveData: LiveData<MutableList<MoviesDetailsDataModel>> = fetchFavoritesMutableLiveData

    fun fetchAllFavorites() = launch {
        val favorites = favoriteRepository.fetchAllFavorites()
        fetchFavoritesMutableLiveData.postValue(favorites)
    }
}