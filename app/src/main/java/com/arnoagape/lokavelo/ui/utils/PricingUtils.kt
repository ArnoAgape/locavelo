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

fun calculateTotalPrice(
    days: Int,
    dayPrice: Long,
    weekPrice: Long?,
    monthPrice: Long?
): Long {

    var remainingDays = days
    var total = 0L

    // Mois
    if (monthPrice != null && remainingDays >= 30) {
        val months = remainingDays / 30
        total += months * monthPrice
        remainingDays %= 30
    }

    // Semaines
    if (weekPrice != null && remainingDays >= 7) {
        val weeks = remainingDays / 7
        total += weeks * weekPrice
        remainingDays %= 7
    }

    // Jours restants
    total += remainingDays * dayPrice

    return total
}