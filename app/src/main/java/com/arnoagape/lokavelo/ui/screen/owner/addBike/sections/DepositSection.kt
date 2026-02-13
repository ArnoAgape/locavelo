package com.arnoagape.lokavelo.ui.screen.owner.addBike.sections

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.arnoagape.lokavelo.R

@Composable
fun DepositSection(
    deposit: Long,
    onDepositChange: (String) -> Unit,
) {
    SectionCard(
        title = stringResource(R.string.deposit),
        subtitle = stringResource(R.string.subtitle_deposit)
    ) {

        OutlinedTextField(
            value = deposit.toString(),
            onValueChange = onDepositChange,
            label = { Text(stringResource(R.string.deposit_amount)) }
        )
    }

}