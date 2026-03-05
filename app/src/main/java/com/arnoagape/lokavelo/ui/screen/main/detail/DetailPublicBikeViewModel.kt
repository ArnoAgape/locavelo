package com.arnoagape.lokavelo.ui.screen.main.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arnoagape.lokavelo.data.repository.BikeRepository
import com.arnoagape.lokavelo.data.repository.UserRepository
import com.arnoagape.lokavelo.domain.model.Bike
import com.arnoagape.lokavelo.domain.model.Rental
import com.arnoagape.lokavelo.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DetailPublicBikeViewModel @Inject constructor(
    private val bikeRepository: BikeRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _bikeId = MutableStateFlow<String?>(null)

    fun setBikeId(id: String) {
        _bikeId.value = id
    }

    val state: StateFlow<DetailPublicBikeState> =
        _bikeId
            .filterNotNull()
            .flatMapLatest { bikeId ->
                bikeRepository.observeBike(bikeId)
            }
            .flatMapLatest { bike ->

                if (bike == null) {
                    flowOf(DetailPublicBikeState())
                } else {

                    userRepository.observeUser(bike.ownerId)
                        .map { owner ->

                            DetailPublicBikeState(
                                bike = bike,
                                owner = owner,
                                isLoading = false
                            )
                        }
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                DetailPublicBikeState()
            )
}

data class DetailPublicBikeState(
    val bike: Bike? = null,
    val owner: User? = null,
    val isLoading: Boolean = true,
    val rentals: List<Rental> = emptyList(),
    val error: Throwable? = null
)