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
import com.arnoagape.lokavelo.R
import com.arnoagape.lokavelo.domain.model.Bike

@Composable
fun BikeSpecsCard(bike: Bike) {

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = stringResource(R.string.characteristics),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(12.dp))

            SpecRow("Marque", bike.brand)

            bike.category?.let {
                SpecRow("Catégorie", it.name)
            }

            bike.condition?.let {
                SpecRow("État", it.name)
            }

            SpecRow("Électrique", if (bike.electric) "Oui" else "Non")

            if (bike.accessories.isNotEmpty()) {

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Accessoires",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(4.dp))

                bike.accessories.forEach {
                    Text("• ${it.name}")
                }
            }
        }
    }
}

@Composable
fun SpecRow(label: String, value: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(label)

        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}