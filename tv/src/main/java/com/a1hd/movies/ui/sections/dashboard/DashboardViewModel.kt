package com.a1hd.movies.ui.sections.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a1hd.movies.etc.extensions.launch
import com.a1hd.movies.api.repository.MostPopularMoviesDataModel
import com.a1hd.movies.api.repository.MoviesDataModel
import com.a1hd.movies.api.repository.ParseJsonDashboardRepository
import com.a1hd.movies.api.repository.ParseJsonMostPopularRepository
import com.a1hd.movies.api.repository.ParseJsonMoviesRepository
import com.a1hd.movies.api.repository.ParseJsonTvShowsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val parseJsonDashboardRepository: ParseJsonDashboardRepository,
    private val parseJsonMoviesRepository: ParseJsonMoviesRepository,
    private val parseJsonTvShowsRepository: ParseJsonTvShowsRepository,
    private val parseJsonMostPopularRepository: ParseJsonMostPopularRepository
): ViewModel() {

    private val fetchMostPopularMutableLiveData = MutableLiveData<List<MostPopularMoviesDataModel>>()
    val fetchMostPopularLiveData: LiveData<List<MostPopularMoviesDataModel>> = fetchMostPopularMutableLiveData

    private val fetchDashboardMoviesMutableLiveData = MutableLiveData<List<MoviesDataModel>>()
    val fetchDashboardMoviesLiveData: LiveData<List<MoviesDataModel>> = fetchDashboardMoviesMutableLiveData

    private val fetchMoviesMutableLiveData = MutableLiveData<List<MoviesDataModel>>()
    val fetchMoviesLiveData: LiveData<List<MoviesDataModel>> = fetchMoviesMutableLiveData

    private val fetchTvShowsMutableLiveData = MutableLiveData<List<MoviesDataModel>>()
    val fetchTvShowsLiveData: LiveData<List<MoviesDataModel>> = fetchTvShowsMutableLiveData

    private var mostPopularMovies = emptyList<MostPopularMoviesDataModel>()
    private var dashboardMovies = emptyList<MoviesDataModel>()
    private var allMoviesList = emptyList<MoviesDataModel>()
    private var allTvShowsList = emptyList<MoviesDataModel>()


    fun fetchMostPopular() = launch {
        if (mostPopularMovies.isEmpty()) {
            mostPopularMovies = parseJsonMostPopularRepository.fetchMostPopular()
        }
        fetchMostPopularMutableLiveData.postValue(mostPopularMovies)
    }

    fun fetchDashboard() = launch {
        if (dashboardMovies.isEmpty()) {
            dashboardMovies = parseJsonDashboardRepository.fetchDashboard()
        }
        fetchDashboardMoviesMutableLiveData.postValue(dashboardMovies)
    }

    fun fetchMovies() = launch {
        if (allMoviesList.isEmpty()) {
            allMoviesList = parseJsonMoviesRepository.fetchMovies(page = 1)
        }
        fetchMoviesMutableLiveData.postValue(allMoviesList)
    }

    fun fetchTvShows() = launch {
        if (allTvShowsList.isEmpty()) {
            allTvShowsList = parseJsonTvShowsRepository.fetchTvShows(page = 1)
        }
        fetchTvShowsMutableLiveData.postValue(allTvShowsList)
    }
}