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
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.facebook.drawee.view.SimpleDraweeView


class PopSaveDrawee
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RelativeLayout(context, attrs, defStyleAttr) {

    //Views
    var simpleDraweeView: SimpleDraweeView? = null
    var popSaveRoot: CoordinatorLayout? = null

    //Configs
    private var srcImage: Int = R.color.c2_light_red
    private var defaultBackgroundColor: Int = R.color.c2_light2_grey
    private var defaultPopperHeight = ViewGroup.LayoutParams.WRAP_CONTENT
    private var defaultPopperWidth = ViewGroup.LayoutParams.WRAP_CONTENT
    private var defaultPopperRootHeight = 250
    private var defaultPopperRootWidth = ViewGroup.LayoutParams.WRAP_CONTENT
    private var shouldMoveDown = false
    private var isFrescoImage = false

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attributes: AttributeSet?) {
        attributes?.let { _ ->

            inflate(context, R.layout.layout_popsave_drawee, this)
            simpleDraweeView = findViewById(R.id.frescoImageView)
            simpleDraweeView?.visibility = View.GONE
            popSaveRoot = findViewById(R.id.popSaveRoot)

            val typedArray = context.obtainStyledAttributes(attributes, R.styleable.popsave, 0, 0)
            typedArray.apply {

                defaultBackgroundColor =
                    getResourceId(R.styleable.popsave_background_color, R.color.c2_light2_grey)
                srcImage = getResourceId(R.styleable.popsave_src_image, R.color.c2_light2_grey)
            }

            setPopperAreaSize(0, 250)
            setPopperSize(140, 140)

            simpleDraweeView?.setOnClickListener {
                onViewClickListener?.onViewClicked()
            }
        }
    }

    fun setPopperCornerRadius(radius: Float): PopSaveDrawee {
        simpleDraweeView?.setImageDrawable(ContextCompat.getDrawable(context, srcImage))
        return this
    }


    var onViewClickListener: OnViewClicked? = null


    interface OnViewClicked {
        fun onViewClicked()
    }

    //config methods
    fun setPopperSize(width: Int, height: Int): PopSaveDrawee {
        if (height > 0)
            defaultPopperHeight = height
        if (width > 0)
            defaultPopperWidth = width
        simpleDraweeView?.layoutParams?.height = defaultPopperHeight
        simpleDraweeView?.layoutParams?.width = defaultPopperWidth

        simpleDraweeView?.requestLayout()

        return this
    }

    fun setPopperAreaSize(width: Int, height: Int): PopSaveDrawee {
        if (height > 0)
            defaultPopperRootHeight = height
        if (width > 0)
            defaultPopperRootWidth = width
        popSaveRoot?.layoutParams?.height = defaultPopperRootHeight
        popSaveRoot?.layoutParams?.width = defaultPopperRootWidth

        popSaveRoot?.requestLayout()

        return this
    }

    fun setPopperImage(drawable: Drawable): PopSaveDrawee {
        simpleDraweeView?.setImageDrawable(drawable)
        return this
    }

    fun setPopperImage(resId: Int): PopSaveDrawee {
        simpleDraweeView?.setImageDrawable(
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
        simpleDraweeView!!.visibility = View.VISIBLE
        popSaveRoot!!.visibility = View.VISIBLE

        PopSaveAnimUtils.pop(simpleDraweeView, object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                Handler().postDelayed({ slideUpImageAnimation() }, 300)
            }

            override fun onAnimationRepeat(animation: Animation) {
            }
        })
    }

    private fun shouldMoveDown(): PopSaveDrawee {
        shouldMoveDown = true
        return this
    }

    private fun setupPopperGravity() {
        val layoutParams = LinearLayout.LayoutParams(defaultPopperWidth, defaultPopperHeight)
        layoutParams.gravity = if (shouldMoveDown) Gravity.TOP else Gravity.BOTTOM
        simpleDraweeView?.layoutParams
        simpleDraweeView?.requestLayout()
        requestLayout()
    }

    fun loadImage(
        imageUrl: String,
        resizeWidth: Int = 100,
        resizeHeight: Int = 100
    ): PopSaveDrawee {
        simpleDraweeView?.loadImage(imageUrl, resizeWidth, resizeHeight)
        simpleDraweeView?.requestLayout()
        return this
    }

    private fun shouldMoveUp(): PopSaveDrawee {
        shouldMoveDown = false
        return this
    }

    //animations
    private fun slideUpImageAnimation() {
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
                        simpleDraweeView?.animate()?.translationY(0.toFloat())?.setListener(null)
                    }
                })
        }
        return
    }


    interface OnFinish {
        fun onFinish()
    }

    var onFinish: OnFinish? = null

    fun setOnFinishListener(onFinish: OnFinish): PopSaveDrawee {
        this.onFinish = onFinish
        return this
    }

}