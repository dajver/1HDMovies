package com.a1hd.movies.etc.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun ViewModel.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return viewModelScope.launch(context, start, block)
}

/**
 * Executes suspending block using IO dispatcher
 */
suspend fun <T> io(
    block: suspend CoroutineScope.() -> T
): T = withContext(
    Dispatchers.IO,
    block
)

/**
 * Executes suspending block using Default dispatcher
 */
suspend fun <T> default(
    block: suspend CoroutineScope.() -> T
): T = withContext(
    Dispatchers.Default,
    block
)

/**
 * Executes suspending block using Main dispatcher
 */
suspend fun <T> main(
    block: suspend CoroutineScope.() -> T
): T = withContext(
    Dispatchers.Main.immediate,
    block
)