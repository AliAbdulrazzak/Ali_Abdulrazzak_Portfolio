package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data

import android.content.Context

interface HamburgerAppContainer {
    val hamburgerRepository: HamburgerRepository
}

class HamburgerAppDataContainer(private val context: Context) : HamburgerAppContainer {
    override val hamburgerRepository: HamburgerRepository by lazy {
        RoomHamburgerRepository(AppDatabase.getDatabase(context).hamburgerDao())
    }
}
