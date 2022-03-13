package id.gwijaya94.mygithubusersapp.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.ParsedRequestListener
import id.gwijaya94.mygithubusersapp.service.ApiService
import org.json.JSONArray

class DetailViewModel : ViewModel() {
    private lateinit var api: ApiService
    private lateinit var uri: String
    private val _selectedData = MutableLiveData<UserDetail?>()
    val selectedData: LiveData<UserDetail?> = _selectedData
    private val _followers = MutableLiveData<List<GithubUser?>?>()
    val followers: LiveData<List<GithubUser?>?> = _followers
    private val _following = MutableLiveData<List<GithubUser?>?>()
    val following: LiveData<List<GithubUser?>?> = _following
    private val _errorData = MutableLiveData<String?>()
    val errorData: LiveData<String?> = _errorData
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun setContext(context: Context,userName: String) {
        api = ApiService(context)
        uri = "https://api.github.com/users/$userName"
    }

    fun getUserDetail() {
//        getListFollower(uri)
//        getListFollowing(uri)
        _isLoading.value = true
        api.get(uri).build().getAsObject(
            UserDetail::class.java,
            object : ParsedRequestListener<UserDetail> {
                override fun onResponse(detail: UserDetail) {
                    _selectedData.value = detail
                    _isLoading.value = false
                }

                override fun onError(anError: ANError) {
                    Log.wtf("error", anError.errorBody.toString())
                    _errorData.value = anError.errorBody.toString()
                    _isLoading.value = false
                }
            })
    }

    private fun getListFollower() {
        api.get("${uri}/followers").build().getAsJSONArray(object : JSONArrayRequestListener {
            override fun onResponse(response: JSONArray?) {
            }

            override fun onError(anError: ANError?) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getListFollowing() {
        api.get("${uri}/following").build().getAsJSONArray(object : JSONArrayRequestListener {
            override fun onResponse(response: JSONArray?) {
                TODO("Not yet implemented")
            }

            override fun onError(anError: ANError?) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        _selectedData.value = null
    }
}