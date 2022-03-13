package id.gwijaya94.mygithubusersapp.service

import android.content.Context
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.ANRequest
import com.androidnetworking.interceptors.HttpLoggingInterceptor

class ApiService(context: Context) {
    private var context: Context = context

    init {
        this.context = context
        AndroidNetworking.initialize(context)
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY)
    }

    fun get(url: String): ANRequest.GetRequestBuilder<out ANRequest.GetRequestBuilder<*>> {
        return AndroidNetworking.get(url)
    }

    fun post(url: String): ANRequest.PostRequestBuilder<out ANRequest.PostRequestBuilder<*>> {
        return AndroidNetworking.post(url)
    }
}