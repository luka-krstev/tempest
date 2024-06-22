package com.elfak.tempest.presentation.modify_ticket

data class ModifyTicketFormState(
    val title: String = "",
    val titleError: String = "",
    val content: String = "",
    val contentError: String = "",
    val priority: String = "Low",
    val loading: Boolean = false,
    val success: Boolean = false,
)