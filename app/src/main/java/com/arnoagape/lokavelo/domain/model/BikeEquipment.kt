package com.arnoagape.lokavelo.domain.model

import androidx.annotation.StringRes
import com.arnoagape.lokavelo.R

enum class BikeEquipment {
    BELL,                   // Sonnette
    LOCK,                   // Antivol
    MUDGUARD,               // Garde-boue
    PHONE_MOUNT,            // Support téléphone
    BACKPACK,               // Sac à dos
    PUMP,                   // Pompe
    HELMET,                 // Casque
    SADDLE_BAG,             // Sacoche selle
    WHEEL_LOCK,             // Antivol roue
    GLOVES,                 // Gants
    SPEEDOMETER,            // Compteur vitesse
    HANDLEBAR_BAG,          // Sacoche guidon
    REFLECTORS,             // Réflecteurs
    REPAIR_KIT,             // Kit réparation
    REFLECTIVE_VEST,        // Gilet réfléchissant
    FRONT_LIGHT,            // Lumière avant
    REAR_LIGHT,             // Lumière arrière
    BASKET,                 // Panier
    KICKSTAND,              // Béquille
    REAR_RACK,              // Porte-bagage
    PUNCTURE_RESISTANT_TIRE,// Pneu anti-crevaison
    TENT,                   // Tente
    TIRE_LEVERS,            // Démonte-pneus
    BOTTLE_CAGE,            // Porte-bidon
    PANNIERS                // Sacoches
}

@StringRes
fun BikeEquipment.labelRes(): Int =
    when (this) {
        BikeEquipment.BELL -> R.string.bike_equipment_bell
        BikeEquipment.LOCK -> R.string.bike_equipment_lock
        BikeEquipment.MUDGUARD -> R.string.bike_equipment_mudguard
        BikeEquipment.PHONE_MOUNT -> R.string.bike_equipment_phone_mount
        BikeEquipment.BACKPACK -> R.string.bike_equipment_backpack
        BikeEquipment.PUMP -> R.string.bike_equipment_pump
        BikeEquipment.HELMET -> R.string.bike_equipment_helmet
        BikeEquipment.SADDLE_BAG -> R.string.bike_equipment_saddle_bag
        BikeEquipment.WHEEL_LOCK -> R.string.bike_equipment_wheel_lock
        BikeEquipment.GLOVES -> R.string.bike_equipment_gloves
        BikeEquipment.SPEEDOMETER -> R.string.bike_equipment_speedometer
        BikeEquipment.HANDLEBAR_BAG -> R.string.bike_equipment_handlebar_bag
        BikeEquipment.REFLECTORS -> R.string.bike_equipment_reflectors
        BikeEquipment.REPAIR_KIT -> R.string.bike_equipment_repair_kit
        BikeEquipment.REFLECTIVE_VEST -> R.string.bike_equipment_reflective_vest
        BikeEquipment.FRONT_LIGHT -> R.string.bike_equipment_front_light
        BikeEquipment.REAR_LIGHT -> R.string.bike_equipment_rear_light
        BikeEquipment.BASKET -> R.string.bike_equipment_basket
        BikeEquipment.KICKSTAND -> R.string.bike_equipment_kickstand
        BikeEquipment.REAR_RACK -> R.string.bike_equipment_rear_rack
        BikeEquipment.PUNCTURE_RESISTANT_TIRE -> R.string.bike_equipment_puncture_resistant_tire
        BikeEquipment.TENT -> R.string.bike_equipment_tent
        BikeEquipment.TIRE_LEVERS -> R.string.bike_equipment_tire_levers
        BikeEquipment.BOTTLE_CAGE -> R.string.bike_equipment_bottle_cage
        BikeEquipment.PANNIERS -> R.string.bike_equipment_panniers
    }