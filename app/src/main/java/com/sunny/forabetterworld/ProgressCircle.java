package com.sunny.forabetterworld;

/**
 * Created by Sunny on 26-02-2016.
 */
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import java.math.BigDecimal;



public class ProgressCircle extends View {

    Context context;
    public static final String DAILY_KEY = "DAILY_EMISSION";
    public static final String WEEKLY_KEY = "WEEKLY_EMISSION";
    public static final String DAY_1 = "DAY_1";
    public static  final String DAY_2 = "DAY_2";
    public static final String DAY_3 = "DAY_3";
    public static final String DAY_4 = "DAY_4";
    public static final String DAY_5 = "DAY_5";
    public static final String DAY_6 = "DAY_6";
    public static final String DAY_7 = "DAY_7";
    public static int flag = 0;
    private final RectF mOval = new RectF();
    private float mSweepAngle = 0;
    private int startAngle = 90;
    private int angleGap = 4;
    private Bitmap icon;
    private SharedPreference sharedPreference;


    float mEndAngle = 1.0f;

    Paint progressPaint = new Paint();
    TextPaint textPaint = new TextPaint();
    Paint incompletePaint = new Paint();
    Paint percentagePaint = new Paint();

    private float strokeWidth = 30.0f;

    public ProgressCircle(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ProgressCircle,
                0, 0);

        int textColor;
        float textSize;

        int progressColor;
        int incompleteColor;

        try {
            textColor = a.getColor(R.styleable.ProgressCircle_android_textColor, ContextCompat.getColor(context, android.R.color.holo_red_dark));
            textSize = a.getDimension(R.styleable.ProgressCircle_android_textSize, 100);

            strokeWidth = a.getDimension(R.styleable.ProgressCircle_strokeWidth, 30.0f);

            progressColor = a.getColor(R.styleable.ProgressCircle_progressColor, ContextCompat.getColor(context,android.R.color.holo_blue_bright));
            incompleteColor = a.getColor(R.styleable.ProgressCircle_incompleteProgressColor, ContextCompat.getColor(context,android.R.color.darker_gray));
        } finally {
            a.recycle();
        }

        progressPaint.setColor(progressColor);
        progressPaint.setStrokeWidth(strokeWidth);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize/2);
        Typeface tf = Typeface.create("Roboto Condensed Light", Typeface.BOLD);
        textPaint.setTypeface(tf);

        percentagePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        percentagePaint.setColor(textColor);
        percentagePaint.setTextSize(textSize/3);

        incompletePaint.setColor(incompleteColor);
        incompletePaint.setStrokeWidth(strokeWidth);
        incompletePaint.setStyle(Paint.Style.STROKE);
        incompletePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

//        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_placeholder);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        context = getContext();
        SharedPreference sharedPreference = new SharedPreference();
 //       SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String test = sharedPreference.getValue(context,"Flag");
 //       List<String> temp = null;
        if( test != null && Integer.valueOf(sharedPreference.getValue(context,"Flag")) == 1){
           // temp = sharedPreference.getValueArray(context, WEEKLY_KEY);
            float data = 0.0f;
            for(int i=0;i<7;i++){
                String temp = sharedPreference.getValue(context,"DAY_" + String.valueOf(i+1));
                if(temp == null) data += 0.0f;
                else data += Float.valueOf(temp);
            }
            BigDecimal bd = new BigDecimal(Float.toString(data));
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            data = bd.floatValue();
            test = String.valueOf(data);
        }else{
            test = sharedPreference.getValue(context, DAILY_KEY);
            float data;
            if(test == null) data = 0.0f;
            else data = Float.valueOf(test);
            BigDecimal bd = new BigDecimal(Float.toString(data));
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            data = bd.floatValue();
            test = String.valueOf(data);
        }
        float currentAngleGap = mSweepAngle == 1.0f || mSweepAngle == 0 ? 0 : angleGap;
        mOval.set(strokeWidth / 2, strokeWidth / 2, getWidth() - (strokeWidth / 2), getWidth() - (strokeWidth / 2));
        canvas.drawArc(mOval, -startAngle + currentAngleGap, (mSweepAngle * 360) - currentAngleGap, false,
                progressPaint);

        mOval.set(strokeWidth / 2, strokeWidth / 2, getWidth() - (strokeWidth / 2), getWidth() - (strokeWidth / 2));
        canvas.drawArc(mOval, mSweepAngle * 360 - startAngle + currentAngleGap, 360 - (mSweepAngle * 360) - currentAngleGap, false,
                incompletePaint);

        drawText(canvas, textPaint, test, percentagePaint);

        if(icon != null)
            canvas.drawBitmap(icon, canvas.getWidth() / 2 - icon.getWidth() / 2, strokeWidth + (canvas.getHeight() / 15), null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    private void drawText(Canvas canvas, Paint paint, String text, Paint percentagePaint) {

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        Rect percentageBounds = new Rect();
        percentagePaint.getTextBounds("kg", 0, 1, percentageBounds);
        int x = (canvas.getWidth() / 2) - (bounds.width() / 2) - (percentageBounds.width() / 2);
        int y = (canvas.getHeight() / 2) + (bounds.height() / 2);
        canvas.drawText(text, x, y, paint);
        //canvas.drawText(String.valueOf(val),x,y,paint);
        canvas.drawText("kg", x + bounds.width() + percentageBounds.width() / 2, y - bounds.height() + percentageBounds.height(), percentagePaint);


    }



    public void setTextColor(int color) {
        textPaint.setColor(color);
    }

    public void setProgressColor(int color) {
        progressPaint.setColor(color);
    }

    public void setIncompleteColor(int color) {
        incompletePaint.setColor(color);
    }

    public void setProgress(float progress) {
        if (progress > 1.0f || progress < 0) {
            throw new RuntimeException("Value must be between 0 and 1: " + progress);
        }

        mEndAngle = progress;

        this.invalidate();
    }

    public void startAnimation() {
        ValueAnimator anim = ValueAnimator.ofFloat(mSweepAngle, mEndAngle);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ProgressCircle.this.mSweepAngle = (Float) valueAnimator.getAnimatedValue();
                ProgressCircle.this.invalidate();
            }
        });
        anim.setDuration(5000);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }
}
