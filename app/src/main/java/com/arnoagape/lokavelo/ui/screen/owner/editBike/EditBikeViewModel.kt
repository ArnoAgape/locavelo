package com.arnoagape.lokavelo.ui.screen.owner.editBike

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arnoagape.lokavelo.R
import com.arnoagape.lokavelo.data.repository.BikeOwnerRepository
import com.arnoagape.lokavelo.domain.model.BikeLocation
import com.arnoagape.lokavelo.ui.common.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for editing a new bike.
 *
 * Manages form state, validation, and user interactions
 * related to bike edition.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class EditBikeViewModel @Inject constructor(
    private val bikeRepository: BikeOwnerRepository
) : ViewModel() {

    private val _events = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = _events.receiveAsFlow()

    private val bikeId = MutableStateFlow<String?>(null)

    private val _state = MutableStateFlow(EditBikeScreenState())
    val state: StateFlow<EditBikeScreenState> = _state

    fun setBikeId(id: String) {
        bikeId.value = id
        observeBike()
    }

    private fun observeBike() {
        viewModelScope.launch {
            bikeId
                .filterNotNull()
                .flatMapLatest { id ->
                    bikeRepository.observeBike(id)
                }
                .collect { bike ->

                    if (bike == null) {
                        _state.updateState {
                            it.copy(uiState = EditBikeUiState.Error.NotFound)
                        }
                        return@collect
                    }

                    _state.updateState {
                        it.copy(
                            uiState = EditBikeUiState.Loaded(bike),
                            form = EditBikeFormState.fromBike(bike),
                            remotePhotoUrls = bike.photoUrls,
                            localUris = emptyList()
                        )
                    }
                }
        }
    }

    val hasUnsavedChanges: StateFlow<Boolean> =
        state.map { current ->

            val original =
                (current.uiState as? EditBikeUiState.Loaded)?.bike
                    ?: return@map false

            val formChanged =
                current.form != EditBikeFormState.fromBike(original)

            val photosChanged =
                current.localUris.isNotEmpty() ||
                        current.remotePhotoUrls != original.photoUrls

            formChanged || photosChanged

        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    fun onAction(event: EditBikeEvent) {

        when (event) {

            is EditBikeEvent.TitleChanged ->
                _state.updateState {
                    it.copy(form = it.form.copy(title = event.title))
                }

            is EditBikeEvent.DescriptionChanged ->
                _state.updateState {
                    it.copy(form = it.form.copy(description = event.description))
                }

            is EditBikeEvent.PriceChanged ->
                _state.updateState {
                    it.copy(form = it.form.copy(priceText = event.priceText))
                }

            is EditBikeEvent.DepositChanged ->
                _state.updateState {
                    it.copy(form = it.form.copy(depositText = event.depositText))
                }

            is EditBikeEvent.AddressChanged ->
                updateLocation { copy(street = event.address) }

            is EditBikeEvent.Address2Changed ->
                updateLocation { copy(addressLine2 = event.address2) }

            is EditBikeEvent.ZipChanged ->
                updateLocation { copy(postalCode = event.zipCode) }

            is EditBikeEvent.CityChanged ->
                updateLocation { copy(city = event.city) }

            is EditBikeEvent.ElectricChanged ->
                _state.updateState {
                    it.copy(form = it.form.copy(isElectric = event.isElectric))
                }

            is EditBikeEvent.CategoryChanged ->
                _state.updateState {
                    it.copy(form = it.form.copy(category = event.category))
                }

            is EditBikeEvent.BrandChanged ->
                _state.updateState {
                    it.copy(form = it.form.copy(brand = event.brand))
                }

            is EditBikeEvent.StateChanged ->
                _state.updateState {
                    it.copy(form = it.form.copy(condition = event.state))
                }

            is EditBikeEvent.AccessoriesChanged ->
                _state.updateState {
                    it.copy(form = it.form.copy(accessories = event.accessories))
                }

            is EditBikeEvent.AddPhoto ->
                _state.updateState {
                    it.copy(localUris = (it.localUris + event.uri).take(3))
                }

            is EditBikeEvent.RemovePhoto ->
                _state.updateState {
                    it.copy(localUris = it.localUris - event.uri)
                }

            is EditBikeEvent.RemoveRemotePhoto ->
                _state.updateState {
                    it.copy(
                        remotePhotoUrls = it.remotePhotoUrls - event.url
                    )
                }

            is EditBikeEvent.ReplacePhoto ->
                _state.updateState { photo ->
                    val updatedLocal =
                        photo.localUris.map { uri ->
                            if (uri == event.oldUri) event.newUri else uri
                        }

                    val updatedRemote =
                        photo.remotePhotoUrls.filterNot {
                            it == event.oldUri.toString()
                        }

                    photo.copy(
                        localUris =
                            if (updatedLocal.contains(event.newUri))
                                updatedLocal
                            else updatedLocal + event.newUri,
                        remotePhotoUrls = updatedRemote
                    )
                }

            EditBikeEvent.Submit ->
                editBike()
        }
    }

    private fun editBike() {

        viewModelScope.launch {

            val current = _state.value
            val original =
                (current.uiState as? EditBikeUiState.Loaded)?.bike
                    ?: return@launch

            val totalPhotos =
                current.localUris.size + current.remotePhotoUrls.size

            if (!current.form.isValid(totalPhotos)) {
                _events.trySend(Event.ShowMessage(R.string.error_invalid_form))
                return@launch
            }

            _state.update {
                it.copy(uiState = EditBikeUiState.Submitting)
            }

            val updatedBike =
                current.form.toUpdatedBikeOrNull(original)
                    ?: return@launch

            val finalBike = updatedBike.copy(
                photoUrls = current.remotePhotoUrls
            )

            runCatching {
                bikeRepository.editBike(
                    localUris = current.localUris,
                    bike = finalBike
                )
            }.onSuccess {

                _events.trySend(
                    Event.ShowSuccessMessage(R.string.success_bike_edited)
                )

            }.onFailure { throwable ->

                Log.e("EditBike", "Error while editing bike", throwable)

                _state.updateState {
                    it.copy(uiState = EditBikeUiState.Error.Generic())
                }

                _events.trySend(Event.ShowMessage(R.string.error_generic))
            }
        }
    }

    private fun updateLocation(update: BikeLocation.() -> BikeLocation) {
        _state.updateState {
            it.copy(
                form = it.form.copy(
                    location = it.form.location.update()
                )
            )
        }
    }

    private fun computeIsValid(state: EditBikeScreenState): Boolean {

        val totalPhotos = state.localUris.size + state.remotePhotoUrls.size

        return state.form.isValid(totalPhotos)
    }

    private fun MutableStateFlow<EditBikeScreenState>.updateState(
        reducer: (EditBikeScreenState) -> EditBikeScreenState
    ) {
        update { current ->
            val updated = reducer(current)
            updated.copy(isValid = computeIsValid(updated))
        }
    }
}

data class EditBikeScreenState(
    val uiState: EditBikeUiState = EditBikeUiState.Idle,
    val form: EditBikeFormState = EditBikeFormState(),
    val isValid: Boolean = false,
    val localUris: List<Uri> = emptyList(),
    val remotePhotoUrls: List<String> = emptyList()
)