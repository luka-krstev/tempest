package com.elfak.tempest.presentation.avatar

import android.net.Uri

sealed class AvatarFormEvent {
    data class UriChanged(val uri: Uri): AvatarFormEvent()
    data object Submit: AvatarFormEvent()
}