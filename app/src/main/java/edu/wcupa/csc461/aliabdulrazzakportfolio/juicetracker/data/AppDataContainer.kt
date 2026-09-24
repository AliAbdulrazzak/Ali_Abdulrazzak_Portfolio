package edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.data

import android.content.Context

class AppDataContainer(private val context: Context) : AppContainer {
    override val trackerRepository: JuiceRepository by lazy {
        RoomJuiceRepository(AppDatabase.getDatabase(context).juiceDao())
    }
}
