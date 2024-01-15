package com.a1hd.core.ui.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.a1hd.core.R
import com.a1hd.core.ui.sections.dashboard.DashboardFragment
import com.a1hd.core.ui.navigation.route.Router
import com.a1hd.core.ui.navigation.route.toFragment
import com.a1hd.core.ui.navigation.route.toRouter
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

private const val CURRENT_SCREEN_OFFSET = 1
private const val PREVIOUS_SCREENS_OFFSET = 2
@ActivityScoped
class NavigationRouter @Inject constructor() {

    private lateinit var supportFragmentManager: FragmentManager
    var navigationChangeListener: (route: Router?) -> Unit = { }

    fun init(supportFragmentManager: FragmentManager) {
        this.supportFragmentManager = supportFragmentManager
    }

    fun navigateTo(route: Router) {
        navigate(route)
    }

    private fun navigate(route: Router) {
        if (route.clearStack) {
            for (i in 0 until supportFragmentManager.backStackEntryCount) {
                val backEntry = supportFragmentManager.getBackStackEntryAt(if (i < 0) 0 else i)
                val fragment = supportFragmentManager.findFragmentByTag(backEntry.name)
                if (fragment?.toRouter() != null && fragment !is DashboardFragment) {
                    supportFragmentManager.popBackStack()
                }
            }
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, route.toFragment(), route.getName())
        transaction.addToBackStack(route.getName())
        transaction.commitAllowingStateLoss()

        navigationChangeListener.invoke(route)
    }

    fun navigateBack() {
        onBackPressed()
    }

    fun onBackPressed(): Boolean {
        return if (supportFragmentManager.backStackEntryCount >= CURRENT_SCREEN_OFFSET) {
            val isLastScreen = if (getCurrentBackStackFragment(CURRENT_SCREEN_OFFSET) is DashboardFragment) {
                supportFragmentManager.backStackEntryCount - CURRENT_SCREEN_OFFSET == 0
            } else {
                supportFragmentManager.backStackEntryCount - 1 == 0
            }

            if (isLastScreen) {
                true
            } else {
                val router = getCurrentBackStackFragment(PREVIOUS_SCREENS_OFFSET)?.toRouter()
                navigationChangeListener.invoke(router)
                supportFragmentManager.popBackStack()
                false
            }
        } else {
            true
        }
    }

    private fun getCurrentBackStackFragment(screensCount: Int? = CURRENT_SCREEN_OFFSET): Fragment? {
        val screenCountFixed = screensCount ?: 0
        val index = supportFragmentManager.backStackEntryCount - screenCountFixed
        return if (index >= 0) {
            try {
                val backEntry = supportFragmentManager.getBackStackEntryAt(if (index < 0) 0 else index)
                supportFragmentManager.findFragmentByTag(backEntry.name)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }
}

fun Router.getName(): String {
    return this.toString().split("$")[1].split("@")[0]
}