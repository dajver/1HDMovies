package com.a1hd.movies.ui.sections.alltvshows

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.a1hd.movies.databinding.FragmentAllTvShowsBinding
import com.a1hd.movies.ui.base.BaseFragment
import com.a1hd.movies.ui.navigation.route.Router
import com.a1hd.movies.ui.sections.allmovies.listener.PaginationScrollListener
import com.a1hd.movies.ui.sections.alltvshows.adapter.AllTvShowsRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AllTvShowsFragment: BaseFragment<FragmentAllTvShowsBinding>(FragmentAllTvShowsBinding::inflate) {

    @Inject
    lateinit var allTvShowsRecyclerAdapter: AllTvShowsRecyclerAdapter

    private val allTvShowsViewModel: AllTvShowsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = binding.rvTvShows.layoutManager as GridLayoutManager
        val paginationListener = object : PaginationScrollListener(layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                allTvShowsViewModel.currentPage += 1
                allTvShowsViewModel.fetchTvShows()
            }
        }
        allTvShowsRecyclerAdapter.onTvShowsClickListener = {
            navigationRouter.navigateTo(Router.MovieDetails(it.link))
        }
        binding.rvTvShows.adapter = allTvShowsRecyclerAdapter
        binding.rvTvShows.addOnScrollListener(paginationListener)

        allTvShowsViewModel.fetchTvShows()
        allTvShowsViewModel.fetchTvShowsLiveData.observe(viewLifecycleOwner) {
            val tvShowsList = it.toMutableList()
            allTvShowsRecyclerAdapter.setTvSHows(tvShowsList)
        }
    }
}