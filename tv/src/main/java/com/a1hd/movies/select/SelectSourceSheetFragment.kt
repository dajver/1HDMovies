package com.a1hd.movies.select

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.a1hd.movies.base.BaseBottomSheetDialogFragment
import com.a1hd.movies.databinding.FragmentSelectSourceSheetBinding
import com.a1hd.movies.select.adapter.SelectSourceRecyclerAdapter

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
        binding.rvSelectSource.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    fun setSourceList(sourcesList: MutableList<String>) {
        selectSourceRecyclerAdapter.setSourceList(sourcesList)
    }
}