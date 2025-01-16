package com.example.hw1_tomerlevy

import android.app.Application
import com.example.hw1_tomerlevy.managers.SignalManager

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)
    }
}