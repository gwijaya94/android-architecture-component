package id.gwijaya94.mygithubusersapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import id.gwijaya94.mygithubusersapp.databinding.ActivityDetailUserBinding
import id.gwijaya94.mygithubusersapp.model.User

class DetailUserActivity : AppCompatActivity() {
    companion object {
        const val USER_DATA = "user_data"
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var userData: User

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userData = intent.getParcelableExtra<User>(USER_DATA) as User

        supportActionBar?.apply {
            title = userData.username.lowercase()
            setDisplayHomeAsUpEnabled(true)
        }

        val imgResource = resources.getIdentifier(userData.avatar, null, packageName)
        Glide.with(this)
            .load(imgResource)
            .circleCrop()
            .into(binding.profilePicture)

        binding.apply {
            userName.text = userData.name
            userCompany.text = userData.company
            userLocation.text = userData.location
            userUsername.text = "@${userData.username.lowercase()}"
            userInfo.followerCountValue.text = userData.follower
            userInfo.followingCountValue.text = userData.following
            userInfo.repoCountValue.text = userData.repository

        }

    }

}