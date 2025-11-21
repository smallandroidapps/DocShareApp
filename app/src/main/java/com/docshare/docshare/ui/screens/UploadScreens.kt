package com.docshare.docshare.ui.screens

import android.widget.Toast
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.docshare.docshare.repository.UploadRepository

@Composable
fun UploadScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    var fileName by rememberSaveable { mutableStateOf<String?>(null) }
    var fileType by rememberSaveable { mutableStateOf<String?>(null) }
    var fileSizeKb by rememberSaveable { mutableStateOf<Int?>(null) }
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var tags by rememberSaveable { mutableStateOf(listOf<String>()) }
    var showAddTag by remember { mutableStateOf(false) }
    var newTagText by remember { mutableStateOf("") }

    val isValid = (fileName != null) && title.trim().isNotEmpty()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Upload Document", style = MaterialTheme.typography.titleLarge)
            TextButton(onClick = onBack) { Text("Back") }
        }

        Spacer(Modifier.height(12.dp))

        Card(modifier = Modifier.fillMaxWidth().clickable {
            fileName = "sample_document.pdf"
            fileType = "PDF"
            fileSizeKb = 325
        }) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (fileName == null) {
                    Text("Tap to choose file", style = MaterialTheme.typography.bodyMedium)
                } else {
                    Text(fileName!!)
                    Text("$fileType, ${fileSizeKb} KB", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Document Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            AssistChip(onClick = { showAddTag = !showAddTag }, label = { Text("+ Add Tag") })
        }
        if (showAddTag) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                OutlinedTextField(value = newTagText, onValueChange = { newTagText = it }, label = { Text("Tag") })
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    val tag = newTagText.trim()
                    if (tag.isNotEmpty()) {
                        tags = tags + tag
                        newTagText = ""
                        showAddTag = false
                    }
                }) { Text("Add") }
            }
        }

        Spacer(Modifier.height(8.dp))

        Column {
            tags.forEach { tag ->
                AssistChip(
                    onClick = { /* no-op */ },
                    label = { Text(tag) },
                    leadingIcon = {
                        IconButton(onClick = { tags = tags.filterNot { t -> t == tag } }) {
                            Icon(Icons.Default.Close, contentDescription = "Remove")
                        }
                    },
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = { onNext() }, enabled = isValid, modifier = Modifier.fillMaxWidth()) {
            Text("Next")
        }

        if (!isValid) {
            Text("Select a file and enter a title to proceed.", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun ReviewScreen(
    onBack: () -> Unit,
    onSubmit: () -> Unit,
) {
    var submitting by rememberSaveable { mutableStateOf(false) }
    val ctx = LocalContext.current
    val repo = remember { UploadRepository(ctx) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Review", style = MaterialTheme.typography.titleLarge)
            TextButton(onClick = onBack) { Text("Back") }
        }

        Spacer(Modifier.height(12.dp))
        // Static preview text; real data preserved on back
        Text("File: sample_document.pdf")
        Text("Type: PDF | Size: 325 KB", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(8.dp))
        Text("Title: (mock)")
        Spacer(Modifier.height(8.dp))
        Text("Description:")
        Text("(mock)", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(8.dp))
        Text("Tags: (mock)")

        Spacer(Modifier.height(24.dp))

        Button(onClick = {
            if (!submitting) {
                Log.i("RequestDetail", "APPROVE clicked; queuing UploadWorker")
                // Enqueue skeleton worker with mock data
                repo.enqueueUpload(
                    "req-mock-001",
                    "doc-mock-001",
                    Uri.parse("file:///sdcard/Download/sample_document.pdf"),
                    "owner-mock-001"
                )
                submitting = true
            }
        }, enabled = !submitting, modifier = Modifier.fillMaxWidth()) { Text("Submit (Mock)") }

        if (submitting) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(top = 12.dp))
            LaunchedEffect(Unit) {
                delay(1000)
                Toast.makeText(ctx, "Document submitted for admin review", Toast.LENGTH_SHORT).show()
                submitting = false
                onSubmit()
            }
        }
    }
}
