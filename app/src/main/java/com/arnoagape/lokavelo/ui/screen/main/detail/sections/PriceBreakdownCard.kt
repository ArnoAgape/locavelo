package com.arnoagape.lokavelo.ui.screen.main.detail.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arnoagape.lokavelo.R
import com.arnoagape.lokavelo.ui.utils.toEuroString
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
fun PriceBreakdownCard(
    pricePerDayInCents: Long,
    startDate: LocalDate,
    endDate: LocalDate,
    serviceFeePercent: Int = 10
) {

    val days = ChronoUnit.DAYS.between(startDate, endDate).toInt().coerceAtLeast(1)

    val subtotal = pricePerDayInCents * days
    val serviceFee = subtotal * serviceFeePercent / 100
    val total = subtotal + serviceFee

    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRANCE)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = stringResource(
                R.string.price_per_day,
                pricePerDayInCents.toEuroString()
            ),
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "${startDate.format(dateFormatter)} - ${endDate.format(dateFormatter)}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.price_details),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(4.dp))

        PriceRow(
            label = "${pricePerDayInCents.toEuroString()} x $days jours",
            value = subtotal.toEuroString()
        )

        PriceRow(
            label = stringResource(
                R.string.service_fee_percent,
                serviceFeePercent
            ),
            value = serviceFee.toEuroString()
        )

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        PriceRow(
            label = stringResource(R.string.total_price_ttc),
            value = total.toEuroString(),
            isBold = true
        )
    }
}