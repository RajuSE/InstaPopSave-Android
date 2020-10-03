package devx.insta.popsave

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
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
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.drawee.view.DraweeHolder




class PopSaveDrawee
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RelativeLayout(context, attrs, defStyleAttr) {

    //Views
    var frescoImageView: SimpleDraweeView? = null
    var popSaveRoot: CoordinatorLayout? = null

    //Configs
    private var srcImage: Int = R.color.c2_light_red
    private var defaultBackgroundColor: Int = R.color.c2_light2_grey
    private var defaultPopperHeight = 100
    private var defaultPopperWidth = 100
    private var defaultPopperRootHeight = 250
    private var defaultPopperRootWidth = ViewGroup.LayoutParams.WRAP_CONTENT
    private var shouldMoveDown = false
    private var isFrescoImage = false

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attributes: AttributeSet?) {
        attributes?.let { _ ->

//            Fresco.initialize(context)
            inflate(context, R.layout.layout_popsave_drawee, this)
            frescoImageView = findViewById(R.id.frescoImageView)
            popSaveRoot = findViewById(R.id.popSaveRoot)

            val typedArray = context.obtainStyledAttributes(attributes, R.styleable.popsave, 0, 0)
            typedArray.apply {

                defaultBackgroundColor =
                    getResourceId(R.styleable.popsave_background_color, R.color.c2_light2_grey)
                srcImage = getResourceId(R.styleable.popsave_src_image, R.color.c2_light2_grey)
            }

            setPopperAreaSize(0, 250)
//            setPopperSize(140, 140)

//            frescoImageView?.setImageDrawable(ContextCompat.getDrawable(context, srcImage))

//            frescoImageView?.setOnClickListener {
//                onViewClickListener?.onViewClicked()
//            }
        }
    }

    fun setPopperCornerRadius(radius: Float): PopSaveDrawee {
        frescoImageView?.setImageDrawable(ContextCompat.getDrawable(context, srcImage))
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
        frescoImageView?.layoutParams?.height = defaultPopperHeight
        frescoImageView?.layoutParams?.width = defaultPopperWidth

        frescoImageView?.requestLayout()

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
        frescoImageView?.setImageDrawable(drawable)
        return this
    }

    fun setPopperImage(resId: Int): PopSaveDrawee {
        frescoImageView?.setImageDrawable(
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
        frescoImageView?.let {
            frescoImageView!!.visibility = View.VISIBLE
            popSaveRoot!!.visibility = View.VISIBLE
            loadImage("http://www.winterquilts.com/wp-content/uploads/2016/11/packing.png")

            if(true) return
            PopSaveAnimUtils.pop(frescoImageView, object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    Handler().postDelayed({ slideUpImageAnimation() }, 300)
                }

                override fun onAnimationRepeat(animation: Animation) {
                }
            })
        }
    }

    fun shouldMoveDown(): PopSaveDrawee {
        shouldMoveDown = true
        return this
    }

    private fun setupPopperGravity() {
        val layoutParams = LinearLayout.LayoutParams(defaultPopperWidth, defaultPopperHeight)
        layoutParams.gravity = if (shouldMoveDown) Gravity.TOP else Gravity.BOTTOM
        frescoImageView?.layoutParams
        frescoImageView?.requestLayout()
        requestLayout()
    }

    fun loadImage(imageUrl: String, resizeWidth: Int = 100, resizeHeight: Int = 100):PopSaveDrawee{
        frescoImageView?.loadImage(imageUrl, resizeWidth, resizeHeight)
        frescoImageView?.requestLayout()
        return this
    }

    fun shouldMoveUp(): PopSaveDrawee {
        shouldMoveDown = false
        return this
    }

    //animations
    private fun slideUpImageAnimation() {
        if(true)return
        frescoImageView?.let {
            frescoImageView!!.animate()
                .setInterpolator(AccelerateDecelerateInterpolator())
                .translationY(if (shouldMoveDown) defaultPopperRootHeight.toFloat() else -defaultPopperRootHeight.toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        frescoImageView?.visibility = View.GONE
                        popSaveRoot?.visibility = View.GONE
                        onFinish?.onFinish()
                        frescoImageView?.animate()?.translationY(0.toFloat())?.setListener(null)
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