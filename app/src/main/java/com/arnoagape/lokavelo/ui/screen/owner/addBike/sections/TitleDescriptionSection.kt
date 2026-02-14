package com.arnoagape.lokavelo.ui.screen.owner.addBike.sections

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.arnoagape.lokavelo.R

@Composable
fun TitleDescriptionSection(
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit
) {
    SectionCard(
        title = stringResource(R.string.title_description),
        subtitle = stringResource(R.string.subtitle_title_description)
    ) {

        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            label = { Text("Titre") }
        )

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            label = { Text("Description") },
            minLines = 3
        )
    }
}