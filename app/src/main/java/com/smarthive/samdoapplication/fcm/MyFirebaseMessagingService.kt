package com.smarthive.samdoapplication.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.smarthive.samdoapplication.Activity.MainActivity
import com.smarthive.samdoapplication.App
import com.smarthive.samdoapplication.R


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FirebaseService"
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "com.smarthive.samdoapplication.fragment"
    private val description = "테스트 노티피케이션"

    // 파이어베이스 서비스의 토큰을 가져온다
    override fun onNewToken(token: String) {
        App.prefs.token = token
        Log.d(TAG, "new Token: $token")
    }

    // 새로운 FCM 메시지가 있을 때 메세지를 받는다
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.from)

        // 앱이 포어그라운드 상태에서 Notificiation을 받는 경우
        if(remoteMessage.notification != null) {
            sendNotification(remoteMessage.notification?.body, remoteMessage.notification?.title)
        }
    }

    // FCM 메시지를 보내는 메시지
    private fun sendNotification(body: String?, title: String?) {
        this.notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("Notification", body)
            putExtra("Notification",title)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor= Color.GREEN
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)

            builder= Notification.Builder(this,channelId)
                .setSmallIcon(R.drawable.warning)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent)
        }else{
            builder = Notification.Builder(this)
                .setSmallIcon(R.drawable.warning)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(0,builder.build())
    }

}