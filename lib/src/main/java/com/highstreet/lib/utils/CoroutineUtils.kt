package com.highstreet.lib.utils

import kotlinx.coroutines.*

/**
 * @author Yang Shihao
 * @Date 12/17/20
 */
object CoroutineUtils {

    fun io(block: suspend CoroutineScope.() -> Unit) {
        GlobalScope.launch(Dispatchers.IO, block = block)
    }

    fun <T> io2main(
        ioBlock: () -> T,
        mainBlock: (T) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = ioBlock()
            withContext(Dispatchers.Main) {
                mainBlock(result)
            }
        }
    }

    fun <T> io2main2(
        ioBlock: suspend CoroutineScope.() -> T,
        mainBlock: (T) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val result = io(ioBlock)
            mainBlock(result)
        }
    }

    private suspend fun <T> io(block: suspend CoroutineScope.() -> T): T =
        withContext(Dispatchers.IO, block)
}
