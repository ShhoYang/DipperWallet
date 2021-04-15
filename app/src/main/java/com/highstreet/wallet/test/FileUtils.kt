package com.highstreet.wallet.test

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * @author Yang Shihao
 * @Date 3/30/21
 *
 * 让国际化string和中文string排序保持一致
 */

fun main() {
    val zh = "/Users/hao/Gaojie/Wallet/app/src/main/res/values/strings.xml"
    val en = "/Users/hao/Gaojie/Wallet/app/src/main/res/values-en/strings.xml"

    val listZh = ArrayList<String>()
    val mapEn = HashMap<String, String>()

    val fileInputStreamZh = FileInputStream(zh)
    val bufferedReaderZh = BufferedReader(InputStreamReader(fileInputStreamZh))

    val fileInputStreamEn = FileInputStream(en)
    val bufferedReaderEn = BufferedReader(InputStreamReader(fileInputStreamEn))

    var ss: String? = bufferedReaderZh.readLine()
    while (ss != null) {
        if (!ss.contains("resources>")) {
            listZh.add(cut(ss))
        }
        ss = bufferedReaderZh.readLine()
    }

    ss = bufferedReaderEn.readLine()
    while (ss != null) {
        if (ss != "" && !ss.contains("resources>")) {
            mapEn[cut(ss)] = ss
        }
        ss = bufferedReaderEn.readLine()
    }

    bufferedReaderEn.close()
    fileInputStreamEn.close()
    bufferedReaderZh.close()
    fileInputStreamZh.close()

    println("<resources>")

    listZh.forEach e@{
        if (it == "") {
            println()
            return@e
        }

        val s = mapEn[it] ?: ""
        println(s)
    }

    println("</resources>")
}

fun cut(origin: String): String {
    if (origin == null || origin == "") {
        return ""
    }

    val index = origin.indexOf("\">")
    if (index <= 0) {
        return ""
    }

    return origin.substring(0, index + 2)
}