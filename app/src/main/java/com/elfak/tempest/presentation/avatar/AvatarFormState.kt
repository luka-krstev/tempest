package com.elfak.tempest.presentation.avatar

import android.net.Uri
import com.elfak.tempest.R

data class AvatarFormState(
    val uri: Uri = Uri.parse("android.resource://com.elfak.tempest/" + R.drawable.placeholder),
    val loading: Boolean = false,
    val error: String = "",
    val selected: Boolean = false,
    val success: Boolean = false,
)
