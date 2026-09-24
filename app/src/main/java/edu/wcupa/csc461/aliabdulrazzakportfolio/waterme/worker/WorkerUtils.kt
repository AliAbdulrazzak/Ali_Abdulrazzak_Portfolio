package edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import edu.wcupa.csc461.aliabdulrazzakportfolio.MainActivity
import edu.wcupa.csc461.aliabdulrazzakportfolio.R
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.CHANNEL_ID
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.NOTIFICATION_ID
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.NOTIFICATION_TITLE
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.REQUEST_CODE
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.VERBOSE_NOTIFICATION_CHANNEL_NAME

fun makePlantReminderNotification(
    message: String,
    context: Context
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(
            CHANNEL_ID,
            VERBOSE_NOTIFICATION_CHANNEL_NAME,
            importance
        )
        channel.description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }

    val pendingIntent: PendingIntent = createPendingIntent(context)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
}

fun createPendingIntent(appContext: Context): PendingIntent {
    val intent = Intent(appContext, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    var flags = PendingIntent.FLAG_UPDATE_CURRENT
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        flags = flags or PendingIntent.FLAG_IMMUTABLE
    }

    return PendingIntent.getActivity(
        appContext,
        REQUEST_CODE,
        intent,
        flags
    )
}
