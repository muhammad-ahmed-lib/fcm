ASKFCM - Firebase Cloud Messaging (FCM) Campaign Tool

ASKFCM is a powerful and easy-to-use tool that simplifies the management of Firebase Cloud Messaging (FCM) campaigns. Whether you're promoting apps or sending targeted notifications, ASKFCM makes the process seamless and efficient with built-in analytics and tracking features.

Features

Send Targeted Notifications: Easily send FCM notifications to specific users or devices based on topics.

Track App Installation Status: Receive notifications when the promotion app is installed or not installed.

Built-in Analytics: Track the performance of your notifications with Firebase Analytics.

Real-time Event Tracking: Keep track of events such as app installation and user actions.

Seamless Integration: Integrates directly with Firebase services for effortless setup.

Requirements

Android 5.0 (Lollipop) and above.

Firebase SDK (Firebase Cloud Messaging).

ASKFCM is compatible with Jetpack Compose and XML-based layouts.

Installation

To integrate ASKFCM into your project, follow these steps:

Step 1: Add the Dependency

In your build.gradle file, add the following dependency:

Copy

dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}

dependencies {
 
    implementation 'com.github.muhammad-ahmed-lib:fcm:1.0.0'

}

Step 2: Set Up Firebase

Ensure Firebase is set up in your project. You can follow the official Firebase setup guide here.

Step 3: Initialize ASKFCM

In your activity or application, initialize ASKFCM with the required parameters:

Copy

val bundle = Bundle().apply {
 
    putString(FCMHelper.ON_RECEIVE_EVENT, "promotion_Notification_Received")
   
    putString(FCMHelper.ON_PROMOTION_APP_INSTALLED, "promotion_App_Installed")
    
    putString(FCMHelper.ON_PROMOTION_APP_NOT_INSTALLED, "promotion_App_Not_Installed")

}

val askFcm = ASKFCM.Builder()
 
    .setContext(this)
   
    .setTopic("com.ask.technologies.fcm") // Set the topic for the notification
   
    .enableAnalytics(true) // Enable analytics
    
    .analyticsBundle(bundle) // Pass custom event data for analytics
    
    .build()

askFcm.startService().setupFCMService()


Contributing

We welcome contributions to ASKFCM! If you'd like to report an issue, suggest a feature, or contribute to the code, please follow these steps:

Fork the repository.

Create a new branch (git checkout -b feature/feature-name).

Make your changes.

Commit your changes (git commit -am 'Add new feature').

Push to the branch (git push origin feature/feature-name).

Create a new pull request.

Feedback 

If you have any feedback, suggestions, or encounter issues, feel free to reach out to us at: feedback@asktechnologies.net.

License

Distributed under the MIT License. See LICENSE for more information.

This template covers the essential setup, usage, and contribution guidelines. You can adjust the details according to your project structure or any additional features in your library.
