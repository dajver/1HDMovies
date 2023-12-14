package com.a1hd.movies.etc

import android.content.SharedPreferences
import com.a1hd.movies.etc.extensions.string
import javax.inject.Inject
import javax.inject.Singleton

private const val LAST_OPENED_PAGE_LIST = "LAST_OPENED_PAGE_LIST"

@Singleton
class LastOpenedScreenRepository @Inject constructor(
    prefs: SharedPreferences
) {

    var lastOpenedPage: String by prefs.string(LAST_OPENED_PAGE_LIST, "https://1hd.to/home")

}