package devx.insta.popsave

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.ScaleAnimation

object PopSaveAnimUtils {
    internal fun pop(v: View, listener: AnimationListener?) {
        v.visibility = View.VISIBLE
        val anim: Animation = ScaleAnimation(
            0f, 1f,
            0f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        anim.fillAfter = true
        anim.duration = 200
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.setAnimationListener(listener)
        v.startAnimation(anim)
    }

    fun scaleUp(v: View) {
        val anim: Animation = ScaleAnimation(
            1f, 1.2f,
            1f, 1.2f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        anim.fillAfter = true
        anim.duration = 200
        anim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                val anim1: Animation = ScaleAnimation(
                    1.2f, 1f,
                    1.2f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
                )
                anim1.fillAfter = true
                anim1.duration = 200
                v.startAnimation(anim1)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        v.startAnimation(anim)
    }
}