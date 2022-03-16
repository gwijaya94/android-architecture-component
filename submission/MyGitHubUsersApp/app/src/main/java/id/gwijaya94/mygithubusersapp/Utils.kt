package id.gwijaya94.mygithubusersapp

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.Window
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.mikepenz.iconics.utils.colorInt
import com.mikepenz.iconics.utils.sizeDp
import id.gwijaya94.mygithubusersapp.model.GithubUser


fun getIcon(
    context: Context, iconName: String, color: Int = Color.WHITE, size: Int = 24
): Drawable = IconicsDrawable(context).apply {
    icon = CommunityMaterial.getIcon(iconName)
    colorInt = color
    sizeDp = size
}

fun setHeaderColor(context: Context, window: Window, color: Int) {
    window.apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusBarColor = context.getColor(color)
            navigationBarColor = context.getColor(color)
        } else {
            context.resources.getColor(color)
            navigationBarColor = context.resources.getColor(color)
        }
    }
}

fun processData(data: List<GithubUser?>?): ArrayList<GithubUser> {
    val listData = ArrayList<GithubUser>()
    if (data != null) {
        for (it in data) if (it != null) listData.add(it)
    }
    return listData
}
