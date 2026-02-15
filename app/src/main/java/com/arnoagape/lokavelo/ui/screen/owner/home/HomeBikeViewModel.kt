package com.arnoagape.lokavelo.ui.screen.owner.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arnoagape.lokavelo.R
import com.arnoagape.lokavelo.data.repository.BikeOwnerRepository
import com.arnoagape.lokavelo.data.repository.UserRepository
import com.arnoagape.lokavelo.domain.model.Bike
import com.arnoagape.lokavelo.ui.common.Event
import com.arnoagape.lokavelo.ui.common.SelectionState
import com.arnoagape.lokavelo.ui.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for the owner home bike screen.
 * Handles bike listing and user actions related to a bike.
 */
@HiltViewModel
class HomeBikeViewModel @Inject constructor(
    private val bikeRepository: BikeOwnerRepository,
    userRepository: UserRepository,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _events = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = _events.receiveAsFlow()

    private val _selection = MutableStateFlow(SelectionState())
    private val _isRefreshing = MutableStateFlow(false)

    private val bikesFlow: Flow<List<Bike>> = bikeRepository.observeBikes()

    val isSignedIn =
        userRepository.isUserSignedIn()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null
            )

    private val _uiState: Flow<HomeBikeUiState> =
        bikesFlow
            .map { bikes ->
                if (bikes.isEmpty())
                    HomeBikeUiState.Empty()
                else
                    HomeBikeUiState.Success(bikes)
            }
            .catch { _ ->
                emit(HomeBikeUiState.Error.Generic())
            }

    val state: StateFlow<HomeBikeScreenState> =
        combine(
            _uiState,
            _isRefreshing,
            _selection
        ) { ui, refreshing, selection ->
            HomeBikeScreenState(
                uiState = ui,
                isRefreshing = refreshing,
                selection = selection
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HomeBikeScreenState()
        )

    fun enterSelectionMode() {
        _selection.update { it.copy(isSelectionMode = true) }
    }

    fun exitSelectionMode() {
        _selection.value = SelectionState() // reset
    }

    fun toggleSelection(id: String) {
        _selection.update { sel ->
            val set = sel.selectedIds.toMutableSet()
            if (!set.add(id)) set.remove(id)
            sel.copy(selectedIds = set)
        }
    }

    fun deleteSelectedMedicines() {
        viewModelScope.launch {
            val result = bikeRepository.deleteBikes(_selection.value.selectedIds)

            if (result.isSuccess) {
                exitSelectionMode()
                _events.trySend(Event.ShowSuccessMessage(R.string.success_bike_deleted))
            } else {
                _events.trySend(Event.ShowMessage(R.string.error_delete_bike))
            }
        }
    }

    fun refreshMedicines() {
        if (!networkUtils.isNetworkAvailable()) {
            _events.trySend(Event.ShowMessage(R.string.error_no_network))
            return
        }

        viewModelScope.launch {
            _isRefreshing.value = true
            delay(700)
            _isRefreshing.value = false
        }
    }

}

data class HomeBikeScreenState(
    val uiState: HomeBikeUiState = HomeBikeUiState.Loading,
    val isRefreshing: Boolean = false,
    val selection: SelectionState = SelectionState()
)