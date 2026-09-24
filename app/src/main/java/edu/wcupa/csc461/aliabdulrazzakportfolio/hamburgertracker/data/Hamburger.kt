package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hamburgers")
data class Hamburger(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val restaurantName: String = "",
    val burgerType: String = BurgerType.CLASSIC.name,
    val rating: Int = 0,
    val price: Double = 0.0,
    val isFavorite: Boolean = false,
    val dateAdded: Long = System.currentTimeMillis()
)

enum class BurgerType(val label: String, val emoji: String) {
    CLASSIC("Classic", "\uD83C\uDF54"),
    BBQ("BBQ", "\uD83E\uDD69"),
    VEGGIE("Veggie", "\uD83E\uDD57"),
    SPICY("Spicy", "\uD83C\uDF36\uFE0F"),
    GOURMET("Gourmet", "\u2728"),
    DOUBLE("Double Stack", "\uD83C\uDF54\uD83C\uDF54"),
    BREAKFAST("Breakfast", "\uD83C\uDF73"),
    SPECIALTY("Specialty", "\u2B50")
}
