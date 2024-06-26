package com.elfak.tempest.presentation.modify_ticket

sealed class ModifyTicketFormEvent {
    data class TitleChanged(val title: String): ModifyTicketFormEvent()
    data class ContentChanged(val content: String): ModifyTicketFormEvent()
    data class PriorityChanged(val priority: String): ModifyTicketFormEvent()
    data object Submit: ModifyTicketFormEvent()
}