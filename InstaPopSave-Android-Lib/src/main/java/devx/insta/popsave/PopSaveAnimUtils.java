package devx.insta.popsave;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class PopSaveAnimUtils {
    protected static void pop(final View v, Animation.AnimationListener listener) {
        v.setVisibility(View.VISIBLE);
        Animation anim = new ScaleAnimation(
                0f, 1f,
                0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true);
        anim.setDuration(200);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());

        anim.setAnimationListener(listener);

        v.startAnimation(anim);
    }

    public static void scaleUp(final View v) {
        final Animation anim = new ScaleAnimation(
                1f, 1.2f,
                1, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true);
        anim.setDuration(200);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation anim1 = new ScaleAnimation(
                        1.2f, 1f,
                        1.2f, 1f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                anim1.setFillAfter(true);
                anim1.setDuration(200);
                v.startAnimation(anim1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        v.startAnimation(anim);
    }

}
