package com.arnoagape.lokavelo.ui.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.arnoagape.lokavelo.R
import com.arnoagape.lokavelo.ui.theme.LokaveloTheme

@Composable
fun LoadingOverlay(text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(Modifier.height(16.dp))
            Text(text)
        }
    }
}

@Composable
fun ErrorOverlay(
    isNetworkError: Boolean,
    onRetry: () -> Unit
) {

    val message = if (isNetworkError) {
        stringResource(R.string.error_no_network)
    } else {
        stringResource(R.string.error_generic)
    }

    val imageRes = if (isNetworkError) {
        R.drawable.ic_bike_no_wifi
    } else {
        R.drawable.ic_bike_broken
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                modifier = Modifier.height(140.dp)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onRetry
            ) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ErrorOverlayPreview() {
    LokaveloTheme {
        ErrorOverlay(
            isNetworkError = true,
            onRetry = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun LoadingOverlayPreview() {
    LokaveloTheme {
        LoadingOverlay(
            text = "Une erreur inconnue est survenue"
        )
    }
}