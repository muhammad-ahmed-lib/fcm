package com.ask.technologies.campaign.abstraction

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.ask.technologies.campaign.R
import com.ask.technologies.campaign.helpers.DataStoreHelper
import com.ask.technologies.campaign.helpers.FCMHelper
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.runBlocking

/**
 * Implementation of the ASKFCMBuilder for managing Firebase Cloud Messaging (FCM) services.
 * This class handles FCM topic subscription, analytics setup, and notification channel creation.
 *
 * @param context The application context.
 * @param mTopic The FCM topic to which the app subscribes.
 * @param mEnableAnalytics Flag to determine if analytics tracking is enabled.
 * @param mAnalyticsBundle Additional analytics parameters.
 */
class ASKFCMBuilderImp(
    override val context: Context,
    override var mTopic: String,
    override var mEnableAnalytics: Boolean,
    override var mAnalyticsBundle: Bundle?
) : ASKFCMBuilder() {

    private val TAG: String = "ASKFCMBuilderImpInfo"
    private val mDataStoreHelper by lazy { DataStoreHelper(context) }

    /**
     * Adds analytics-related data to the DataStore for tracking FCM-related events.
     *
     * Stores flags and events for analytics purposes if analytics is enabled.
     *
     * - **FCMHelper.ON_RECEIVE_EVENT**: Tracks when an FCM message is received.
     * - **FCMHelper.ON_PROMOTION_APP_INSTALLED**: Tracks events for promotions when the app is installed.
     * - **FCMHelper.ON_PROMOTION_APP_NOT_INSTALLED**: Tracks events for promotions when the app is not installed.
     */
    override fun addAnalytics() {
        val mReceiveEvent = mAnalyticsBundle?.getString(FCMHelper.ON_RECEIVE_EVENT)
        val mAppInstalledEvent = mAnalyticsBundle?.getString(FCMHelper.ON_PROMOTION_APP_INSTALLED)
        val mAppNotInstalledEvent = mAnalyticsBundle?.getString(FCMHelper.ON_PROMOTION_APP_NOT_INSTALLED)

        // Store the analytics enable flag.
        mDataStoreHelper.saveBoolean(FCMHelper.IS_ANALYTICS_ENABLED, mEnableAnalytics)

        if (mEnableAnalytics && mAnalyticsBundle != null) {
            // Save individual analytics events if available.
            if (mReceiveEvent != null) {
                mDataStoreHelper.saveString(FCMHelper.ON_RECEIVE_EVENT, mReceiveEvent)
            }
            if (mAppInstalledEvent != null) {
                mDataStoreHelper.saveString(FCMHelper.ON_PROMOTION_APP_INSTALLED, mAppInstalledEvent)
            }
            if (mAppNotInstalledEvent != null) {
                mDataStoreHelper.saveString(FCMHelper.ON_PROMOTION_APP_NOT_INSTALLED, mAppNotInstalledEvent)
            }
        }
    }

    /**
     * Sets up the Firebase Cloud Messaging (FCM) service.
     *
     * - Initializes Firebase.
     * - Creates a notification channel (for Android Oreo and above).
     * - Subscribes the app to the specified FCM topic.
     */
    override fun setupFCMService() {
        runBlocking {
            initializeFirebase(context)
            createNotificationChannel(context)
            FirebaseMessaging.getInstance().subscribeToTopic(mTopic)
            Log.d(TAG, "setupFCMService: $mTopic")
        }
    }

    /**
     * Removes the app's subscription to a specified FCM topic.
     *
     * @param mTopic The topic from which the app will unsubscribe.
     */
    override fun removeTopic(mTopic: String) {
      //  FirebaseMessaging.getInstance().unsubscribeFromTopic(mTopic)
    }

    /**
     * Initializes Firebase for the app.
     *
     * Ensures that Firebase services are properly set up. Logs an error if initialization fails.
     *
     * @param context The [Context] required for Firebase initialization.
     */
    private fun initializeFirebase(context: Context) {
        try {
            FirebaseApp.initializeApp(context)
        } catch (e: Exception) {
            Log.d(TAG, "initializeFirebase: Initialization failed", e)
        }
    }

    /**
     * Creates a default notification channel for Firebase Cloud Messaging notifications.
     *
     * This is required for Android Oreo (API level 26) and above. If the notification channel
     * already exists, this method does nothing.
     *
     * @param context The [Context] required to access the Notification Manager.
     */
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = context.getString(R.string.default_notification_channel_id)
            val channelName = context.getString(R.string.default_notification_channel_name)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Check if the channel already exists.
            if (notificationManager.getNotificationChannel(channelId) == null) {
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}
