package com.elfak.tempest.presentation.filter

import com.elfak.tempest.model.User
import java.util.Date

sealed class FilterEvent {
    data class TitleChanged(val title: String): FilterEvent()
    data class ContentChanged(val content: String): FilterEvent()
    data class AuthorChanged(val author: User): FilterEvent()
    data class PriorityChanged(val priority: String): FilterEvent()
    data class RadiusChanged(val radius: Int?): FilterEvent()
    data class CreatedChanged(val created: Date?): FilterEvent()
}