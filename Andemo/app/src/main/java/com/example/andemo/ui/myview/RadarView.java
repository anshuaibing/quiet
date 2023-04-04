package com.example.andemo.ui.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.andemo.R;

public class RadarView extends View {
//    默认颜色
    private final int DEFAULT_COLOR = Color.parseColor("#87CEEB");

    private int circleColor = DEFAULT_COLOR;

    private int circleNum = 3;

    private int sweepColor = DEFAULT_COLOR;
    //显示交叉线
    private boolean showCross = true;
    //转速
    private float speed = 3.0f;
    //线条宽度
    private int lineWidth = 2;
    //圆圈间隔
    private float interval = 30;
    //扫描方向
    private boolean direction = true;
    private Paint circlePaint;
    private Paint sweepPaint;
    //旋转角度
    private float degrees;
    //默认处于静止状态
//    private boolean isScanning = false;
    private boolean isScanning = true;

    public RadarView(Context context) {
        super(context);
        init();
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        init();
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
        init();
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        if(attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.RadarView);
            circleColor = typedArray.getColor(R.styleable.RadarView_circleColor,DEFAULT_COLOR);
            circleNum = typedArray.getInt(R.styleable.RadarView_circleNum, circleNum);
            if(circleNum < 1) {
                circleNum = 3;
            }
            sweepColor = typedArray.getColor(R.styleable.RadarView_sweepColor, DEFAULT_COLOR);
            showCross = typedArray.getBoolean(R.styleable.RadarView_showCross, true);
            speed = typedArray.getFloat(R.styleable.RadarView_speed, speed);
            if(speed <= 0) {
                speed = 3;
            }
            lineWidth = typedArray.getInt(R.styleable.RadarView_lineWidth, lineWidth);
            interval = typedArray.getFloat(R.styleable.RadarView_interval, interval);
            direction = typedArray.getBoolean(R.styleable.RadarView_direction, direction);
            typedArray.recycle();
        }
    }

    private void init() {

        circlePaint = new Paint();
        circlePaint.setColor(circleColor);
        circlePaint.setStrokeWidth(lineWidth);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);

        sweepPaint = new Paint();
        sweepPaint.setAntiAlias(true);

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultSize = Dp_to_Px(getContext(),400);
        setMeasuredDimension(measureWidth(widthMeasureSpec, defaultSize),
                measureHeigth(heightMeasureSpec, defaultSize));
    }

    private int measureHeigth(int heightMeasureSpec, int defaultSize) {
        int result;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        if(specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize + getPaddingTop() + getPaddingBottom();
            if(specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        result = Math.max(result, getSuggestedMinimumHeight());
        return result;
    }

    private int measureWidth(int widthMeasureSpec, int defaultSize) {
        int result;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if(specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize + getPaddingStart() + getPaddingEnd();
            if(specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        result = Math.max(result, getSuggestedMinimumWidth());
        return result;
    }
    //  绘制
    @Override
    protected void onDraw(Canvas canvas) {
        //半径
        int width = getWidth() - getPaddingStart() - getPaddingEnd();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        int radius = Math.min(width, height) / 2;

        //圆心
        int circleCenter_x = getPaddingStart() + (getWidth() - getPaddingStart() - getPaddingEnd())/2;
        int circleCenter_y = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom())/2;

        drawCircle(canvas, circleCenter_x, circleCenter_y, radius);

        if(showCross) {
            drawCross(canvas, circleCenter_x, circleCenter_y, radius);
        }

        if(isScanning) {
            if(direction) {
                drawSweep(canvas, circleCenter_x, circleCenter_y, radius);
                degrees = (degrees + (360 / speed / 60)) % 360;
            } else {
                drawSweep_back(canvas, circleCenter_x, circleCenter_y, radius);
                degrees = (degrees - (360 / speed / 60)) % 360;
            }
            //重新绘制，不断绘制实现动画
            invalidate();
        }

    }
    //顺时针扫描
    private void drawSweep(Canvas canvas, int circleCenter_x, int circleCenter_y, int radius) {

        SweepGradient sweepGradient = new SweepGradient(circleCenter_x, circleCenter_y,
                new int[]{Color.TRANSPARENT, changeAlpha(sweepColor, 0), changeAlpha(sweepColor, 45),
                        changeAlpha(sweepColor, 90), changeAlpha(sweepColor, 135)},
                new float[]{0.0f, 0.8f, 0.87f, 0.94f, 1f});
        sweepPaint.setShader(sweepGradient);

        canvas.rotate(-90 + degrees, circleCenter_x, circleCenter_y);
        canvas.drawCircle(circleCenter_x, circleCenter_y, radius, sweepPaint);

    }
    //逆时针扫描
    private void drawSweep_back(Canvas canvas, int circleCenter_x, int circleCenter_y, int radius) {

        SweepGradient sweepGradient = new SweepGradient(circleCenter_x, circleCenter_y,
                new int[]{changeAlpha(sweepColor, 135), changeAlpha(sweepColor, 90),
                        changeAlpha(sweepColor, 45), changeAlpha(sweepColor, 0), Color.TRANSPARENT},
                new float[]{0.0f, 0.06f, 0.13f, 0.20f, 1f});
        sweepPaint.setShader(sweepGradient);

        canvas.rotate(-90 + degrees, circleCenter_x, circleCenter_y);
        canvas.drawCircle(circleCenter_x, circleCenter_y, radius, sweepPaint);

    }

    private int changeAlpha(int sweepColor, int alpha) {
        int red = Color.red(sweepColor);
        int green = Color.green(sweepColor);
        int blue = Color.blue(sweepColor);
        return Color.argb(alpha, red, green, blue);
    }
    //画交叉线
    private void drawCross(Canvas canvas, int circleCenter_x, int circleCenter_y, float radius) {
        canvas.drawLine(circleCenter_x - radius, circleCenter_y, circleCenter_x + radius, circleCenter_y, circlePaint);
        canvas.drawLine(circleCenter_x, circleCenter_y - radius, circleCenter_x, circleCenter_y + radius, circlePaint);
    }
    //画圆
    private void drawCircle(Canvas canvas, int circleCenter_x, int circleCenter_y, float radius) {
        for (int i = 0; i< circleNum; i++) {
            if(interval >= radius / (circleNum - 1)) {
                //默认状态
                canvas.drawCircle(circleCenter_x, circleCenter_y, radius - (radius * i / circleNum), circlePaint);
            } else {
                //配置圆圈间隔
                canvas.drawCircle(circleCenter_x, circleCenter_y, radius - i * interval, circlePaint);
            }
        }
    }

    public void start() {
        if(!isScanning) {
            isScanning = true;
            invalidate();
        }
    }

    private int Dp_to_Px(Context context, int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

}
