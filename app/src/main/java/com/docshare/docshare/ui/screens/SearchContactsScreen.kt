package com.docshare.docshare.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.docshare.docshare.data.models.ContactUser

@Composable
fun SearchContactsScreen(
    contacts: List<ContactUser>,
    onContactClick: (String) -> Unit,
    onBack: () -> Unit
) {
    val queryState = remember { mutableStateOf(TextFieldValue("")) }
    val q = queryState.value.text.trim()
    val results = if (q.length < 2) emptyList() else contacts.filter {
        it.name.lowercase().contains(q.lowercase()) || it.phone.contains(q)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Text(text = "Search", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(start = 8.dp))
        }
        OutlinedTextField(
            value = queryState.value,
            onValueChange = { queryState.value = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search by name or phone") }
        )

        if (q.length < 2) {
            Text(text = "Type at least 2 characters", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 12.dp))
        } else if (results.isEmpty()) {
            Text(text = "No matching members found", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 12.dp))
        } else {
            LazyColumn(modifier = Modifier.padding(top = 12.dp)) {
                items(results) { c ->
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onContactClick(c.id) }
                        .padding(vertical = 8.dp)) {
                        Text(text = c.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = c.phone, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

