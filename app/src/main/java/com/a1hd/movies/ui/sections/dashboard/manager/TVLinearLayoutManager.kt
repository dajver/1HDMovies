package com.a1hd.movies.ui.sections.dashboard.manager

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class TVLinearLayoutManager : LinearLayoutManager {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)

    override fun onInterceptFocusSearch(focused: View, direction: Int): View? {
        if (direction == View.FOCUS_RIGHT) {
            val pos = getPosition(focused)
            if (pos == itemCount - 1)
                return focused
        }
        return super.onInterceptFocusSearch(focused, direction)
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        val centerSmoothScroller = CenterSmoothScroller(recyclerView.context)
        centerSmoothScroller.targetPosition = position
        startSmoothScroll(centerSmoothScroller)
    }
}
class CenterSmoothScroller(context: Context) : LinearSmoothScroller(context) {

    override fun calculateDtToFit(
        viewStart: Int,
        viewEnd: Int,
        boxStart: Int,
        boxEnd: Int,
        snapPreference: Int
    ): Int = (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)

    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
        return MILLISECONDS_PER_INCH / displayMetrics.densityDpi
    }

    companion object {
        private const val MILLISECONDS_PER_INCH = 150f
    }
}