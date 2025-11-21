package com.docshare.docshare.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.docshare.docshare.data.models.DocumentRequest

@Composable
fun RequestDetailScreen(request: DocumentRequest, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Request Detail", style = MaterialTheme.typography.titleLarge)
        Text(text = "ID: ${request.id}")
        Text(text = "Owner: ${request.ownerId}")
        Text(text = "Status: ${request.status}")
        Text(text = "Documents: ${request.documentIds.joinToString()}")
        Text(text = "Back", modifier = Modifier.padding(top = 12.dp).clickable { onBack() })
    }
}
