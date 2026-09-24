package edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Plant(
    val id: String,
    @StringRes val name: Int,
    @StringRes val type: Int,
    @StringRes val description: Int,
    @StringRes val schedule: Int,
    val emoji: String,
    val recommendedIntervalDays: Int,
    @StringRes val sunlight: Int,
    @StringRes val soil: Int,
    @StringRes val funFact: Int,
    val customName: String? = null
) : Parcelable
