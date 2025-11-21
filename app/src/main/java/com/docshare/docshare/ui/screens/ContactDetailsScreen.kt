package com.docshare.docshare.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.docshare.docshare.data.models.ContactUser

@Composable
fun ContactDetailsScreen(
    contact: ContactUser,
    onRequestDocs: () -> Unit,
    onViewHistory: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = contact.name, style = MaterialTheme.typography.titleLarge)
        Text(text = contact.phone, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onRequestDocs) { Text("Request Documents") }
        Spacer(Modifier.height(12.dp))
        Button(onClick = onViewHistory) { Text("View Request History") }
        Spacer(Modifier.height(12.dp))
        Button(onClick = onBack) { Text("Back") }
    }
}
