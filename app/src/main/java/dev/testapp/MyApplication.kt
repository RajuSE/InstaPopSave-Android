package dev.testapp

import android.app.Application
import devx.insta.popsave.PopSaveSetup

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        PopSaveSetup.setup(this)
    }

}