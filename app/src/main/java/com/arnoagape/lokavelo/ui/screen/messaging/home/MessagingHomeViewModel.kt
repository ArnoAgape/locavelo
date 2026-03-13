package com.arnoagape.lokavelo.ui.screen.messaging.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arnoagape.lokavelo.data.repository.BikeRepository
import com.arnoagape.lokavelo.data.repository.ConversationRepository
import com.arnoagape.lokavelo.domain.model.Bike
import com.arnoagape.lokavelo.domain.model.Conversation
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MessagingHomeViewModel @Inject constructor(
    conversationRepository: ConversationRepository,
    private val bikeRepository: BikeRepository,
    auth: FirebaseAuth
) : ViewModel() {

    private val currentUserId = requireNotNull(auth.currentUser?.uid)
    private val uiState: Flow<MessagingHomeUiState> =
        conversationRepository
            .observeUserConversations(currentUserId)
            .flatMapLatest { conversations ->

                if (conversations.isEmpty()) {
                    flowOf(MessagingHomeUiState.Empty())
                } else {

                    combine(
                        conversations.map { conversation ->

                            bikeRepository
                                .observeBike(conversation.bikeId)
                                .map { bike ->

                                    ConversationItemScreen(
                                        conversation = conversation,
                                        bike = bike,
                                        displayName =
                                            if (conversation.ownerId == currentUserId)
                                                conversation.renterName
                                            else
                                                conversation.ownerName,
                                        lastMessage = conversation.lastMessage,
                                        lastMessageTime = conversation.lastMessageTime,
                                        isOwner = conversation.ownerId == currentUserId,
                                        unreadCount = conversation.unreadCount[currentUserId] ?: 0
                                    )
                                }
                        }
                    ) { list ->
                        MessagingHomeUiState.Success(list.toList())
                    }
                }
            }
            .onStart {
                emit(MessagingHomeUiState.Loading)
            }
            .catch { e ->
                if (e is com.google.firebase.firestore.FirebaseFirestoreException
                    && e.code == com.google.firebase.firestore.FirebaseFirestoreException.Code.FAILED_PRECONDITION) {
                    emit(MessagingHomeUiState.Empty())
                } else {
                    emit(MessagingHomeUiState.Error.Generic())
                }
            }

    val state: StateFlow<MessagingHomeUiState> =
        uiState.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            MessagingHomeUiState.Loading
        )

    val unreadCount: StateFlow<Int> =
        conversationRepository
            .observeUnreadCount(currentUserId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                0
            )
}

data class ConversationItemScreen(
    val conversation: Conversation,
    val bike: Bike?,
    val displayName: String?,
    val lastMessage: String,
    val lastMessageTime: Long,
    val isOwner: Boolean,
    val unreadCount: Int
)