package com.arnoagape.lokavelo.ui.screen.owner.addBike.sections

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arnoagape.lokavelo.R
import com.arnoagape.lokavelo.ui.theme.LokaveloTheme

@Composable
fun PublishButton(
    isSubmitting: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isSubmitting,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .navigationBarsPadding(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = stringResource(R.string.publish_bike),
            fontSize = 15.sp)
    }
}

@PreviewLightDark
@Composable
private fun PublishButtonPreview() {
    LokaveloTheme {
        PublishButton(
            enabled = true,
            onClick = {},
            isSubmitting = false
        )
    }
}