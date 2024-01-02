package com.a1hd.movies.ui.sections.alltvshows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a1hd.movies.etc.extensions.launch
import com.a1hd.movies.api.repository.MoviesDataModel
import com.a1hd.movies.api.repository.ParseJsonTvShowsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllTvShowsViewModel @Inject constructor(
    private val parseJsonTvShowsRepository: ParseJsonTvShowsRepository
): ViewModel() {

    private val fetchTvShowsMutableLiveData = MutableLiveData<List<MoviesDataModel>>()
    val fetchTvShowsLiveData: LiveData<List<MoviesDataModel>> = fetchTvShowsMutableLiveData

    var currentPage = 1

    fun fetchTvShows() = launch {
        val parseData = parseJsonTvShowsRepository.fetchTvShows(page = currentPage)
        fetchTvShowsMutableLiveData.postValue(parseData)
    }
}