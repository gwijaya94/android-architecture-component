package id.gwijaya94.mygithubusersapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.gwijaya94.mygithubusersapp.model.DetailViewModel

class PagerAdapter(
    activity: AppCompatActivity,
    tabName: IntArray,
    userName:String,
) :
    FragmentStateAdapter(activity) {
    private val mContext: Context = activity.applicationContext
    private val userId = userName
    private val mTabName = tabName

    private fun getTabName(index: Int): String {
        return mContext.resources.getString(mTabName[index])
    }

    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ListUserFragment(userId,getTabName(position))
            1 -> fragment = ListUserFragment(userId,getTabName(position))
        }
        return fragment as Fragment
    }
}