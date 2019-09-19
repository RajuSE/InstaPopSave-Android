package devx.insta.popsave

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat


class PopSave
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RelativeLayout(context, attrs, defStyleAttr) {

    //Views
    var imageView: ImageView? = null
    var popSaveRoot: CoordinatorLayout? = null

    //Configs
    private var src_image: Int = R.color.c2_light2_grey
    private var defaultBackgroundColor: Int = R.color.c2_light2_grey
    private var defaultPopperHeight = ViewGroup.LayoutParams.WRAP_CONTENT
    private var defaultPopperWidth = ViewGroup.LayoutParams.WRAP_CONTENT
    private var defaultPopperRootHeight = 250
    private var defaultPopperRootWidth = ViewGroup.LayoutParams.WRAP_CONTENT

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attributes: AttributeSet?) {
        attributes?.let { _ ->

            inflate(context, R.layout.layout_popsave, this)
            imageView = findViewById(R.id.myImageView)
            popSaveRoot = findViewById(R.id.popSaveRoot)

            val typedArray = context.obtainStyledAttributes(attributes, R.styleable.popsave, 0, 0)
            typedArray.apply {

                defaultBackgroundColor =
                    getResourceId(R.styleable.popsave_background_color, R.color.c2_light2_grey)
                src_image =
                    getResourceId(R.styleable.popsave_src_image, R.color.c2_light2_grey)
            }

            setPopperRootsize(0, 250)
            setPopperSize(140, 140)

            imageView?.setImageDrawable(ContextCompat.getDrawable(context,src_image))

            imageView?.setOnClickListener {
                onViewClickListener?.onViewClicked()

            }
        }
    }


    var onViewClickListener: OnViewClicked? = null


    interface OnViewClicked {
        fun onViewClicked()
    }

    //config methods
    fun setPopperSize(width: Int, height: Int): PopSave {
        if (height > 0)
            defaultPopperHeight = height
        if (width > 0)
            defaultPopperWidth = width
        imageView?.getLayoutParams()?.height = defaultPopperHeight
        imageView?.getLayoutParams()?.width = defaultPopperWidth

        imageView?.requestLayout()

        return this
    }

    fun setPopperRootsize(width: Int, height: Int): PopSave {
        if (height > 0)
            defaultPopperRootHeight = height
        if (width > 0)
            defaultPopperRootWidth = width
        popSaveRoot?.getLayoutParams()?.height = defaultPopperRootHeight
        popSaveRoot?.getLayoutParams()?.width = defaultPopperRootWidth

        popSaveRoot?.requestLayout()

        return this
    }

    fun setPopperImage(drawable: Drawable): PopSave {
        imageView?.setImageDrawable(drawable)
        return this
    }

    fun setPopperImage(resId: Int): PopSave {
        imageView?.setImageDrawable(
            ContextCompat.getDrawable(context, resId)
        )
        return this
    }

    fun popNow() {
        imageView?.let {
            imageView!!.visibility = View.VISIBLE
            popSaveRoot!!.visibility = View.VISIBLE

            PopSaveAnimUtils.pop(imageView, object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    Handler().postDelayed({ slideUpImageAnimation() }, 300)
                }

                override fun onAnimationRepeat(animation: Animation) {
                }
            })
        }
    }

    //animations
    private fun slideUpImageAnimation() {

        val animation = AnimationUtils.loadAnimation(context, R.anim.slide_up)
        animation.interpolator = AccelerateDecelerateInterpolator()
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                imageView?.visibility = View.GONE
                popSaveRoot?.visibility = View.GONE
                onFinish?.onFinish()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        imageView?.startAnimation(animation)
    }


    interface OnFinish {
        fun onFinish()
    }

    var onFinish:OnFinish ?=null

    fun setOnFinishListener(onFinish: OnFinish):PopSave {
        this.onFinish=onFinish
        return this
    }

}