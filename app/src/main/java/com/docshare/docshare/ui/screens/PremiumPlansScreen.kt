package com.docshare.docshare.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PremiumPlansScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Premium Plans", style = MaterialTheme.typography.titleLarge)
        Text(text = "Free (current)", fontWeight = FontWeight.Bold)
        Text(text = "Basic")
        Text(text = "Pro")

        Text(text = "Back", modifier = Modifier.padding(top = 12.dp).clickable { onBack() })
    }
}
