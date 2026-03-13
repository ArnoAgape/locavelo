package com.arnoagape.lokavelo.ui.screen.main.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arnoagape.lokavelo.data.repository.BikeRepository
import com.arnoagape.lokavelo.data.repository.ConversationRepository
import com.arnoagape.lokavelo.data.repository.UserRepository
import com.arnoagape.lokavelo.domain.model.Bike
import com.arnoagape.lokavelo.domain.model.Conversation
import com.arnoagape.lokavelo.domain.model.Message
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ContactViewModel @Inject constructor(
    private val bikeRepository: BikeRepository,
    private val conversationRepository: ConversationRepository,
    userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _bikeId = MutableStateFlow<String?>(null)
    private val currentUserId = requireNotNull(auth.currentUser?.uid)

    val currentUser =
        userRepository.observeUser(currentUserId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null
            )
    private val _conversationId = MutableStateFlow<String?>(null)

    val conversation: StateFlow<Conversation?> =
        _conversationId
            .filterNotNull()
            .flatMapLatest {
                conversationRepository.observeConversation(it)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null
            )

    val bike: StateFlow<Bike?> =
        _bikeId
            .filterNotNull()
            .flatMapLatest { bikeRepository.observeBike(it) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null
            )

    fun setBikeId(id: String) {
        _bikeId.value = id
    }

    private val _openConversation = MutableSharedFlow<String>()
    val openConversation = _openConversation.asSharedFlow()

    fun sendMessage(
        text: String,
        startDate: LocalDate,
        endDate: LocalDate
    ) {

        val bike = bike.value ?: return
        val renterId = auth.currentUser?.uid ?: return
        val renterName = currentUser.value?.displayName ?: "Utilisateur"

        if (bike.ownerId == renterId) return

        viewModelScope.launch {

            val conversation = conversationRepository.getOrCreateConversation(
                bikeId = bike.id,
                ownerId = bike.ownerId,
                ownerName = bike.ownerName,
                renterId = renterId,
                renterName = renterName,
                startDate = startDate,
                endDate = endDate
            )

            conversationRepository.sendMessage(
                conversationId = conversation.id,
                message = Message(
                    conversationId = conversation.id,
                    senderId = renterId,
                    text = text,
                    createdAt = System.currentTimeMillis()
                )
            )

            _openConversation.emit(conversation.id)
        }
    }
}