package com.highstreet.lib.utils

import com.socks.library.KLog

/**
 * @author Yang Shihao
 */
object L {

    fun d(tag: String, msg: String) {
        KLog.d(tag, msg)
    }

    fun e(tag: String, msg: String) {
        KLog.e(tag, msg)
    }

    fun json(tag: String, msg: String) {
        KLog.json(tag, msg)
    }
}