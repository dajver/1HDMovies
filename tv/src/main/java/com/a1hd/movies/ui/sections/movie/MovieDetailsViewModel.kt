package com.a1hd.movies.ui.sections.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a1hd.movies.etc.extensions.launch
import com.a1hd.movies.ui.repository.MoviesDetailsDataModel
import com.a1hd.movies.ui.repository.ParseJsonMovieDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val parseJsonMovieDetailsRepository: ParseJsonMovieDetailsRepository
): ViewModel() {

    private val fetchDetailsMoviesMutableLiveData = MutableLiveData<MoviesDetailsDataModel>()
    val fetchDetailsMoviesLiveData: LiveData<MoviesDetailsDataModel> = fetchDetailsMoviesMutableLiveData

    fun fetchDetails(html: String) = launch {
        val details = parseJsonMovieDetailsRepository.fetchDetails(html)
        fetchDetailsMoviesMutableLiveData.postValue(details)
    }
}