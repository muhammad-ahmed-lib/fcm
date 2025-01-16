package com.ask.technologies.campaign.builder

import android.content.Context
import android.os.Bundle
import com.ask.technologies.campaign.abstraction.ASKFCMBuilder
import com.ask.technologies.campaign.abstraction.ASKFCMBuilderImp

/**
 * Service class to handle Firebase Cloud Messaging (FCM) campaigns.
 * This class provides a structured way to manage FCM topics and enable analytics for campaigns.
 * Instances of this class should be constructed using the [Builder] class.
 *
 * @param context The application or activity context required for FCM operations.
 * @param mTopic The topic to subscribe to for receiving notifications.
 * @param mEnableAnalytics Flag to enable or disable analytics for the campaign.
 * @param mAnalyticsBundle The analytics bundle containing additional data for analytics (if enabled).
 *
 * @author Muhammad Ahmed
 * @since 01/01/2025
 */
class ASKFCM private constructor(
    private val context: Context,
    private var mTopic: String = "",
    private var mEnableAnalytics: Boolean = false,
    private var mAnalyticsBundle: Bundle? = null,
) {
    /**
     * Builder class for constructing instances of [ASKFCM].
     * Use this class to configure and create an ASKFCM object.
     */
    class Builder {
        private lateinit var mContext: Context
        private var mTopic = ""
        private var mEnableAnalytics = false
        private var mAnalyticsBundle: Bundle? = null

        /**
         * Sets the application or activity context.
         * This is a required parameter and must be called before building the instance.
         *
         * @param context The application or activity context.
         * @return The current instance of [Builder].
         */
        fun setContext(context: Context) = apply {
            this.mContext = context
        }

        /**
         * Sets the topic for Firebase Cloud Messaging subscriptions.
         *
         * @param topic The topic name to subscribe to.
         * @return The current instance of [Builder].
         */
        fun setTopic(topic: String) = apply {
            this.mTopic = topic
        }

        /**
         * Enables or disables analytics for the FCM campaign.
         *
         * @param isEnable Flag indicating whether to enable analytics.
         * @return The current instance of [Builder].
         */
        fun enableAnalytics(isEnable: Boolean) = apply {
            this.mEnableAnalytics = isEnable
        }

        /**
         * Sets the analytics bundle for the campaign.
         * This must be provided if analytics is enabled.
         *
         * @param analyticsBundle A [Bundle] containing analytics data.
         * @return The current instance of [Builder].
         */
        fun analyticsBundle(analyticsBundle: Bundle) = apply {
            this.mAnalyticsBundle = analyticsBundle
        }

        /**
         * Builds and returns an instance of [ASKFCM].
         * This method validates the input parameters and ensures all required fields are set.
         *
         * @throws IllegalStateException if the context is not set or analytics is enabled without a valid analytics bundle.
         * @return A configured instance of [ASKFCM].
         */
        fun build(): ASKFCM {
            if (!::mContext.isInitialized) {
                throw IllegalStateException("Context must be set before building")
            }
            if (mEnableAnalytics && mAnalyticsBundle == null) {
                throw IllegalStateException("Analytics Bundle must not be null")
            }
            return ASKFCM(
                context = mContext,
                mTopic = mTopic,
                mEnableAnalytics = mEnableAnalytics,
                mAnalyticsBundle = mAnalyticsBundle
            )
        }
    }
    fun startService():ASKFCMBuilder=ASKFCMBuilderImp(
        context = context,
        mTopic = mTopic,
        mEnableAnalytics = mEnableAnalytics,
        mAnalyticsBundle = mAnalyticsBundle
    )
}