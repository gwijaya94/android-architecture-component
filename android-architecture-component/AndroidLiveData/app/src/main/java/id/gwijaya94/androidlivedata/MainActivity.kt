package id.gwijaya94.androidlivedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import id.gwijaya94.androidlivedata.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private val mLiveDataTimerViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        subscribe()
    }

    private fun subscribe() {
        val elapsedTimeObserver = Observer<Long?> { aLong ->
            val newText = this@MainActivity.resources.getString(R.string.seconds, aLong)
            activityMainBinding.timerTextview.text = newText
        }
        mLiveDataTimerViewModel.getElapsedTime().observe(this, elapsedTimeObserver)
    }
}