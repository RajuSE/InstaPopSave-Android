package dev.testapp

import android.app.Application
import com.facebook.common.logging.FLog
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.listener.RequestListener
import com.facebook.imagepipeline.listener.RequestLoggingListener

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        var requestListeners =  HashSet<RequestListener>();
        requestListeners.add( RequestLoggingListener());
        var config = ImagePipelineConfig.newBuilder(applicationContext)
            .setRequestListeners(requestListeners)
            .build();

        Fresco.initialize(applicationContext, config)
        FLog.setMinimumLoggingLevel(FLog.VERBOSE);

    }

}