package edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import edu.wcupa.csc461.aliabdulrazzakportfolio.R

class WaterReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val plantName = inputData.getString(nameKey)

        makePlantReminderNotification(
            applicationContext.resources.getString(R.string.waterme_time_to_water, plantName),
            applicationContext
        )

        return Result.success()
    }

    companion object {
        const val nameKey = "NAME"
    }
}
