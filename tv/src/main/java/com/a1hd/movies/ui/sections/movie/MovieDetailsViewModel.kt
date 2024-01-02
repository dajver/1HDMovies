package com.a1hd.movies.ui.sections.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a1hd.movies.etc.extensions.launch
import com.a1hd.movies.api.repository.MoviesDetailsDataModel
import com.a1hd.movies.api.repository.ParseJsonMovieDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val parseJsonMovieDetailsRepository: ParseJsonMovieDetailsRepository
): ViewModel() {

    private val fetchDetailsMoviesMutableLiveData = MutableLiveData<MoviesDetailsDataModel>()
    val fetchDetailsMoviesLiveData: LiveData<MoviesDetailsDataModel> = fetchDetailsMoviesMutableLiveData

    private var moviesDetailsDataModel: MoviesDetailsDataModel? = null

    fun fetchDetails(html: String) = launch {
        if (moviesDetailsDataModel == null) {
            moviesDetailsDataModel = parseJsonMovieDetailsRepository.fetchDetails(html)
        }

        val seasons = moviesDetailsDataModel?.seasonsList
        if (!seasons.isNullOrEmpty()) {
            if (seasons.firstOrNull { it.isSelected } != null ) return@launch

            seasons.onEach { it.isSelected = false }
            seasons.lastOrNull()?.isSelected = true

            val episodes = seasons.lastOrNull()?.episodes
            if (!episodes.isNullOrEmpty()) {
                episodes.onEach { it.isSelected = false }
                episodes.firstOrNull()?.isSelected = true
            }
        }

        fetchDetailsMoviesMutableLiveData.postValue(moviesDetailsDataModel!!)
    }
}