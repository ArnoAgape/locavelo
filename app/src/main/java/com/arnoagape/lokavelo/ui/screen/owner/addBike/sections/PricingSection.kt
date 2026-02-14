package com.arnoagape.lokavelo.ui.screen.owner.addBike.sections

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.arnoagape.lokavelo.R

@Composable
fun PricingSection(
    price: String,
    onPriceChange: (String) -> Unit,
) {
    SectionCard(
        title = stringResource(R.string.pricing),
        subtitle = stringResource(R.string.subtitle_pricing)
    ) {

        OutlinedTextField(
            value = price,
            onValueChange = onPriceChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            label = { Text(stringResource(R.string.pricing_day)) }
        )

        Text(
            text = stringResource(R.string.coming_soon_pricing),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}