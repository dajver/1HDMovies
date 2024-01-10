package com.a1hd.movies.ui.navigation.route

import androidx.fragment.app.Fragment
import com.a1hd.movies.api.repository.GenresEnum
import com.a1hd.movies.ui.sections.dashboard.DashboardFragment
import com.a1hd.movies.ui.sections.SplashFragment
import com.a1hd.movies.ui.sections.allmovies.AllMoviesFragment
import com.a1hd.movies.ui.sections.alltvshows.AllTvShowsFragment
import com.a1hd.movies.ui.sections.favorite.FavoriteFragment
import com.a1hd.movies.ui.sections.genre.MovieByGenreFragment
import com.a1hd.movies.ui.sections.movie.MovieDetailsFragment
import com.a1hd.movies.ui.sections.movie.watch.WatchMovieFragment
import com.a1hd.movies.ui.sections.search.SearchFragment

sealed class Router(val clearStack: Boolean = false) {
    object Splash : Router(clearStack = true)
    object Dashboard : Router(clearStack = true)
    class MovieDetails(val movieUrl: String?) : Router()
    class WatchMovie(val movieUrl: String?) : Router()
    object AllMovies : Router()
    object AllTvShows : Router()
    class MovieByGenre(val movieGenre: GenresEnum?) : Router()
    object Search : Router()
    object Favorites : Router()
}

fun Router.toFragment(): Fragment {
    return when (this) {
        Router.Splash -> SplashFragment()
        Router.Dashboard -> DashboardFragment()
        is Router.MovieDetails -> MovieDetailsFragment.newInstance(movieUrl)
        is Router.WatchMovie -> WatchMovieFragment.newInstance(movieUrl)
        Router.AllMovies -> AllMoviesFragment()
        Router.AllTvShows -> AllTvShowsFragment()
        is Router.MovieByGenre -> MovieByGenreFragment.newInstance(movieGenre)
        Router.Search -> SearchFragment()
        Router.Favorites -> FavoriteFragment()
    }
}

fun Fragment.toRouter(): Router {
    return when (this) {
        is SplashFragment -> Router.Splash
        is DashboardFragment -> Router.Dashboard
        is MovieDetailsFragment -> Router.MovieDetails(movieUrl)
        is WatchMovieFragment -> Router.WatchMovie(movieUrl)
        is AllMoviesFragment -> Router.AllMovies
        is AllTvShowsFragment -> Router.AllTvShows
        is MovieByGenreFragment -> Router.MovieByGenre(movieGenre)
        is SearchFragment -> Router.Search
        is FavoriteFragment -> Router.Favorites
        else -> throw RuntimeException("Not found such fragment in router $this")
    }
}