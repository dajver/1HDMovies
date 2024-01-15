package com.a1hd.core.ui.sections.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a1hd.core.api.repository.MoviesDataModel
import com.a1hd.core.api.repository.ParseJsonSearchRepository
import com.a1hd.core.etc.extensions.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val parseJsonSearchRepository: ParseJsonSearchRepository
) : ViewModel() {

    private val fetchSearchResultMutableLiveData = MutableLiveData<List<MoviesDataModel>>()
    val fetchSearchResultLiveData: LiveData<List<MoviesDataModel>> = fetchSearchResultMutableLiveData

    fun search(keyword: String) = launch {
        val searchKeyword = keyword.replace(" ", "+")
        val searchResult = parseJsonSearchRepository.fetchSearchResult(searchKeyword)
        fetchSearchResultMutableLiveData.postValue(searchResult)
    }
}