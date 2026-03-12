package com.arnoagape.lokavelo.domain.utils

object ConversationIdBuilder {

    fun build(
        bikeId: String,
        ownerId: String,
        ownerName: String,
        renterId: String,
        renterName: String,
        start: Long,
        end: Long
    ): String {
        return "${bikeId}_${ownerId}_${ownerName}_${renterId}_${renterName}_${start}_${end}"
    }
}