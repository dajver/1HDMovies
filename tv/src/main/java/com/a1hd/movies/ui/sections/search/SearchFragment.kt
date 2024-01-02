package com.a1hd.movies.ui.sections.search

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.a1hd.movies.databinding.FragmentSearchBinding
import com.a1hd.movies.etc.openKeyboard
import com.a1hd.movies.ui.base.BaseFragment
import com.a1hd.movies.ui.navigation.route.Router
import com.a1hd.movies.ui.sections.search.adapter.SearchResultRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment: BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    @Inject
    lateinit var searchResultRecyclerAdapter: SearchResultRecyclerAdapter

    private val searchViewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchResultRecyclerAdapter.onSearchResultClickListener = {
            navigationRouter.navigateTo(Router.MovieDetails(it.link))
        }
        binding.rvSearchResult.adapter = searchResultRecyclerAdapter

        searchViewModel.fetchSearchResultLiveData.observe(viewLifecycleOwner) {
            val tvShowsList = it.toMutableList()
            searchResultRecyclerAdapter.setSearchResult(tvShowsList)
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.etSearch.requestFocus()
        binding.etSearch.openKeyboard()
        binding.etSearch.addTextChangedListener {
            searchViewModel.search(it.toString())
        }
    }
}