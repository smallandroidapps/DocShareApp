package com.docshare.docshare.data.local

import com.docshare.docshare.data.models.AppUser
import com.docshare.docshare.data.models.ContactUser
import com.docshare.docshare.data.models.DocumentRequest
import com.docshare.docshare.data.models.DocumentType
import com.docshare.docshare.data.models.RequestStatus

object LocalDataProvider {
    val currentUser = AppUser(id = "u0", name = "Ganesh", phone = "+91 90000 00000")

    val contacts: List<ContactUser> = List(10) { index ->
        ContactUser(
            id = "c$index",
            name = "Contact $index",
            phone = "+91 90000 00${index.toString().padStart(2, '0')}",
            isTrusted = index % 2 == 0,
            isBlocked = false
        )
    }

    val documentTypes: List<DocumentType> = listOf(
        DocumentType("dt1", "ID Proof", "Government issued photo ID"),
        DocumentType("dt2", "Address Proof", "Utility bill or rental agreement"),
        DocumentType("dt3", "Bank Statement", "Last 3 months statement"),
        DocumentType("dt4", "Salary Slip", "Last month payslip"),
        DocumentType("dt5", "PAN/Aadhaar", "Tax/Identity documents")
    )

    private val allRequests: List<DocumentRequest> = List(8) { idx ->
        DocumentRequest(
            id = "r$idx",
            requesterId = currentUser.id,
            ownerId = contacts[idx % contacts.size].id,
            documentIds = listOf("dt${(idx % documentTypes.size) + 1}"),
            status = RequestStatus.values()[idx % RequestStatus.values().size],
            timestamp = System.currentTimeMillis() - idx * 86_400_000L
        )
    }

    fun getContactById(id: String): ContactUser = contacts.first { it.id == id }

    fun getRequestsForContact(contactId: String): List<DocumentRequest> =
        allRequests.filter { it.ownerId == contactId }

    fun getRequestById(requestId: String): DocumentRequest =
        allRequests.first { it.id == requestId }
}

