package edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.data

import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.model.Plant
import java.util.concurrent.TimeUnit

interface WaterRepository {
    fun scheduleReminder(duration: Long, unit: TimeUnit, plantName: String)
    fun cancelReminder(plantName: String)
    val plants: List<Plant>
}
