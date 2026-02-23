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
    titleError: Boolean,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit
) {
    SectionCard(
        title = stringResource(R.string.title_description),
        subtitle = stringResource(R.string.subtitle_title_description)
    ) {


        OutlinedTextField(
            value = title,
            onValueChange = { onTitleChange(it) },
            label = { Text(stringResource(R.string.title)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            isError = titleError,
            supportingText = {
                if (titleError) {
                    Text(stringResource(R.string.required))
                }
            }
        )

        OutlinedTextField(
            value = description,
            onValueChange = { onDescriptionChange(it) },
            label = { Text(stringResource(R.string.description)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            minLines = 3
        )

    }
}