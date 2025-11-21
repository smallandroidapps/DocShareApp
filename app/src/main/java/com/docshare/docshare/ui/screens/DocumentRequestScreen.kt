package com.docshare.docshare.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.docshare.docshare.data.models.DocumentType

@Composable
fun DocumentRequestScreen(
    documentTypes: List<DocumentType>,
    onNext: (selectedIds: List<String>) -> Unit,
    onBack: () -> Unit
) {
    val selected = remember { mutableStateListOf<String>() }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Select Documents", style = MaterialTheme.typography.titleLarge)
        LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
            items(documentTypes) { dt ->
                val checked = selected.contains(dt.id)
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Checkbox(checked = checked, onCheckedChange = {
                        if (it) selected.add(dt.id) else selected.remove(dt.id)
                    })
                    Text(text = dt.title, style = MaterialTheme.typography.titleMedium)
                    Text(text = dt.description, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        Button(onClick = { onNext(selected.toList()) }, modifier = Modifier.padding(top = 16.dp)) { Text("Next") }
        Button(onClick = onBack, modifier = Modifier.padding(top = 8.dp)) { Text("Back") }
    }
}
