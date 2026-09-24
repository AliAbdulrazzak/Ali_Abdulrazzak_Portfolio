package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Hamburger::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun hamburgerDao(): HamburgerDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "hamburger_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
