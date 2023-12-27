package com.a1hd.movies.ui.sections.allmovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a1hd.movies.etc.extensions.launch
import com.a1hd.movies.ui.repository.MoviesDataModel
import com.a1hd.movies.ui.repository.ParseJsonMoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllMoviesViewModel @Inject constructor(
    private val parseJsonMoviesRepository: ParseJsonMoviesRepository
): ViewModel() {

    private val fetchMoviesMutableLiveData = MutableLiveData<List<MoviesDataModel>>()
    val fetchMoviesLiveData: LiveData<List<MoviesDataModel>> = fetchMoviesMutableLiveData

    var currentPage = 1

    fun fetchMovies() = launch {
        val parseData = parseJsonMoviesRepository.fetchMovies(page = currentPage)
        fetchMoviesMutableLiveData.postValue(parseData)
    }
}