package id.gwijaya94.dicodingretrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _restaurant = MutableLiveData<Restaurant>()
    private val _listReview = MutableLiveData<List<CustomerReviewsItem>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _snackbarText = MutableLiveData<Event<String>>()

    val restaurant: LiveData<Restaurant> = _restaurant
    val listReview: LiveData<List<CustomerReviewsItem>> = _listReview
    val isLoading: LiveData<Boolean> = _isLoading
    val snackbarText: LiveData<Event<String>> = _snackbarText

    init {
        findRestaurant()
    }

    private fun findRestaurant() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getRestaurant(RESTAURANT_ID)
        client.enqueue(object : Callback<ResponseRestaurant> {
            override fun onResponse(
                call: Call<ResponseRestaurant>,
                response: Response<ResponseRestaurant>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _restaurant.value = response.body()?.restaurant!!
                    _listReview.value =
                        (response.body()?.restaurant?.customerReviews as List<CustomerReviewsItem>?)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseRestaurant>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }


    fun postReview(review: String) {
        _isLoading.value = false
        val client =
            ApiConfig.getApiService().postReview(RESTAURANT_ID, "Dicoding", review)
        client.enqueue(object : Callback<PostReviewResponse> {
            override fun onResponse(
                call: Call<PostReviewResponse>,
                response: Response<PostReviewResponse>
            ) {
                _isLoading.value = (false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _listReview.value=(responseBody.customerReviews)
                    _snackbarText.value = Event(response.body()?.message.toString())
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
                _isLoading.value = (false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }


    companion object {
        private const val TAG = "MainViewModel"
        private const val RESTAURANT_ID = "uewq1zg2zlskfw1e867"
    }
}