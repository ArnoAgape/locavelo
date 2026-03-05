package com.arnoagape.lokavelo.ui.screen.main.detail.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arnoagape.lokavelo.domain.model.Bike
import com.arnoagape.lokavelo.R

@Composable
fun PriceTableCard(bike: Bike) {

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = stringResource(R.string.pricing),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(12.dp))

            PriceRow("Journée", bike.priceInCents)

            bike.priceHalfDayInCents?.let {
                PriceRow("Demi-journée", it)
            }

            bike.priceWeekInCents?.let {
                PriceRow("Semaine", it)
            }

            bike.priceMonthInCents?.let {
                PriceRow("Mois", it)
            }

            bike.depositInCents?.let {

                Spacer(Modifier.height(8.dp))

                PriceRow("Caution", it)
            }
        }
    }
}

@Composable
fun PriceRow(label: String, price: Long) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(label)

        Text(
            text = "${price / 100} €",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}