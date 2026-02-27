package com.arnoagape.lokavelo.ui.screen.main.home

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arnoagape.lokavelo.data.repository.BikeOwnerRepository
import com.arnoagape.lokavelo.data.repository.LocationRepository
import com.arnoagape.lokavelo.ui.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val bikeRepository: BikeOwnerRepository,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _events = Channel<HomeEvent>(Channel.BUFFERED)
    val eventsFlow = _events.receiveAsFlow()

    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation = _userLocation.asStateFlow()

    fun fetchUserLocation() {
        viewModelScope.launch {
            _userLocation.value = locationRepository.getLastLocation()
        }
    }
}