package com.arnoagape.lokavelo.ui.screen.main.detail.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PriceRow(
    label: String,
    value: String,
    isBold: Boolean = false
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            style = if (isBold)
                MaterialTheme.typography.titleMedium
            else
                MaterialTheme.typography.bodyMedium
        )

        Text(
            text = value,
            style = if (isBold)
                MaterialTheme.typography.titleMedium
            else
                MaterialTheme.typography.bodyMedium
        )
    }
}