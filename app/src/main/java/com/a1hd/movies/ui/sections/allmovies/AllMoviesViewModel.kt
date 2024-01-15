package com.a1hd.movies.ui.sections.allmovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a1hd.movies.etc.extensions.launch
import com.a1hd.movies.api.repository.MoviesDataModel
import com.a1hd.movies.api.repository.ParseJsonMoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllMoviesViewModel @Inject constructor(
    private val parseJsonMoviesRepository: ParseJsonMoviesRepository
): ViewModel() {

    private val fetchMoviesMutableLiveData = MutableLiveData<List<MoviesDataModel>>()
    val fetchMoviesLiveData: LiveData<List<MoviesDataModel>> = fetchMoviesMutableLiveData

    var currentPage = 1
    private var allMoviesList = emptyList<MoviesDataModel>()

    fun fetchMovies() = launch {
        if (allMoviesList.isEmpty()) {
            allMoviesList = parseJsonMoviesRepository.fetchMovies(page = currentPage)
        }
        fetchMoviesMutableLiveData.postValue(allMoviesList)
    }

    fun fetchPaginationMovies() = launch {
        allMoviesList = parseJsonMoviesRepository.fetchMovies(page = currentPage)
        fetchMoviesMutableLiveData.postValue(allMoviesList)
    }
}