package id.gwijaya94.mygithubusersapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.gwijaya94.mygithubusersapp.activity.DetailUserActivity
import id.gwijaya94.mygithubusersapp.databinding.FragmentListUserBinding
import id.gwijaya94.mygithubusersapp.model.DetailViewModel
import id.gwijaya94.mygithubusersapp.model.GithubUser


class ListUserFragment : Fragment() {
    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var binding: FragmentListUserBinding

    private var userName: String = ""
    private var type: String = ""
    private var noData = "No $type Data"
    private var isFollowerTab: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            type = bundle.getString("type").toString()
            isFollowerTab = type == resources.getString(TAB_TITLES[0])
            userName = bundle.getString("username").toString()

            detailViewModel.setContext(requireContext(), userName)

            detailViewModel.isLoading.observe(this) {
                if (it) binding.progressBar.visibility = View.VISIBLE
                else binding.progressBar.visibility = View.GONE
            }
            detailViewModel.userData.observe(this) {
                val data = if (isFollowerTab) it.second else it.third
                Log.d(TAG, "onCreate: $data")
                if (data !== null) {
                    binding.progressBar.visibility = View.GONE
                    val listData = processData(data)
                    checkData(listData)
                } else binding.progressBar.visibility = View.VISIBLE

            }
        } else binding.errText.apply {
            text = noData
            textSize = 20f
            visibility = View.VISIBLE
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun checkData(listData: List<GithubUser>) {
        if (listData.isNotEmpty()) setListData(listData)
        else binding.errText.apply {
            text = noData
            textSize = 20f
            visibility = View.VISIBLE
        }
    }

    private fun setListData(listUserData: List<GithubUser>) {
        val listUsersAdapter = ListUserAdapter(listUserData)
        listUsersAdapter.setOnItemClicked(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GithubUser) {
                val moveDetailUserActivity =
                    Intent(requireContext(), DetailUserActivity::class.java)
                moveDetailUserActivity.putExtra(DetailUserActivity.USER_NAME, data.login)
                startActivity(moveDetailUserActivity)
            }
        })
        binding.rvListUsers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listUsersAdapter
        }
    }

    companion object {
        private const val TAG = "HERE I AM >>>>>"

        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_follower, R.string.tab_following)
    }
}