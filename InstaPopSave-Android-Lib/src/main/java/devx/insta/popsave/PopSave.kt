package devx.insta.popsave

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.LinearLayout
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
    private var srcImage: Int = R.color.c2_light2_grey
    private var defaultBackgroundColor: Int = R.color.c2_light2_grey
    private var defaultPopperHeight = ViewGroup.LayoutParams.WRAP_CONTENT
    private var defaultPopperWidth = ViewGroup.LayoutParams.WRAP_CONTENT
    private var defaultPopperRootHeight = 250
    private var defaultPopperRootWidth = ViewGroup.LayoutParams.WRAP_CONTENT
    private var shouldMoveDown = false

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attributes: AttributeSet?) {
        attributes?.let { _ ->

            inflate(context, R.layout.layout_popsave, this)
            imageView = findViewById(R.id.myImageView)
            imageView?.visibility = View.GONE

            popSaveRoot = findViewById(R.id.popSaveRoot)

            val typedArray = context.obtainStyledAttributes(attributes, R.styleable.popsave, 0, 0)
            typedArray.apply {

                defaultBackgroundColor =
                    getResourceId(R.styleable.popsave_background_color, R.color.c2_light2_grey)
                srcImage = getResourceId(R.styleable.popsave_src_image, R.color.c2_light2_grey)
            }

            setPopperAreaSize(0, 250)
            setPopperSize(140, 140)

            imageView?.setImageDrawable(ContextCompat.getDrawable(context, srcImage))

            imageView?.setOnClickListener {
                onViewClickListener?.onViewClicked()
            }
        }
    }

    fun setPopperCornerRadius(radius: Float): PopSave {
        imageView?.setImageDrawable(ContextCompat.getDrawable(context, srcImage))
        return this
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
        imageView?.layoutParams?.height = defaultPopperHeight
        imageView?.layoutParams?.width = defaultPopperWidth

        imageView?.requestLayout()

        return this
    }

    private fun setPopperAreaSize(width: Int, height: Int): PopSave {
        if (height > 0)
            defaultPopperRootHeight = height
        if (width > 0)
            defaultPopperRootWidth = width

        popSaveRoot?.layoutParams?.height = defaultPopperRootHeight
        popSaveRoot?.layoutParams?.width = defaultPopperRootWidth
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

    fun highlightAreaToDebug(isDebug: Boolean = false) {
        if (isDebug) {
            popSaveRoot?.setBackgroundColor(ContextCompat.getColor(context, R.color.green_debug))
        } else {
            popSaveRoot?.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
        }
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


    fun shouldMoveDown(): PopSave {
        shouldMoveDown = true
        return this
    }

    private fun setupPopperGravity() {
        val layoutParams = LinearLayout.LayoutParams(defaultPopperWidth, defaultPopperHeight)
        layoutParams.gravity = if (shouldMoveDown) Gravity.TOP else Gravity.BOTTOM
        imageView?.layoutParams
        imageView?.requestLayout()
        requestLayout()
    }

    fun shouldMoveUp(): PopSave {
        shouldMoveDown = false
        return this
    }

    //animations
    private fun slideUpImageAnimation() {
        if (true) {
            imageView?.let {
                imageView!!.animate()
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .translationY(if (shouldMoveDown) defaultPopperRootHeight.toFloat() else -defaultPopperRootHeight.toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            imageView?.visibility = View.GONE
                            popSaveRoot?.visibility = View.GONE
                            onFinish?.onFinish()
                            imageView?.animate()?.translationY(0.toFloat())?.setListener(null)
                        }
                    })
            }
            return
        }
    }

    interface OnFinish {
        fun onFinish()
    }

    var onFinish: OnFinish? = null

    fun setOnFinishListener(onFinish: OnFinish): PopSave {
        this.onFinish = onFinish
        return this
    }
}