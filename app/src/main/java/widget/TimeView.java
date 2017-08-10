package widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

/**
 * Created by 高俊 on 2017/8/10.
 */

public class TimeView extends View {
    private static final String TAG = "TimeView";
    private Paint mPaint_circle;
    private Paint mPaint_progress_arc;
    private Paint mPaint_text;
    private int width;
    private int height;
    private int currentText = 3;
    private int currentDegree;
    private static final int START = 0;//动画开启状态
    private static final int STOP = 1;

    public TimeView(Context context) {
        this(context, null, 0);
    }

    public TimeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint_circle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_circle.setStyle(Paint.Style.STROKE);
        mPaint_circle.setStrokeWidth(5);
        mPaint_circle.setColor(Color.GRAY);

        mPaint_progress_arc = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_progress_arc.setStyle(Paint.Style.STROKE);
        mPaint_progress_arc.setStrokeWidth(5);
        mPaint_progress_arc.setColor(Color.GRAY);

        mPaint_text = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_text.setStyle(Paint.Style.FILL);
        mPaint_text.setColor(Color.GRAY);
        mPaint_text.setTextSize(60);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        int resultWidth = 150;
        int resultHeight = 150;

        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(resultWidth, resultHeight);
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(resultWidth, sizeHeight);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(sizeWidth, resultHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(width / 2, height / 2, width / 2 - 2, mPaint_circle);
        RectF rectF = new RectF(4 + mPaint_circle.getStrokeWidth(), 4 + mPaint_circle.getStrokeWidth(), width - 4 - mPaint_circle.getStrokeWidth(), height - 4 - mPaint_circle.getStrokeWidth());
        canvas.drawArc(rectF, 0, currentDegree, false, mPaint_progress_arc);
        Rect bounds = new Rect();
        mPaint_text.getTextBounds(currentText + "", 0, (currentText + "").length(), bounds);
        canvas.drawText(currentText + "", width / 2 - bounds.width() / 2, height / 2 + bounds.height() / 2, mPaint_text);
    }

    public void startAnimation() {
        ValueAnimator valueAnimator_arc = ValueAnimator.ofInt(0, 360);//360 1
        valueAnimator_arc.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                currentDegree = animatedValue;
                invalidate();
            }
        });

        ValueAnimator valueAnimator_text = ValueAnimator.ofInt(3, 1);
        valueAnimator_text.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                currentText = animatedValue;
                invalidate();
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(3000);
        animatorSet.setStartDelay(1000);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playTogether(valueAnimator_arc, valueAnimator_text);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mStatusChangeListener != null) {
                    mStatusChangeListener.change(STOP);
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (mStatusChangeListener != null) {
                    mStatusChangeListener.change(START);
                }
            }
        });
        animatorSet.start();
    }

    public interface StatusChangeListener {
        void change(int currentStatus);
    }

    private StatusChangeListener mStatusChangeListener;

    //对外暴露监听方法
    public void setStatusChangeListener(StatusChangeListener statusChangeListener) {
        mStatusChangeListener = statusChangeListener;
    }
}
