package id.gwijaya94.mygithubusersapp

import android.content.Intent
import android.os.Bundle
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

class ListUserFragment(private val userName: String, private val type: String) : Fragment() {
    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var binding: FragmentListUserBinding
    private var noData = "No $type Data"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailViewModel.setContext(requireContext(), userName)

        if (type == resources.getString(TAB_TITLES[0])) {
            detailViewModel.getListFollower()
            detailViewModel.followers.observe(this) {
                processData(it)
            }
        } else detailViewModel.following.observe(this) {
            detailViewModel.getListFollowing()
            processData(it)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun processData(data: List<GithubUser?>?) {
        if (data == null || data.isEmpty()) {
            binding.emptyData.apply {
                text = noData
                textSize = 20f
                visibility = View.VISIBLE
            }
        } else {
            setListData(data)
            binding.emptyData.visibility = View.GONE
        }
    }

    private fun filterData(list: List<GithubUser?>): ArrayList<GithubUser> {
        val tempData = ArrayList<GithubUser>()
        for (data in list) {
            if (data != null) tempData.add(data)
        }
        return tempData
    }

    private fun setListData(listUserData: List<GithubUser?>) {
        val listData = filterData(listUserData)
        val listUsersAdapter = ListUserAdapter(listData)
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
        private val TAG = ListUserFragment::class.java.simpleName

        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_follower, R.string.tab_following)
    }
}