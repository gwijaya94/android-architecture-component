package id.gwijaya94.mygithubusersapp.activity

import android.app.SearchManager
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setupApp()
        setContentView(binding.root)

        mainViewModel.isLoading.observe(this) {
            if (it) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.GONE
        }
        mainViewModel.searchData.observe(this) {
            val listData = ArrayList<GithubUser>()
            if (it != null) {
                for (data in it) if (data != null) listData.add(data)
            }
            if (listData.size > 0) setListData(listData)
            else binding.errText.apply {
                text = "No Data"
                visibility = View.VISIBLE
            }

        }
        mainViewModel.errorData.observe(this) {
            binding.errText.apply {
                if (it != null && it != "") {
                    text = it
                    visibility = View.VISIBLE
                } else visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService<SearchManager>()
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.apply {
            if (searchManager != null) {
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
            }
            queryHint = resources.getString(R.string.search)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    binding.errText.visibility = View.GONE
                    Log.d(this@MainActivity::class.java.simpleName, "ONSUBMIT: $p0")
                    mainViewModel.getSearchData(p0 ?: "")
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    Log.d(this@MainActivity::class.java.simpleName, "ONCHANGE: $p0")
                    return false
                }
            })
        }

        return super.onCreateOptionsMenu(menu)
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
        binding.errText.apply {
            textSize = 20f
            text = "Type anything."
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
        binding.errText.visibility = View.GONE
        binding.rvListUsers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listUsersAdapter
        }
    }

}