package id.gwijaya94.mygithubusersapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JsonListUser(
    var users: List<User>
) : Parcelable