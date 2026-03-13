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
fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(Modifier.height(16.dp))
            Text(stringResource(R.string.loading))
        }
    }
}

@Composable
fun DeletingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(Modifier.height(16.dp))
            Text(stringResource(R.string.deleting))
        }
    }
}

@Composable
fun ErrorOverlay(
    type: ErrorType,
    onRetry: () -> Unit = {}
) {

    val (message, imageRes) = when (type) {

        ErrorType.NETWORK -> Pair(
            stringResource(R.string.error_no_network),
            R.drawable.ic_bike_no_wifi
        )

        ErrorType.EMPTY_MESSAGES -> Pair(
            stringResource(R.string.empty_messaging),
            R.drawable.ic_bike_no_message
        )

        ErrorType.EMPTY_RENTALS -> Pair(
            stringResource(R.string.empty_messaging),
            R.drawable.ic_bike_no_message
        )

        ErrorType.GENERIC -> Pair(
            stringResource(R.string.error_generic),
            R.drawable.ic_bike_broken
        )
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

            if (type != ErrorType.EMPTY_MESSAGES && type != ErrorType.EMPTY_RENTALS) {

                Spacer(Modifier.height(12.dp))

                Button(onClick = onRetry) {
                    Text(stringResource(R.string.retry))
                }
            }
        }
    }
}

enum class ErrorType {
    NETWORK,
    EMPTY_MESSAGES,
    EMPTY_RENTALS,
    GENERIC
}

@PreviewLightDark
@Composable
private fun ErrorOverlayPreview() {
    LokaveloTheme {
        ErrorOverlay(
            type = ErrorType.EMPTY_RENTALS,
            onRetry = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun LoadingOverlayPreview() {
    LokaveloTheme {
        LoadingOverlay()
    }
}