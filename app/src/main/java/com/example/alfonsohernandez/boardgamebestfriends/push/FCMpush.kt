package com.example.alfonsohernandez.boardgamebestfriends.push

import com.google.firebase.messaging.RemoteMessage.Notification
//import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.chat.ChatActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.splash.SplashActivity

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber
import javax.inject.Inject

class FCMpush: FirebaseMessagingService() {

    @Inject
    lateinit var fcmHandler: FCMHandler

    init {
        App.instance.component.inject(this)
    }

    private val TAG = "FCMpush"

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Timber.d(TAG + " From: " + remoteMessage!!.getFrom())

        remoteMessage?.let {
            fcmHandler.handlePush(it)
        }

    }

//    private fun sendNotification(messageBody: String) {
//        val intent = Intent(this, SplashActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT)
//
//        val channelId = getString(R.string.default_notification_channel_id)
//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.drawable.bgbf_icon_small_letters)
//                .setContentTitle("FCM Message")
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent)
//
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT)
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
//    }


//    internal fun buildNotification(context: Context, title: String?, txt: String?, makepending: Boolean): Notification {
//        // build intent to launch landing activity with
//        val intent = Intent(context, SplashActivity::class.java)
//
//        // build fake backstack
//        val stackBuilder = TaskStackBuilder.create(context)
//        stackBuilder.addNextIntentWithParentStack(intent)
//        val contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val b = NotificationCompat.Builder(context)
//        b.setAutoCancel(true)
//                .setSmallIcon(R.drawable.bgbf_icon_small_letters)
//                .setStyle(NotificationCompat.BigTextStyle().bigText(txt))
//                .setColor(resources.getColor(R.color.colorAccent))
//                .setContentText(txt)
//
//        if (title != null)
//            b.setContentTitle(title)
//        else
//            b.setContentTitle(context.resources.getString(R.string.app_name))
//
//        if (makepending)
//            b.setContentIntent(contentIntent)
//        else
//            b.setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT))
//
//        b.priority = NotificationCompat.PRIORITY_MAX
//
//        return b.build()
//    }

}