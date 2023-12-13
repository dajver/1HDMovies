package com.a1hd.movies.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

typealias Inflate<T> = (LayoutInflater) -> T

abstract class BaseActivity<VB: ViewBinding>(
    private val inflate: Inflate<VB>
) : AppCompatActivity(), CoroutineScope by MainScope() {

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate.invoke(layoutInflater)
        setContentView(binding.root)
    }
}