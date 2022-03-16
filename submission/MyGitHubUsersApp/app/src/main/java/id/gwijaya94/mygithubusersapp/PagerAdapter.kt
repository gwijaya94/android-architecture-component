package id.gwijaya94.mygithubusersapp

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class PagerAdapter(
    activity: AppCompatActivity,
    tabName: IntArray,
    userName: String,
) :
    FragmentStateAdapter(activity) {
    private val mContext: Context = activity.applicationContext
    private val userId = userName
    private val mTabName = tabName


    private fun bundledFrame(position: Int): ListUserFragment {
        val tabName = mContext.resources.getString(mTabName[position])
        val bundle = Bundle()
        val fragment = ListUserFragment()
        bundle.putString("type",tabName)
        bundle.putString("username",userId)

        fragment.arguments = bundle
        return fragment
    }

    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = bundledFrame(position)
            1 -> fragment = bundledFrame(position)
        }
        return fragment as Fragment
    }
}