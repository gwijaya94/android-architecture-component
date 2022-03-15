package id.gwijaya94.mygithubusersapp.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import id.gwijaya94.mygithubusersapp.service.ApiService

class MainViewModel : ViewModel() {
    private lateinit var api: ApiService
    private val _searchData = MutableLiveData<List<GithubUser?>?>()
    private val _errorData = MutableLiveData<String?>()
    private val _isLoading = MutableLiveData<Boolean>(false)

    val searchData: LiveData<List<GithubUser?>?> = _searchData
    val errorData: LiveData<String?> = _errorData
    val isLoading: LiveData<Boolean> = _isLoading

    fun setContext(context: Context) {
        api = ApiService(context)
    }

    fun getSearchData(userName: String) {
        _isLoading.value = true
        api.get("https://api.github.com/search/users")
            .addQueryParameter("q", userName)
            .addQueryParameter("per_page", "50")
            .build().getAsObject(
                SearchUser::class.java,
                object : ParsedRequestListener<SearchUser> {
                    override fun onResponse(data: SearchUser) {
                        _searchData.value = data.items
                        _isLoading.value = false
                    }

                    override fun onError(anError: ANError) {
                        Log.wtf("error", anError.errorBody.toString())
                        _searchData.value = null
                        _errorData.value = anError.errorBody.toString()
                        _isLoading.value = false
                    }
                })
    }
}