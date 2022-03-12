package id.gwijaya94.mygithubusersapp

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import com.google.gson.Gson
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.mikepenz.iconics.utils.colorInt
import com.mikepenz.iconics.utils.sizeDp
import id.gwijaya94.mygithubusersapp.model.JsonListUser

fun getJsonDataFromAsset(context: Context, fileName: String): JsonListUser {
    val gson = Gson()
    val jsonFileString = context.assets.open(fileName).bufferedReader().use { it.readText() }

    return gson.fromJson(jsonFileString, JsonListUser::class.java)
}

fun getIcon(
    context: Context, iconName: String, color: Int = Color.WHITE, size: Int = 24
): Drawable = IconicsDrawable(context).apply {
    icon = CommunityMaterial.getIcon(iconName)
    colorInt = color
    sizeDp = size
}

