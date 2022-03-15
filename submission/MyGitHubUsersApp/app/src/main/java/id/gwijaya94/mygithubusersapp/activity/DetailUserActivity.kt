package id.gwijaya94.mygithubusersapp.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import id.gwijaya94.mygithubusersapp.PagerAdapter
import id.gwijaya94.mygithubusersapp.R
import id.gwijaya94.mygithubusersapp.databinding.ActivityDetailUserBinding
import id.gwijaya94.mygithubusersapp.model.DetailViewModel
import id.gwijaya94.mygithubusersapp.model.UserDetail
import id.gwijaya94.mygithubusersapp.setHeaderColor

class DetailUserActivity : AppCompatActivity() {
    companion object {
        const val USER_NAME = "gwijaya94"

        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_follower, R.string.tab_following)
    }

    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var userData: UserDetail
    private var headerColor = R.color.github_header

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userNameExtra = intent.getStringExtra(USER_NAME) as String
        detailViewModel.setContext(this, userNameExtra)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)

        detailViewModel.getUserDetail()
        setContentView(binding.root)
        setUp(userNameExtra)

        detailViewModel.selectedData.observe(this) {
            if (it != null) {
                userData = it
                setUser(it)
                binding.contentWrapper.visibility = View.VISIBLE
            } else binding.contentWrapper.visibility = View.GONE
        }
        detailViewModel.isLoading.observe(this) {
            if (it) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.GONE
        }
        detailViewModel.errorData.observe(this) {
            if (it != null) binding.contentWrapper.visibility = View.GONE
            else binding.contentWrapper.visibility = View.VISIBLE
        }
        val sectionsPagerAdapter = PagerAdapter(this, TAB_TITLES, USER_NAME)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()


    }

    private fun setUser(userData: UserDetail) {
        val imgResource = userData.avatarUrl
        Glide.with(this)
            .load(imgResource)
            .circleCrop()
            .into(binding.userSummary.profilePicture)

        binding.userSummary.apply {
            userName.text = userData.name ?: userData.login
            userUsername.text = "ID ${userData.id?.toString()}"
            userBio.text = userData.bio ?: "-"
            userInfo.followerCountValue.text = userData.followers.toString()
            userInfo.followingCountValue.text = userData.following.toString()
            userInfo.repoCountValue.text = userData.publicRepos.toString()
        }
    }

    private fun setUp(userNameExtra: String) {
        supportActionBar?.apply {
            title = "@${userNameExtra.lowercase()}"
            setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.github_header)))
            setDisplayHomeAsUpEnabled(true)
        }
        setHeaderColor(this, window, headerColor)
    }


}