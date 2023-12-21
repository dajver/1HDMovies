package com.a1hd.movies.ui.sections.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.a1hd.movies.ui.base.BaseFragment
import com.a1hd.movies.databinding.FragmentDashboardBinding
import com.a1hd.movies.ui.sections.dashboard.adapter.DashboardRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment: BaseFragment<FragmentDashboardBinding>(FragmentDashboardBinding::inflate) {

    @Inject
    lateinit var dashboardRecyclerAdapter: DashboardRecyclerAdapter
    
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardRecyclerAdapter.onMovieClickListener = {

        }
        binding.rvMovies.adapter = dashboardRecyclerAdapter
        
        dashboardViewModel.fetchDashboard()
        dashboardViewModel.fetchDashboardMoviesLiveData.observe(viewLifecycleOwner) {
            dashboardRecyclerAdapter.setMovies(it.sortedBy { it.type }.toMutableList())
        }
    }
}