package com.a1hd.core.ui.sections.genre

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a1hd.core.api.repository.GenresEnum
import com.a1hd.core.api.repository.MoviesDataModel
import com.a1hd.core.api.repository.ParseJsonMoviesGenresRepository
import com.a1hd.core.etc.extensions.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieByGenreViewModel @Inject constructor(
    private val parseJsonMoviesGenresRepository: ParseJsonMoviesGenresRepository
) : ViewModel() {

    private val fetchMoviesGenreMutableLiveData = MutableLiveData<List<MoviesDataModel>>()
    val fetchMoviesGenreLiveData: LiveData<List<MoviesDataModel>> = fetchMoviesGenreMutableLiveData

    var currentPage = 1
    private var moviesGenreList = emptyList<MoviesDataModel>()

    fun fetchMoviesByGenre(genre: GenresEnum) = launch {
        if (moviesGenreList.isEmpty()) {
            moviesGenreList = parseJsonMoviesGenresRepository.fetchMoviesByGenre(genre = genre, page = currentPage)
        }
        fetchMoviesGenreMutableLiveData.postValue(moviesGenreList)
    }

    fun fetchPaginationMoviesByGenre(genre: GenresEnum) = launch {
        moviesGenreList = parseJsonMoviesGenresRepository.fetchMoviesByGenre(genre = genre, page = currentPage)
        fetchMoviesGenreMutableLiveData.postValue(moviesGenreList)
    }
}