package devx.insta.popsave

import android.content.Context
import com.facebook.drawee.backends.pipeline.Fresco

object PopSaveSetup {

    init {
        println("Singleton class PopSaveSetup init.")
    }

    var isSetupDone = false

    fun setup(context: Context) {
        isSetupDone = true
        Fresco.initialize(context)
    }

}