package id.gwijaya94.mygithubusersapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.mikepenz.iconics.utils.colorInt
import id.gwijaya94.mygithubusersapp.R
import id.gwijaya94.mygithubusersapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val displayMetrics = resources.displayMetrics
        val headerColor = resources.getColor(R.color.github_header)
        val greyColor = resources.getColor(R.color.github_grey)

        val iconSize = (displayMetrics.widthPixels * 0.4).toInt()
        window.statusBarColor = headerColor
        binding.apply {
            splashLogo.layoutParams.height = iconSize
            splashLogo.layoutParams.width = iconSize
            footer.tvCopyright.setTextColor(greyColor)
            footer.tvCopyrightIcon.icon?.colorInt = greyColor
        }

        Handler().postDelayed({
            val moveToMainActivity = Intent(this, MainActivity::class.java)
            startActivity(moveToMainActivity)
            finish()
        }, 2000)
    }
}