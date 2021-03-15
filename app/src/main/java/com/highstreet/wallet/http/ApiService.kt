package com.highstreet.wallet.http

import com.hao.library.utils.L
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.constant.Chain
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
object ApiService {

    private const val BASE_URL_MAIN = "https://rpc.dippernetwork.com/"
    private const val BASE_URL_TEST = "https://rpc.testnet.dippernetwork.com/"

    fun getApi(chain: Chain? = null): DipApi {
        return when (chain ?: Chain.getChain(AccountManager.instance().chain)) {
            Chain.DIP_MAIN, Chain.DIP_MAIN2 -> getDipperMainApi()
            else -> getDipperTestApi()
        }
    }

    private var dipperTestApi: DipApi? = null
    private fun getDipperTestApi(): DipApi {
        if (dipperTestApi == null) {
            synchronized(ApiService::class.java) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL_TEST)
                    .client(getOkHttp())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                dipperTestApi = retrofit.create(DipApi::class.java)
            }
        }
        return dipperTestApi!!
    }

    private var dipperMainApi: DipApi? = null
    private fun getDipperMainApi(): DipApi {
        if (dipperMainApi == null) {
            synchronized(ApiService::class.java) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL_MAIN)
                    .client(getOkHttp())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                dipperMainApi = retrofit.create(DipApi::class.java)
            }
        }
        return dipperMainApi!!
    }

    private fun getOkHttp(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                L.json("json__", message)
            }

        })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addNetworkInterceptor(NetInterceptor())
            .retryOnConnectionFailure(true)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }
}

class NetInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            .addHeader("Connection", "close")
            .addHeader("accept", "application/json")
            .method(original.method, original.body)
            .build()
        return chain.proceed(request)
    }
}