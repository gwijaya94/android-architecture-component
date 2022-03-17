package id.gwijaya94.mygithubusersapp.activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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
import id.gwijaya94.mygithubusersapp.model.GithubUser
import id.gwijaya94.mygithubusersapp.model.UserDetail
import id.gwijaya94.mygithubusersapp.processData
import id.gwijaya94.mygithubusersapp.setHeaderColor

class DetailUserActivity : AppCompatActivity() {
    companion object {
        const val USER_NAME = "gwijaya94"

        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_follower, R.string.tab_following)
    }

    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var binding: ActivityDetailUserBinding
    private var userData: UserDetail? = null
    private var followers: List<GithubUser?>? = null
    private var following: List<GithubUser?>? = null
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

        setContentView(binding.root)
        setUp(userNameExtra)
        detailViewModel.userData.observe(this) {
            val mUserData = it.first
            var mFollowers = processData(it.second)
            var mFollowing = processData(it.third)

            if (mUserData != null) {
                userData = mUserData
                setUser(mUserData)
                binding.contentWrapper.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.contentWrapper.visibility = View.GONE
            }
        }

        detailViewModel.errorData.observe(this) {
            if (it != null) binding.contentWrapper.visibility = View.GONE
            else binding.contentWrapper.visibility = View.VISIBLE
        }

        val sectionsPagerAdapter = PagerAdapter(this, TAB_TITLES, userNameExtra)
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
            userCompany.text = userData.company ?: "-"
            userLocation.text = userData.location ?: "-"
            userRepo.apply {
                text = userData.htmlUrl ?: "-"
                setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(userData.htmlUrl))
                    startActivity(intent)
                }
            }
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