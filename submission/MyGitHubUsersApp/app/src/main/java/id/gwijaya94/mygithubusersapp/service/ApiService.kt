package id.gwijaya94.mygithubusersapp.service

import android.content.Context
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.ANRequest
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient

class ApiService(context: Context) {
    private val client = OkHttpClient.Builder().addInterceptor(ChuckerInterceptor(context)).build()

    init {
        AndroidNetworking.initialize(context, client)
    }

    fun get(url: String): ANRequest.GetRequestBuilder<out ANRequest.GetRequestBuilder<*>> {
        return AndroidNetworking.get(url)
    }

    fun post(url: String): ANRequest.PostRequestBuilder<out ANRequest.PostRequestBuilder<*>> {
        return AndroidNetworking.post(url)
    }
}