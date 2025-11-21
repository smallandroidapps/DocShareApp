package com.docshare.docshare.data.models

data class ContactUser(
    val id: String,
    val name: String,
    val phone: String,
    val isTrusted: Boolean,
    val isBlocked: Boolean
)

