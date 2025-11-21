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
import com.docshare.docshare.data.models.ContactUser

@Composable
fun ContactsListScreen(
    contacts: List<ContactUser>,
    onContactClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Contacts", style = MaterialTheme.typography.titleLarge)
        LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
            items(contacts) { c ->
                Column(modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable { onContactClick(c.id) }) {
                    Text(text = c.name, style = MaterialTheme.typography.titleMedium)
                    Text(text = c.phone, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
