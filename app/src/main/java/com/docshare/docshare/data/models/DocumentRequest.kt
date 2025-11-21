package com.docshare.docshare.data.models

enum class RequestStatus { REQUEST, APPROVED, REJECTED, PENDING }

data class DocumentRequest(
    val id: String,
    val requesterId: String,
    val ownerId: String,
    val documentIds: List<String>,
    val status: RequestStatus,
    val timestamp: Long
)

