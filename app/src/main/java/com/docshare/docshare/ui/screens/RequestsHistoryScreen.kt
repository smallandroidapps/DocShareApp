package com.docshare.docshare.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.docshare.docshare.data.models.DocumentRequest

@Composable
fun RequestsHistoryScreen(
    requests: List<DocumentRequest>,
    onOpenDetail: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Requests History", style = MaterialTheme.typography.titleLarge)
        LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
            items(requests) { r ->
                Column(modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable { onOpenDetail(r.id) }) {
                    Text(text = "Request ${r.id}", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Status: ${r.status}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        Text(text = "Back", modifier = Modifier.padding(top = 12.dp).clickable { onBack() })
    }
}
