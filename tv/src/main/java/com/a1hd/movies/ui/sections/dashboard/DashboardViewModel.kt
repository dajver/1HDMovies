package com.a1hd.movies.ui.sections.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a1hd.movies.etc.extensions.launch
import com.a1hd.movies.ui.repository.MoviesDataModel
import com.a1hd.movies.ui.repository.ParseJsonDashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val parseJsonDashboardRepository: ParseJsonDashboardRepository
): ViewModel() {

    private val fetchDashboardMoviesMutableLiveData = MutableLiveData<List<MoviesDataModel>>()
    val fetchDashboardMoviesLiveData: LiveData<List<MoviesDataModel>> = fetchDashboardMoviesMutableLiveData

    fun fetchDashboard() = launch {
        val parseData = parseJsonDashboardRepository.fetchDashboard()
        fetchDashboardMoviesMutableLiveData.postValue(parseData)
    }
}