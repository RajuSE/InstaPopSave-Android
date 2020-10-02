package devx.insta.popsave

import android.net.Uri
import androidx.core.content.ContextCompat
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder

class ViewExtension {

    fun SimpleDraweeView?.loadImage(imageUrl: String, width: Int = 100, height: Int = 100, tapToRetry: Boolean = false, hasPlaceHolder: Boolean = false, cornerRadious: Int) {
        val imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUrl))
            .setResizeOptions(ResizeOptions(width, height))
            .build()

        val controller = Fresco.newDraweeControllerBuilder()
            .setImageRequest(imageRequest)
            .setOldController(this@loadImage?.controller)
            .setTapToRetryEnabled(tapToRetry)
            .build()

        if (cornerRadious > 0) {
            val roundParam = RoundingParams.fromCornersRadii(FloatArray(cornerRadious))
            this@loadImage?.hierarchy?.roundingParams = roundParam
        }

        if (hasPlaceHolder)
            this@loadImage?.hierarchy?.setPlaceholderImage(ContextCompat.getColor(context, R.color.c2_light2_grey))
        this@loadImage?.controller = controller
    }
}