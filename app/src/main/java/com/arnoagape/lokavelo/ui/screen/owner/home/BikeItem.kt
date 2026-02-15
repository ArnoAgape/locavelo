package com.arnoagape.lokavelo.ui.screen.owner.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.arnoagape.lokavelo.domain.model.Bike
import com.arnoagape.lokavelo.ui.common.SelectionState
import com.arnoagape.lokavelo.ui.common.components.SelectItemRow
import com.arnoagape.lokavelo.ui.theme.LokaveloTheme
import java.text.NumberFormat
import java.util.Locale

/**
 * Displays a scrollable list of bikes with optional selection mode.
 *
 * Supports item selection and navigation to bike details.
 */
@Composable
fun BikeItem(
    modifier: Modifier = Modifier,
    bikes: List<Bike>,
    onBikeClick: (Bike) -> Unit,
    selectionState: SelectionState,
    onToggleSelection: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(bikes) { bike ->
            SelectItemRow(
                id = bike.id,
                isSelectionMode = selectionState.isSelectionMode,
                isSelected = selectionState.selectedIds.contains(bike.id),
                onSelectToggle = { onToggleSelection(bike.id) },
                onClick = { onBikeClick(bike) }
            ) {
                Column {
                    Text(
                        text = bike.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    val formattedPrice = remember(bike.priceInCents) {
                        val priceInEuros = bike.priceInCents / 100.0
                        NumberFormat
                            .getCurrencyInstance(Locale.getDefault())
                            .format(priceInEuros)
                    }

                    Text(text = formattedPrice)

                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun BikeItemPreview() {
    LokaveloTheme {
        BikeItem(
            bikes = listOf(
                Bike(
                    title = "Vélo gravel Origine",
                    priceInCents = 2500
                ),
                Bike(
                    title = "Vélo VTT Rockrider",
                    priceInCents = 3000
                ),
            ),
            onBikeClick = {},
            selectionState = SelectionState(),
            onToggleSelection = {}
        )
    }
}