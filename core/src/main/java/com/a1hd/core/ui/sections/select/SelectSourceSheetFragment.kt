package com.a1hd.core.ui.sections.select

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.a1hd.core.R
import com.a1hd.core.databinding.FragmentSelectSourceSheetBinding
import com.a1hd.core.ui.base.BaseBottomSheetDialogFragment
import com.a1hd.core.ui.sections.select.adapter.SelectSourceRecyclerAdapter

class SelectSourceSheetFragment : BaseBottomSheetDialogFragment<FragmentSelectSourceSheetBinding>(FragmentSelectSourceSheetBinding::inflate) {

    private val selectSourceRecyclerAdapter: SelectSourceRecyclerAdapter by lazy {
        SelectSourceRecyclerAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectSourceRecyclerAdapter.onSourceClickListener = {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(it)
            startActivity(intent)
        }
        binding.rvSelectSource.adapter = selectSourceRecyclerAdapter
        val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider_color)!!)
        binding.rvSelectSource.addItemDecoration(divider)
    }

    fun setSourceList(sourcesList: MutableList<String>) {
        selectSourceRecyclerAdapter.setSourceList(sourcesList)
    }
}