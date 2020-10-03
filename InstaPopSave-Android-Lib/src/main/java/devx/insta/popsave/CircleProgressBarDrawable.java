package devx.insta.popsave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

import com.facebook.drawee.drawable.ProgressBarDrawable;

public class CircleProgressBarDrawable extends ProgressBarDrawable {
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mLevel = 0;
    private int maxLevel = 10000;
    private int circleRadius = 45;
    private boolean disableProgress = false;

    public CircleProgressBarDrawable() {
    }

    Context context;

    public CircleProgressBarDrawable(Context context, int circleRadius, boolean disableProgress) {
        this.circleRadius = circleRadius;
        this.disableProgress = disableProgress;
        this.context = context;
    }

    @Override
    protected boolean onLevelChange(int level) {
        mLevel = level;
        invalidateSelf();
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (getHideWhenZero() && mLevel == 0) {
            return;
        }
        drawBar(canvas, maxLevel, ContextCompat.getColor(context, R.color.indicator_light));

        if (!disableProgress) {
            drawBar(canvas, mLevel, ContextCompat.getColor(context,R.color.indicator_grey_dark));
        }
    }

    private void drawBar(Canvas canvas, int level, int color) {
        Rect bounds = getBounds();

//        RectF rectF = new RectF((float) (bounds.right * .4), (float) (bounds.bottom * .4),
//                (float) (bounds.right * .6), (float) (bounds.bottom * .6));

        float x = bounds.right / 2;
        float y = bounds.bottom / 2;
        PointF center = new PointF(x, y);

        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        if (level != 0) {
            ArcUtils.drawArc(canvas, center, circleRadius, 0, (float) (level * 360 / maxLevel), mPaint);
        }
    }
}