package com.ask.technologies.fcm

import android.os.Bundle
import android.os.StrictMode
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ask.technologies.campaign.builder.ASKFCM
import com.ask.technologies.campaign.helpers.FCMHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle=Bundle()
            .apply {
              putString(FCMHelper.ON_RECEIVE_EVENT,"promotion_Notification_Received")
              putString(FCMHelper.ON_PROMOTION_APP_INSTALLED,"promotion_App_Installed")
              putString(FCMHelper.ON_PROMOTION_APP_NOT_INSTALLED,"promotion_App_Not_Installed")
            }

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork() // Detect network access on the main thread
                .penaltyLog() // Log violations
                .build()
        )
        val askFcm=ASKFCM.Builder()
            .setContext(this)
            .setTopic("com.ask.technologies.fcm")
            .enableAnalytics(true)
            .analyticsBundle(bundle)
            .build()
        askFcm.startService().setupFCMService()
    }
}