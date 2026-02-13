package com.arnoagape.lokavelo.ui.screen.owner.addBike

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arnoagape.lokavelo.R
import com.arnoagape.lokavelo.data.repository.BikeOwnerRepository
import com.arnoagape.lokavelo.data.repository.UserRepository
import com.arnoagape.lokavelo.domain.model.Bike
import com.arnoagape.lokavelo.domain.model.BikeCategory
import com.arnoagape.lokavelo.domain.model.BikeEquipment
import com.arnoagape.lokavelo.domain.model.BikeCondition
import com.arnoagape.lokavelo.ui.common.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBikeViewModel @Inject constructor(
    private val bikeRepository: BikeOwnerRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddBikeUiState>(AddBikeUiState.Idle)
    private val _events = Channel<Event>()
    val eventsFlow = _events.receiveAsFlow()

    private val _localUris = MutableStateFlow<List<Uri>>(emptyList())

    private val isSignedIn =
        userRepository.isUserSignedIn()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                false
            )

    private val _formState = MutableStateFlow(
        AddFormState(
            title = "",
            description = "",
            price = 0,
            deposit = 0,
            isElectric = false,
            category = BikeCategory.CITY,
            state = BikeCondition.GOOD,
            accessories = emptyList(),
            location = ""
        )
    )

    val state: StateFlow<AddBikeScreenState> =
        combine(
            _uiState,
            _formState,
            _localUris,
            isSignedIn
        ) { ui, form, uris, signedIn ->
            AddBikeScreenState(
                uiState = ui,
                bike = form.toBike(),
                localUris = uris,
                isValid = form.title.isNotBlank() && uris.isNotEmpty(),
                isSignedIn = signedIn
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AddBikeScreenState()
        )

    /**
     * Handles user actions modifying the file or selected URIs.
     */
    fun onAction(event: AddBikeEvent) {
        when (event) {

            is AddBikeEvent.TitleChanged ->
                _formState.update { it.copy(title = event.title) }

            is AddBikeEvent.DescriptionChanged ->
                _formState.update { it.copy(description = event.description) }

            is AddBikeEvent.PriceChanged ->
                _formState.update { it.copy(price = event.price) }

            is AddBikeEvent.DepositChanged ->
                _formState.update { it.copy(deposit = event.deposit) }

            is AddBikeEvent.LocationChanged ->
                _formState.update { it.copy(location = event.location) }

            is AddBikeEvent.ElectricChanged ->
                _formState.update { it.copy(isElectric = event.isElectric) }

            is AddBikeEvent.CategoryChanged ->
                _formState.update { it.copy(category = event.category) }

            is AddBikeEvent.BrandChanged ->
                _formState.update { it.copy(brand = event.brand) }

            is AddBikeEvent.StateChanged ->
                _formState.update { it.copy(state = event.state) }

            is AddBikeEvent.AccessoriesChanged ->
                _formState.update { it.copy(accessories = event.accessories) }

            is AddBikeEvent.AddPhoto ->
                _localUris.update { (it + event.uri).take(3) }

            is AddBikeEvent.RemovePhoto ->
                _localUris.update { it - event.uri }

            AddBikeEvent.Submit ->
                addBike()
        }
    }

    /**
     * Uploads the selected files to Firebase Storage and Firestore.
     * Performs network and authentication checks before uploading.
     */
    fun addBike() {
        viewModelScope.launch {

            val form = _formState.value
            val uris = _localUris.value

            if (
                form.title.isBlank() ||
                form.price <= 0 ||
                form.location.isBlank() ||
                uris.isEmpty()
            ) return@launch

            _uiState.value = AddBikeUiState.Loading

            runCatching {
                bikeRepository.addBike(
                    localUris = _localUris.value,
                    bike = _formState.value.toBike()
                )
            }.onSuccess {
                _uiState.value = AddBikeUiState.Success
                _localUris.value = emptyList()
                _formState.value = AddFormState()
                _events.trySend(Event.ShowSuccessMessage(R.string.success_bike_added))
            }.onFailure {
                _uiState.value = AddBikeUiState.Error.Generic()
                _events.trySend(Event.ShowMessage(R.string.error_generic))
            }
        }
    }
}

data class AddBikeScreenState(
    val uiState: AddBikeUiState = AddBikeUiState.Idle,
    val bike: Bike = Bike(),
    val isValid: Boolean = false,
    val localUris: List<Uri> = emptyList(),
    val isSignedIn: Boolean = false
)

data class AddFormState(
    val title: String = "",
    val description: String = "",
    val price: Long = 0L,
    val deposit: Long = 0L,
    val isElectric: Boolean = false,
    val category: BikeCategory? = null,
    val brand: String = "",
    val state: BikeCondition? = null,
    val accessories: List<BikeEquipment> = emptyList(),
    val location: String = ""
) {
    fun toBike(): Bike =
        Bike(
            title = title,
            description = description,
            price = price,
            deposit = deposit,
            isElectric = isElectric,
            category = category,
            brand = brand,
            condition = state,
            accessories = accessories,
            location = location
        )
}