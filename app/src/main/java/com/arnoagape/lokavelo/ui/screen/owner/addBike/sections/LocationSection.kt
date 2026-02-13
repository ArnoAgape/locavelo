package com.arnoagape.lokavelo.ui.screen.owner.addBike.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arnoagape.lokavelo.R

@Composable
fun LocationSection(
    location: String,
    onLocationChange: (String) -> Unit
) {
    SectionCard(
        title = stringResource(R.string.location),
        subtitle = stringResource(R.string.subtitle_location)
    ) {

        //AddressLineField()
        //AddressLine2Field()
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            //CityField()
            //ZipCodeField()
        }
    }
}