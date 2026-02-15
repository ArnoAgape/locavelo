package com.arnoagape.lokavelo.ui.screen.owner.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arnoagape.lokavelo.R
import com.arnoagape.lokavelo.domain.model.Bike
import com.arnoagape.lokavelo.ui.common.Event
import com.arnoagape.lokavelo.ui.common.EventsEffect
import com.arnoagape.lokavelo.ui.common.components.ConfirmDeleteDialog
import com.arnoagape.lokavelo.ui.theme.LokaveloTheme

@Composable
fun HomeBikeScreen(
    viewModel: HomeBikeViewModel,
    onBikeClick: (Bike) -> Unit,
    onFABClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isSignedIn by viewModel.isSignedIn.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val resources = LocalResources.current
    val context = LocalContext.current

    EventsEffect(viewModel.eventsFlow) { event ->
        when (event) {
            is Event.ShowMessage -> {
                snackbarHostState.showSnackbar(
                    message = resources.getString(event.message),
                    duration = SnackbarDuration.Short
                )
            }

            is Event.ShowSuccessMessage -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    HomeBikeContent(
        state = state,
        onBikeClick = onBikeClick,
        onFABClick = onFABClick,
        onRefresh = { viewModel.refreshMedicines() },
        isSignedIn = isSignedIn == true,
        onToggleSelection = { viewModel.toggleSelection(it) },
        onEnterSelectionMode = { viewModel.enterSelectionMode() },
        onExitSelectionMode = { viewModel.exitSelectionMode() },
        onDeleteSelected = { viewModel.deleteSelectedMedicines() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBikeContent(
    state: HomeBikeScreenState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBikeClick: (Bike) -> Unit,
    onFABClick: () -> Unit,
    onRefresh: () -> Unit,
    isSignedIn: Boolean,
    onToggleSelection: (String) -> Unit,
    onEnterSelectionMode: () -> Unit,
    onExitSelectionMode: () -> Unit,
    onDeleteSelected: () -> Unit
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val refreshState = rememberPullToRefreshState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.rentals)) },
                actions = {
                    if (state.selection.isSelectionMode) {
                        IconButton(
                            onClick = {
                                if (state.selection.selectedIds.isEmpty()) {
                                    onExitSelectionMode()
                                } else {
                                    showDeleteDialog = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (state.selection.selectedIds.isEmpty())
                                    Icons.Default.Close
                                else
                                    Icons.Default.DeleteForever,
                                contentDescription = stringResource(R.string.cd_button_delete)
                            )
                        }
                    } else if (state.uiState is HomeBikeUiState.Success) {
                        IconButton(onClick = onEnterSelectionMode) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.cd_button_delete_bike)
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                onClick = {
                    if (isSignedIn) onFABClick()
                    else Toast.makeText(
                        context,
                        resources.getString(R.string.error_no_account_add_bike),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.cd_button_add_bike)
                )
            }
        }
    ) { contentPadding ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            state = refreshState,
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh
        ) {
            when (val ui = state.uiState) {

                is HomeBikeUiState.Success -> {
                    BikeItem(
                        bikes = ui.bikes,
                        onBikeClick = onBikeClick,
                        selectionState = state.selection,
                        onToggleSelection = onToggleSelection
                    )
                }

                is HomeBikeUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is HomeBikeUiState.Empty -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.error_bike_not_found),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is HomeBikeUiState.Error.Generic -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.error_loading_bike),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        ConfirmDeleteDialog(
            show = showDeleteDialog,
            onConfirm = {
                showDeleteDialog = false
                onDeleteSelected()
            },
            onDismiss = { showDeleteDialog = false },
            confirmButtonTitle = stringResource(R.string.confirm_delete_bike),
            confirmButtonMessage = stringResource(R.string.confirm_delete_message_bikes)
        )
    }
}

@PreviewLightDark
@Composable
fun HomeBikeScreenPreview() {
    LokaveloTheme {

        val sampleBikes = listOf(
            Bike(title = "Vélo gravel Origine", priceInCents = 2500),
            Bike(title = "Vélo VTT Rockrider", priceInCents = 1000),
            Bike(title = "Vélo randonneuse Riverside", priceInCents = 2000)
        )

        val previewState = HomeBikeScreenState(
            uiState = HomeBikeUiState.Success(sampleBikes),
            isRefreshing = false
        )

        HomeBikeContent(
            state = previewState,
            onBikeClick = {},
            onFABClick = {},
            onRefresh = {},
            isSignedIn = true,
            onToggleSelection = {},
            onEnterSelectionMode = {},
            onExitSelectionMode = {},
            onDeleteSelected = {}
        )
    }
}