package com.a1hd.movies.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.a1hd.movies.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

typealias Inflater<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseBottomSheetDialogFragment<VB: ViewBinding>(private val inflate: Inflater<VB>) : BottomSheetDialogFragment(), CoroutineScope by MainScope() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFullHeight()
    }

    private fun setupFullHeight() {
        val bottomSheetDialog = dialog as BottomSheetDialog
        val behavior = bottomSheetDialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet = (dialog as BottomSheetDialog?)?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
            val layoutParams = bottomSheet.layoutParams
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            bottomSheet.layoutParams = layoutParams
            bottomSheet.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.background_gradient_start
                )
            )
        }

        // Make the dialog cover the status bar
        requireDialog().window?.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }
}