package com.ask.technologies.campaign.fcm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.ask.technologies.campaign.R
import com.ask.technologies.campaign.helpers.DataStoreHelper
import com.ask.technologies.campaign.helpers.FCMHelper
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.picasso.Picasso
import java.util.concurrent.atomic.AtomicInteger

/**
 * A custom Firebase Messaging Service implementation for handling incoming Firebase Cloud Messaging (FCM) messages.
 *
 * This class is responsible for:
 * 1. Processing incoming FCM messages.
 * 2. Generating new FCM tokens when required.
 * 3. Creating and displaying notifications based on the message payload.
 */
class ASKFCMService : FirebaseMessagingService() {

    private val mDataStoreHelper by lazy { DataStoreHelper(this) }

    private val TAG = "ASKFCMService"

    /**
     * Called when a message is received from FCM.
     *
     * @param remoteMessage The [RemoteMessage] containing data or notification payload.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        printAnalytics(mDataStoreHelper.getString(FCMHelper.ON_RECEIVE_EVENT))
        if (remoteMessage.data.isNotEmpty()) {
            processMessage(remoteMessage.data)

        }
    }

    /**
     * Called when a new FCM token is generated.
     *
     * @param token The new FCM registration token.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM Token: $token")
    }

    /**
     * Processes the incoming message data and delegates notification creation.
     *
     * @param data A map containing the message payload data.
     * Expected keys:
     * - `icon`: URL of the notification icon (required).
     * - `title`: Notification title (required).
     * - `short_desc`: Short description for the notification (required).
     * - `feature`: (Optional) URL of a feature image for the notification.
     * - `package`: (Optional) Target app package name.
     */
    private fun processMessage(data: Map<String, String>) {
        val icon = data["appIcon"] ?: return
        val title = data["title"] ?: return
        val shortDesc = data["body"] ?: return
        val image = data["featureGraphic"]
        val packageName = data["packageName"]

        if (!packageName.isNullOrEmpty()) {
            Handler(mainLooper).post {
                sendNotification(
                    icon, title, shortDesc, image, packageName
                )
            }
        }
    }

    /**
     * Creates and displays a notification based on the message payload.
     *
     * @param icon URL for the notification icon.
     * @param title Title of the notification.
     * @param shortDesc Short description for the notification.
     * @param image Optional URL for the feature image.
     * @param storePackage Target app package name.
     */
    private fun sendNotification(
        icon: String,
        title: String,
        shortDesc: String,
        image: String?,
        storePackage: String
    ) {
        val intent = if (!isAppInstalled(storePackage)) {
            printAnalytics(mDataStoreHelper.getString(FCMHelper.ON_PROMOTION_APP_INSTALLED))
            createPlayStoreIntent(storePackage)
        } else {
            printAnalytics(mDataStoreHelper.getString(FCMHelper.ON_PROMOTION_APP_NOT_INSTALLED))
            createOpenAppIntent(storePackage)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )

        val remoteViews = RemoteViews(packageName, R.layout.fcm)
        remoteViews.setTextViewText(R.id.titleTv, title)
        remoteViews.setTextViewText(R.id.bodyTv, shortDesc)

        val channelId = getString(R.string.default_notification_channel_id)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentIntent(pendingIntent)
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(remoteViews)
            .setAutoCancel(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationID = AtomicInteger().incrementAndGet()
        notificationManager.notify(notificationID, notificationBuilder.build())

        loadImagesIntoViews(icon, image, remoteViews, notificationID, notificationBuilder)
    }

    /**
     * Checks if the specified app is installed on the device.
     *
     * @param packageName The package name of the app to check.
     * @return True if the app is installed and enabled, otherwise false.
     */
    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            packageManager.getApplicationInfo(packageName, 0).enabled
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * Creates an intent to open the specified app. If the app is not installed, it falls back to a Play Store intent.
     *
     * @param packageName The package name of the app.
     * @return An intent to open the app or redirect to the Play Store.
     */
    private fun createOpenAppIntent(packageName: String): Intent {
        return packageManager.getLaunchIntentForPackage(packageName)
            ?: createPlayStoreIntent(packageName)
    }

    /**
     * Creates an intent to open the Play Store page of the specified app.
     *
     * @param packageName The package name of the app.
     * @return An intent to open the Play Store.
     */
    private fun createPlayStoreIntent(packageName: String): Intent {
        return try {
            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
        } catch (e: ActivityNotFoundException) {
            Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
        }
    }

    /**
     * Loads images into notification views using Picasso and updates the notification.
     *
     * @param icon URL for the notification icon.
     * @param image Optional URL for the feature image.
     * @param remoteViews The RemoteViews for the notification.
     * @param notificationID The ID of the notification.
     * @param notificationBuilder The notification builder instance.
     */
    private fun loadImagesIntoViews(
        icon: String,
        image: String?,
        remoteViews: RemoteViews,
        notificationID: Int,
        notificationBuilder: NotificationCompat.Builder
    ) {
        try {
            Picasso.get().load(icon).into(remoteViews, R.id.appIcon, notificationID, notificationBuilder.build())
            if (!image.isNullOrEmpty()) {
                remoteViews.setViewVisibility(R.id.featureGraphic, View.VISIBLE)
                Picasso.get().load(image).into(remoteViews, R.id.featureGraphic, notificationID, notificationBuilder.build())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading images into notification views: ${e.message}")
        }
    }

    private fun printAnalytics(message:String?){
        if (!mDataStoreHelper.getBoolean(FCMHelper.IS_ANALYTICS_ENABLED)) return
        val firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        if (message != null) {
            firebaseAnalytics.logEvent(message, bundle)
        }

    }
}