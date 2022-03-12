package id.gwijaya94.mygithubusersapp.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.gwijaya94.mygithubusersapp.ListUserAdapter
import id.gwijaya94.mygithubusersapp.databinding.ActivityMainBinding
import id.gwijaya94.mygithubusersapp.getIcon
import id.gwijaya94.mygithubusersapp.getJsonDataFromAsset
import id.gwijaya94.mygithubusersapp.model.User

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var listUserData: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.apply {
            title = "Github Users"
            setHomeAsUpIndicator(getIcon(this@MainActivity, "cmd_github", color = Color.WHITE))
            setDisplayHomeAsUpEnabled(true)
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var jsonListUser = getJsonDataFromAsset(this, "githubuser.json")
        listUserData = jsonListUser.users

        val listUsersAdapter = ListUserAdapter(listUserData)
        listUsersAdapter.setOnItemClicked(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val moveDetailUserActivity =
                    Intent(this@MainActivity, DetailUserActivity::class.java)
                moveDetailUserActivity.putExtra(DetailUserActivity.USER_DATA, data)
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