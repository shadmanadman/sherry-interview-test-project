package com.adman.shadman.sherryinterviewtestproject

import android.app.Application
import com.adman.shadman.sherryinterviewtestproject.di.AppContainer

class InterviewTripApp: Application() {
    override fun onCreate() {
        super.onCreate()
        AppContainer.initialize(this)
    }
}