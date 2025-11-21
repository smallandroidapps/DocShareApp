package com.docshare.docshare.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onOpenContacts: () -> Unit,
    onOpenRequests: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenPremium: () -> Unit,
    onOpenUpload: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "DocShare", style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = { /* settings stub */ }) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
            }
        }

        Button(onClick = onOpenContacts, modifier = Modifier.padding(top = 24.dp)) { Text("Contacts") }
        Button(onClick = onOpenRequests, modifier = Modifier.padding(top = 12.dp)) { Text("Requests") }
        Button(onClick = onOpenSearch, modifier = Modifier.padding(top = 12.dp)) { Text("Search") }
        Button(onClick = onOpenUpload, modifier = Modifier.padding(top = 12.dp)) { Text("Upload") }
        Button(onClick = onOpenPremium, modifier = Modifier.padding(top = 12.dp)) { Text("Premium") }
    }
}
