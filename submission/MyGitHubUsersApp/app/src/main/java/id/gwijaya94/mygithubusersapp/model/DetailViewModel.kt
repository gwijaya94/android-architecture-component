package id.gwijaya94.mygithubusersapp.model

import TripleMediatorLiveData
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.ParsedRequestListener
import com.google.gson.Gson
import id.gwijaya94.mygithubusersapp.service.ApiService
import org.json.JSONArray

class DetailViewModel : ViewModel() {
    private lateinit var api: ApiService
    private lateinit var uri: String
    private val _selectedData = MutableLiveData<UserDetail?>()
    private val selectedData: LiveData<UserDetail?> = _selectedData
    private val _followers = MutableLiveData<List<GithubUser?>?>()
    private val followers: LiveData<List<GithubUser?>?> = _followers
    private val _following = MutableLiveData<List<GithubUser?>?>()
    private val following: LiveData<List<GithubUser?>?> = _following
    private val _errorData = MutableLiveData<String?>()
    val errorData: LiveData<String?> = _errorData
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    val userData = TripleMediatorLiveData(selectedData, followers, following)


    fun setContext(context: Context, userName: String) {
        api = ApiService(context)
        uri = "https://api.github.com/users/$userName"

        getUserData()
    }

    private fun getUserData() {
        getUserDetail()
        getListFollowing()
        getListFollower()
    }


    private fun getUserDetail() {
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
                if (response != null)
                    _followers.value = processUser(response)
            }

            override fun onError(anError: ANError?) {
                Log.wtf("error", anError?.errorBody.toString())
                _errorData.value = anError?.errorBody.toString()
                _isLoading.value = false
            }
        })
    }

    private fun getListFollowing() {
        api.get("${uri}/following").build().getAsJSONArray(object : JSONArrayRequestListener {
            override fun onResponse(response: JSONArray?) {
                if (response != null)
                    _following.value = processUser(response)
            }

            override fun onError(anError: ANError?) {
                Log.wtf("error", anError?.errorBody.toString())
                _errorData.value = anError?.errorBody.toString()
                _isLoading.value = false
            }
        })
    }

    private fun processUser(list: JSONArray): ArrayList<GithubUser> {
        val tempArr = ArrayList<GithubUser>()
        for (i in 0 until list.length()) {
            if (list[i] != null) {
                val jsonData = list.getJSONObject(i).toString()
                val data = Gson().fromJson(jsonData, GithubUser::class.java)
                tempArr.add(data)
            }
        }
        return tempArr
    }

    override fun onCleared() {
        super.onCleared()
        _selectedData.value = null
    }

}