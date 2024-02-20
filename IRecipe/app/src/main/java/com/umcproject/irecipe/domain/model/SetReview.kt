package com.umcproject.irecipe.domain.model

import android.net.Uri

data class SetWrite(
    var content: String = "",
    var score: Int = 0,
    var imageUri: Uri? = null
)