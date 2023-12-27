package com.a1hd.movies.ui.sections.alltvshows

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.a1hd.movies.databinding.FragmentAllTvShowsBinding
import com.a1hd.movies.ui.base.BaseFragment
import com.a1hd.movies.ui.navigation.route.Router
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
        allTvShowsRecyclerAdapter.onTvShowsClickListener = {
            navigationRouter.navigateTo(Router.MovieDetails(it.link))
        }
        binding.rvTvShows.adapter = allTvShowsRecyclerAdapter

        allTvShowsViewModel.fetchTvShows()
        allTvShowsViewModel.fetchTvShowsLiveData.observe(viewLifecycleOwner) {
            val tvShowsList = it.toMutableList()
            allTvShowsRecyclerAdapter.setTvSHows(tvShowsList)
        }
    }
}