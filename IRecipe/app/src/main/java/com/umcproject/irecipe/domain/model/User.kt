package com.umcproject.irecipe.domain.model

import android.net.Uri
import java.io.File

data class User(
    var num: String = "",
    var accessToken: String = "",
    var refreshToken: String = "",
    var platform: String = "",
    var name: String = "",
    var genderCode: Int = -1,
    var age: String = "",
    var photoUri: Uri? = null,
    var nick: String = "",
    var allergy: List<Int>? = null
)
