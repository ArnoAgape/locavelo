package com.arnoagape.lokavelo.ui.screen.main.home

import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arnoagape.lokavelo.R
import com.arnoagape.lokavelo.domain.model.Bike
import com.arnoagape.lokavelo.ui.common.EventsEffect
import com.arnoagape.lokavelo.ui.screen.main.home.components.OSMMap
import com.arnoagape.lokavelo.ui.screen.main.home.components.SearchBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel
) {

    val state by viewModel.state.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val resources = LocalResources.current
    val context = LocalContext.current

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var showAddressSheet by remember { mutableStateOf(false) }

    val locationPermissionState =
        rememberPermissionState(
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

    LaunchedEffect(locationPermissionState.status) {
        if (locationPermissionState.status.isGranted) {
            viewModel.userLocation
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    EventsEffect(viewModel.eventsFlow) { event ->
        when (event) {
            is HomeEvent.ShowMessage -> {
                snackbarHostState.showSnackbar(
                    message = resources.getString(event.message),
                    duration = SnackbarDuration.Short
                )
            }

            is HomeEvent.ShowSuccessMessage -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    if (showAddressSheet) {

        ModalBottomSheet(
            onDismissRequest = { showAddressSheet = false }
        ) {

            var text by remember { mutableStateOf("") }

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
                keyboardController?.show()
            }

            Column(Modifier.padding(16.dp)) {

                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        viewModel.onAddressQueryChange(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    label = { Text(stringResource(R.string.city)) },
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                state.suggestions.forEach { suggestion ->

                    Text(
                        text = suggestion.displayName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.updateAddressFromSuggestion(suggestion)
                                keyboardController?.hide()
                                showAddressSheet = false
                            }
                            .padding(vertical = 14.dp)
                    )
                }
            }
        }
    }

    Box(Modifier.fillMaxSize()) {

        // 🗺 Carte
        MapContent(
            userLocation = userLocation,
            bikes = state.filteredBikes,
            state = state
        )

        // 🔎 Barre + Dates
        Column {

            SearchBar(
                filters = state.filters,
                onAddressClick = { showAddressSheet = true },
                onDatesSelected = { start, end ->
                    viewModel.updateDates(
                        start.atStartOfDay(),
                        end.atStartOfDay()
                    )
                }
            )
        }
    }
}

@Composable
fun MapContent(
    userLocation: Location?,
    bikes: List<Bike>,
    state: HomeScreenState
) {

    val geoPoint = userLocation?.let {
        org.osmdroid.util.GeoPoint(it.latitude, it.longitude)
    }

    OSMMap(
        userLocation = geoPoint,
        bikes = bikes,
        filters = state.filters
    )

}