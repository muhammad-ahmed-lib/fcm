package com.ask.technologies.campaign.helpers

object FCMHelper {

    /**
     * Event triggered to check if analytics tracking is enabled for FCM messages.
     *
     * Use Case: Determine whether to log analytics events for received FCM messages.
     * Example Scenario: If enabled, record user engagement, message delivery statistics, or user actions triggered by the message.
     */
    const val IS_ANALYTICS_ENABLED = "IS_ANALYTICS_ENABLED"
    /**
     * Event triggered when a new Firebase Cloud Messaging (FCM) message is received.
     *
     * Use Case: Log or handle a generic FCM message received by the application.
     * Example Scenario: A user receives a notification about an app update or a general announcement.
     */
    const val ON_RECEIVE_EVENT = "ON_RECEIVE_EVENT"

    /**
     * Event triggered when a promotion message is received, and the promoted app is already installed.
     *
     * Use Case: Notify the user about a promotion related to an app they have installed.
     * Example Scenario: A user has installed a partnered app, and you send a notification about a promotion or a new feature for that app.
     */
    const val ON_PROMOTION_APP_INSTALLED = "ON_PROMOTION_APP_INSTALLED"

    /**
     * Event triggered when a promotion message is received, but the promoted app is not installed.
     *
     * Use Case: Encourage the user to install the promoted app via a deep link or store link.
     * Example Scenario: A notification is sent prompting the user to download a partnered app as part of a campaign.
     */
    const val ON_PROMOTION_APP_NOT_INSTALLED = "ON_PROMOTION_APP_NOT_INSTALLED"
}
