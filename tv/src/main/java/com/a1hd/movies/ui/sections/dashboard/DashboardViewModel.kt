package com.a1hd.movies.ui.sections.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a1hd.movies.etc.extensions.launch
import com.a1hd.movies.ui.repository.MoviesDataModel
import com.a1hd.movies.ui.repository.ParseJsonDashboardRepository
import com.a1hd.movies.ui.repository.ParseJsonMoviesRepository
import com.a1hd.movies.ui.repository.ParseJsonTvShowsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val parseJsonDashboardRepository: ParseJsonDashboardRepository,
    private val parseJsonMoviesRepository: ParseJsonMoviesRepository,
    private val parseJsonTvShowsRepository: ParseJsonTvShowsRepository
): ViewModel() {

    private val fetchDashboardMoviesMutableLiveData = MutableLiveData<List<MoviesDataModel>>()
    val fetchDashboardMoviesLiveData: LiveData<List<MoviesDataModel>> = fetchDashboardMoviesMutableLiveData

    private val fetchMoviesMutableLiveData = MutableLiveData<List<MoviesDataModel>>()
    val fetchMoviesLiveData: LiveData<List<MoviesDataModel>> = fetchMoviesMutableLiveData

    private val fetchTvShowsMutableLiveData = MutableLiveData<List<MoviesDataModel>>()
    val fetchTvShowsLiveData: LiveData<List<MoviesDataModel>> = fetchTvShowsMutableLiveData

    fun fetchDashboard() = launch {
        val parseData = parseJsonDashboardRepository.fetchDashboard()
        fetchDashboardMoviesMutableLiveData.postValue(parseData)
    }

    fun fetchMovies() = launch {
        val parseData = parseJsonMoviesRepository.fetchMovies(page = 1)
        fetchMoviesMutableLiveData.postValue(parseData)
    }

    fun fetchTvShows() = launch {
        val parseData = parseJsonTvShowsRepository.fetchTvShows(page = 1)
        fetchTvShowsMutableLiveData.postValue(parseData)
    }
}