package fr.isen.laval.isensmartcompanion.notif

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import fr.isen.laval.isensmartcompanion.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val channelId = "event_notifications"
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Si Android 8 ou plus, créer un channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Event Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Rappel d'événement")
            .setContentText("Vous avez un événement à ne pas manquer !")
            .setSmallIcon(android.R.drawable.ic_dialog_info)  // Assure-toi que cette icône est valide
            .setAutoCancel(true)  // Ferme la notification après l'interaction
            .setPriority(NotificationCompat.PRIORITY_HIGH)  // Priorité haute pour s'assurer qu'elle s'affiche bien
            .build()

        notificationManager.notify(1, notification)
    }
}

