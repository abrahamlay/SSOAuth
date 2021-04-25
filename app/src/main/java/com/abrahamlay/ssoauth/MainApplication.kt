package com.abrahamlay.ssoauth

import android.app.Application
import android.util.Log
import com.facebook.FacebookSdk

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        FacebookSdk.sdkInitialize(this)
        Log.d(TAG, "Facebook hash key: ${FacebookSdk.getApplicationSignature(this)}")
    }

    companion object {
        const val TAG = "MainApplication"
    }
}