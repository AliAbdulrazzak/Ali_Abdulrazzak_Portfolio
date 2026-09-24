package edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.data

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.model.Plant
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.worker.WaterReminderWorker
import java.util.concurrent.TimeUnit

class WorkManagerWaterRepository(context: Context) : WaterRepository {
    private val workManager = WorkManager.getInstance(context)

    override val plants: List<Plant>
        get() = DataSource.plants

    override fun scheduleReminder(duration: Long, unit: TimeUnit, plantName: String) {
        val data = Data.Builder()
            .putString(WaterReminderWorker.nameKey, plantName)
            .build()

        val work = OneTimeWorkRequestBuilder<WaterReminderWorker>()
            .setInitialDelay(duration, unit)
            .setInputData(data)
            .addTag(plantName)
            .build()

        workManager.enqueueUniqueWork(
            plantName,
            ExistingWorkPolicy.REPLACE,
            work
        )
    }

    override fun cancelReminder(plantName: String) {
        workManager.cancelUniqueWork(plantName)
    }
}
