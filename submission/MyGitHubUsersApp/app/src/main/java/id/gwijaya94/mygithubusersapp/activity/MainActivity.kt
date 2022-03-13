package id.gwijaya94.mygithubusersapp.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.gwijaya94.mygithubusersapp.ListUserAdapter
import id.gwijaya94.mygithubusersapp.R
import id.gwijaya94.mygithubusersapp.databinding.ActivityMainBinding
import id.gwijaya94.mygithubusersapp.getIcon
import id.gwijaya94.mygithubusersapp.model.GithubUser
import id.gwijaya94.mygithubusersapp.model.MainViewModel
import id.gwijaya94.mygithubusersapp.setHeaderColor


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private var headerColor = R.color.github_header

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupApp()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.isLoading.observe(this) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
        mainViewModel.searchData.observe(this) {
            val listData = ArrayList<GithubUser>()
            if (it != null) {
                for (data in it) {
                    if (data != null) listData.add(data)
                }
            }
            setListData(listData)
        }
        mainViewModel.errorData.observe(this) {
            binding.errText.apply {
                if (it != null && it != "") {
                    text = it
                    visibility = View.VISIBLE
                } else visibility = View.INVISIBLE
            }
        }
        mainViewModel.getSearchData("gwija")
    }

    private fun setupApp() {
        mainViewModel.setContext(this)
        setHeaderColor(this, window, headerColor)
        supportActionBar?.apply {
            title = "Github Users"
            setHomeAsUpIndicator(getIcon(this@MainActivity, "cmd_github", color = Color.WHITE))
            setDisplayHomeAsUpEnabled(true)
            setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.github_header)))
        }
    }

    private fun setListData(listUserData: List<GithubUser>) {
        val listUsersAdapter = ListUserAdapter(listUserData)
        listUsersAdapter.setOnItemClicked(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GithubUser) {
                val moveDetailUserActivity =
                    Intent(this@MainActivity, DetailUserActivity::class.java)
                moveDetailUserActivity.putExtra(DetailUserActivity.USER_NAME, data.login)
                startActivity(moveDetailUserActivity)
            }
        })
        binding.rvListUsers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listUsersAdapter
        }
    }

}