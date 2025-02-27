
package fr.isen.laval.isensmartcompanion.notif

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import android.util.Log
import kotlinx.coroutines.launch
import fr.isen.laval.isensmartcompanion.MainActivity
import fr.isen.laval.isensmartcompanion.R


class NotificationViewModel : ViewModel() {

    fun scheduleNotification(context: Context, eventTitle: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "event_reminders"

        // Créer un canal de notification pour Android 8.0 (API 26) et supérieur
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Event Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )


        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Rappel d'événement")
            .setContentText("L'événement \"$eventTitle\" arrive bientôt!")
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        GlobalScope.launch(Dispatchers.Main) {
            delay(10_000) // 🔄 Notification après 10 secondes
            if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                NotificationManagerCompat.from(context).notify(eventTitle.hashCode(), notification)
            } else {
                // Permission non accordée, tu peux afficher un message ou demander l'autorisation
                Log.e("Notification", "Permission de notification refusée")
            }

        }
    }
}