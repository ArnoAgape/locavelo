package com.arnoagape.lokavelo.ui.utils

fun calculateDiscountPercent(
    basePerDayCents: Long,
    discountedCents: Long,
    days: Int
): Int {

    val normal = basePerDayCents * days
    if (normal == 0L) return 0

    val ratio = discountedCents.toDouble() / normal.toDouble()
    return (100 - (ratio * 100))
        .toInt()
        .coerceIn(0, 100)
}

fun calculateRentalPrice(
    dayPrice: Long,
    twoDaysPrice: Long?,
    weekPrice: Long?,
    monthPrice: Long?,
    days: Int
): Long {

    var remaining = days
    var total = 0L

    // mois
    if (monthPrice != null && monthPrice > 0) {
        val months = remaining / 30
        total += months * monthPrice
        remaining %= 30
    }

    // semaine
    if (weekPrice != null && weekPrice > 0) {
        val weeks = remaining / 7
        total += weeks * weekPrice
        remaining %= 7
    }

    // bloc 2 jours
    if (twoDaysPrice != null && twoDaysPrice > 0) {
        val blocks = remaining / 2
        total += blocks * twoDaysPrice
        remaining %= 2
    }

    // jours restants
    total += remaining * dayPrice

    return total
}