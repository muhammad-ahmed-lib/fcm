package com.ask.technologies.campaign.abstraction

import android.content.Context
import android.os.Bundle

/**
 * Abstract class for building Firebase Cloud Messaging (FCM) functionality.
 * Provides methods for setting up FCM service, adding analytics, and handling topic subscriptions.
 */
abstract class ASKFCMBuilder {

    // The application context, used to initialize FCM and other services
    abstract val context: Context

    // The topic to subscribe to/unsubscribe from for push notifications
    abstract var mTopic: String

    // Flag indicating if analytics tracking is enabled
    abstract var mEnableAnalytics: Boolean

    // Optional bundle containing analytics parameters for event tracking
    abstract var mAnalyticsBundle: Bundle?

    /**
     * Adds analytics-related data to the DataStore for FCM events tracking.
     * This is invoked if analytics is enabled.
     */
    abstract fun addAnalytics()

    /**
     * Sets up the Firebase Cloud Messaging (FCM) service.
     * - Initializes Firebase and subscribes to the specified topic.
     * - Creates a notification channel for Android Oreo (API level 26) and above.
     */
    abstract fun setupFCMService()

    /**
     * Removes a topic subscription.
     *
     * @param mTopic The topic to unsubscribe from.
     */
    abstract fun removeTopic(mTopic: String)
}
