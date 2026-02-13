package com.arnoagape.lokavelo.ui.screen.owner.addBike.sections

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.arnoagape.lokavelo.R

@Composable
fun PricingSection(
    price: Long,
    onPriceChange: (String) -> Unit,
) {
    SectionCard(
        title = stringResource(R.string.pricing),
        subtitle = stringResource(R.string.subtitle_pricing)
    ) {

        OutlinedTextField(
            value = price.toString(),
            onValueChange = onPriceChange,
            label = { Text(stringResource(R.string.pricing_day)) }
        )

        Text(
            text = stringResource(R.string.coming_soon_pricing),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}