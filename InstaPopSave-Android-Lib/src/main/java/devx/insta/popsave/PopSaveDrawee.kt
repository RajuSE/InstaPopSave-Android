package devx.insta.popsave

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.widget.RelativeLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder

class PopSaveDrawee @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    //Views
    var simpleDraweeView: SimpleDraweeView? = null
    var popSaveRoot: CoordinatorLayout? = null
    var onFinish: OnFinish? = null

    //Configs
    private var src_image: Int = R.color.c2_light2_grey
    private var roundedCornerRadius: Int = 10
    private var defaultBackgroundColor: Int = R.color.c2_light2_grey
    private var defaultPopperHeight = ViewGroup.LayoutParams.WRAP_CONTENT
    private var defaultPopperWidth = ViewGroup.LayoutParams.WRAP_CONTENT
    private var defaultPopperRootHeight = 250
    private var defaultPopperRootWidth = ViewGroup.LayoutParams.WRAP_CONTENT
    private var shouldMoveDown = false
    private var imageUrl = "NA"

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attributes: AttributeSet?) {
        attributes?.let { _ ->
            Fresco.initialize(context)
            inflate(context, R.layout.layout_popsave_drawee, this)
            simpleDraweeView = findViewById(R.id.frescoImageView)
            popSaveRoot = findViewById(R.id.popSaveRoot)

            val typedArray = context.obtainStyledAttributes(attributes, R.styleable.popsave, 0, 0)
            typedArray.apply {
                defaultBackgroundColor =
                    getResourceId(R.styleable.popsave_background_color, R.color.c2_light2_grey)
                src_image =
                    getResourceId(R.styleable.popsave_src_image, R.color.c2_light2_grey)
            }

            setPopperAreaSize(0, 250)
            setPopperSize(140, 140)

            simpleDraweeView?.setImageDrawable(ContextCompat.getDrawable(context, src_image))


            simpleDraweeView?.setOnClickListener {
                onViewClickListener?.onViewClicked()
            }
        }
    }

    var onViewClickListener: OnViewClicked? = null

    interface OnViewClicked {
        fun onViewClicked()
    }

    //config methods
    fun setPopperSize(width: Int, height: Int): PopSaveDrawee {
        if (height > 0) defaultPopperHeight = height
        if (width > 0) defaultPopperWidth = width
        simpleDraweeView?.layoutParams?.height = defaultPopperHeight
        simpleDraweeView?.layoutParams?.width = defaultPopperWidth
        simpleDraweeView?.requestLayout()
        return this
    }

    fun setPopperAreaSize(width: Int, height: Int): PopSaveDrawee {
        if (height > 0) defaultPopperRootHeight = height
        if (width > 0) defaultPopperRootWidth = width

        popSaveRoot?.layoutParams?.height = defaultPopperRootHeight
        popSaveRoot?.layoutParams?.width = defaultPopperRootWidth
        popSaveRoot?.requestLayout()
        return this
    }

    fun setPopperImage(resource: Any): PopSaveDrawee {
        return when (resource) {
            is String -> {
                imageUrl = resource
                imageDrawable = null
                imageResource = null
                this
            }
            is Int -> {
                imageResource = resource
                imageDrawable = null
                imageUrl = "NA"
                this
            }
            is Drawable -> {
                imageDrawable = resource
                imageResource = null
                imageUrl = "NA"
                this
            }
            else -> throw Throwable("Unsupported image resource found")
        }
    }

    private var imageDrawable: Drawable? = null
    private var imageResource: Int? = null

    private fun getImageResource(): Any? {
        return when {
            imageResource != null -> imageResource
            imageDrawable != null -> imageDrawable
            else -> imageUrl
        }
    }

    fun highlightAreaToDebug(isDebug: Boolean = false) {
        if (isDebug) {
            popSaveRoot?.setBackgroundColor(ContextCompat.getColor(context, R.color.green_debug))
        } else {
            popSaveRoot?.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
        }
    }

    fun popNow() {
        simpleDraweeView.loadImage(imageUrl, 100, 100, true, 20)

//        Glide.with(this)
//            .load(getImageResource())
//            .into(simpleDraweeView!!)

        simpleDraweeView?.let {
            simpleDraweeView!!.visibility = View.VISIBLE
            popSaveRoot!!.visibility = View.VISIBLE
            PopSaveAnimUtils.pop(simpleDraweeView, object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    Handler().postDelayed({ slideUpImageAnimation() }, 300)
                }
            })
        }
    }

    //animations
    private fun slideUpImageAnimation() {
        if (true) {
            simpleDraweeView?.let {
                simpleDraweeView!!.animate()
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .translationY(if (shouldMoveDown) defaultPopperRootHeight.toFloat() else -defaultPopperRootHeight.toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            simpleDraweeView?.visibility = View.GONE
                            popSaveRoot?.visibility = View.GONE
                            onFinish?.onFinish()
                            simpleDraweeView?.animate()?.translationY(0.toFloat())
                                ?.setListener(null)
                        }
                    })
            }
            return
        }
    }

    interface OnFinish {
        fun onFinish()
    }

    fun setOnFinishListener(onFinish: OnFinish): PopSaveDrawee {
        this.onFinish = onFinish
        return this
    }

}

fun SimpleDraweeView?.loadImage(
    imageUrl: String,
    width: Int = 100,
    height: Int = 100,
    hasPlaceHolder: Boolean = false,
    cornerRadious: Int
) {
    val imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUrl))
        .setResizeOptions(ResizeOptions(width, height))
        .build()

    val controller = Fresco.newDraweeControllerBuilder()
        .setImageRequest(imageRequest)
        .setOldController(this@loadImage?.controller)
        .build()

    if (cornerRadious > 0) {
        val floatArray = floatArrayOf(cornerRadious.toFloat(), cornerRadious.toFloat(), cornerRadious.toFloat(), cornerRadious.toFloat(),
            cornerRadious.toFloat(), cornerRadious.toFloat(), cornerRadious.toFloat(), cornerRadious.toFloat())

        val roundParam = RoundingParams.fromCornersRadii(floatArray)
        this@loadImage?.hierarchy?.roundingParams = roundParam
    }

//    if (hasPlaceHolder)
//        this@loadImage?.hierarchy?.setPlaceholderImage(
//            ContextCompat.getColor(
//                context,
//                R.color.c2_light2_grey
//            )
//        )
    this@loadImage?.controller = controller
}
