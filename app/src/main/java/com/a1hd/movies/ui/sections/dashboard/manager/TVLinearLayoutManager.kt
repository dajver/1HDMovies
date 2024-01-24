package com.a1hd.movies.ui.sections.dashboard.manager

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager

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
}