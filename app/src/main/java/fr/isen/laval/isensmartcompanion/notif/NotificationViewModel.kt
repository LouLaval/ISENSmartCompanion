
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
import kotlinx.coroutines.delay
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import fr.isen.laval.isensmartcompanion.MainActivity


class NotificationViewModel : ViewModel() {

    private val channelId = "event_reminders"

    // Fonction pour configurer le canal de notification
    private fun createNotificationChannel(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Event Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications de rappels d'événements"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Fonction pour envoyer une notification
    fun sendNotification(context: Context, eventTitle: String, eventDescription: String) {
        createNotificationChannel(context)

        viewModelScope.launch(Dispatchers.Main) {
            delay(10000); // Délai de 10 secondes

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Rappel : $eventTitle")
            .setContentText(eventDescription)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            NotificationManagerCompat.from(context).notify(eventTitle.hashCode(), notification)
        } else {
            // Gérer le cas où les notifications sont désactivées
            Log.e("Notification", "Les notifications sont désactivées pour l'application.")
        }
    }


    fun scheduleNotification(context: Context, eventTitle: String, eventDescription: String, delayMillis: Long) {
        createNotificationChannel(context)

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

        viewModelScope.launch(Dispatchers.Main) {
            delay(delayMillis)
            if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                NotificationManagerCompat.from(context).notify(eventTitle.hashCode(), notification)
            } else {
                Log.e("Notification", "Permission de notification refusée")
            }
        }
    }
}
}