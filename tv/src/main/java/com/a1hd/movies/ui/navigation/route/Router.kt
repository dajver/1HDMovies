package com.a1hd.movies.ui.navigation.route

import androidx.fragment.app.Fragment
import com.a1hd.movies.ui.repository.MovieType
import com.a1hd.movies.ui.sections.dashboard.DashboardFragment
import com.a1hd.movies.ui.sections.SplashFragment
import com.a1hd.movies.ui.sections.movie.MovieDetailsFragment
import com.a1hd.movies.ui.sections.movie.watch.WatchMovieFragment

sealed class Router(val clearStack: Boolean = false) {
    object Splash : Router(clearStack = true)
    object Dashboard : Router(clearStack = true)
    class MovieDetails(val movieUrl: String?) : Router()
    class WatchMovie(val movieUrl: String?, val movieType: MovieType?) : Router()
}

fun Router.toFragment(): Fragment {
    return when (this) {
        Router.Splash -> SplashFragment()
        Router.Dashboard -> DashboardFragment()
        is Router.MovieDetails -> MovieDetailsFragment.newInstance(movieUrl)
        is Router.WatchMovie -> WatchMovieFragment.newInstance(movieUrl, movieType)
    }
}

fun Fragment.toRouter(): Router {
    return when (this) {
        is SplashFragment -> Router.Splash
        is DashboardFragment -> Router.Dashboard
        is MovieDetailsFragment -> Router.MovieDetails(movieUrl)
        is WatchMovieFragment -> Router.WatchMovie(movieUrl, movieType)
        else -> throw RuntimeException("Not found such fragment in router $this")
    }
}