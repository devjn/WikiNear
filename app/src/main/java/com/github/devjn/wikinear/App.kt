package com.github.devjn.wikinear

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate


/**
 * Created by @author Jahongir on 26-Jan-19
 * devjn@jn-arts.com
 * App
 */

class App : Application() {

    companion object {
        const val TAG = "WikiNear"

        lateinit var appContext: Context
//            private set

        fun isNetworkAvailable(): Boolean {
            try {
                val e = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork = e.activeNetworkInfo
                return activeNetwork != null && activeNetwork.isConnectedOrConnecting
            } catch (e: Exception) {
                Log.w(TAG, e.toString())
            }

            return false
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

}
