package com.example.andemo.ui.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.andemo.R;

public class MyView extends View {
    private Paint paint;//画笔
    private String text;
    private static final String TAG="MyViewAAA";

    /**
     * 不带属性的构造方法
     * @param context
     */
    public MyView(Context context) {
        super(context);
        Log.v(TAG, "***MyView不带属性的构造方法");

        paint = new Paint();//初始化画笔
        paint.setColor(Color.BLACK);//画笔颜色-黑色
        paint.setTextSize(20);//画笔尺寸

    }
    /**
     * 带属性的构造方法
     * @param context
     * @param attrs
     */
    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.v(TAG, "***MyView带属性的构造方法");

        paint = new Paint();//初始化画笔

        // 获取属性的方式
        // 这里取得 declare-styleable 集合
        TypedArray typedArray =  context.obtainStyledAttributes(attrs,R.styleable.myView);

        //参数1：直接内嵌在View中的属性集合。这里直接使用 MyView构造方法的参数2
        //参数2：想要获取的属性集合。
        //通常对应values文件下，自定义的attrs.xml资源文件里面，某个declare-styleable(风格样式)
        //obtainStyledAttributes(AttributeSet set, int[] attrs)

        // 这里从集合里取出相对应的属性值。 参数2：如果使用者没有配置该属性时，所用的默认值
        int color = typedArray.getColor(R.styleable.myView_textColor,  Color.GREEN);
        float size = typedArray.getDimension(R.styleable.myView_textSize, 26);
        text = typedArray.getString(R.styleable.myView_text);

        paint.setTextSize(size);//设置自己的类成员变量
        paint.setColor(color);//画笔颜色-绿色
        typedArray.recycle();//关闭资源

    }


    /**
     * 初始化组件的时候，被触发，进行组件的渲染
     * Canvas canva 画布
     */
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Log.v(TAG, "***onDraw方法");

        paint.setStyle(Paint.Style.FILL);//设置画笔的风格-实心

        canvas.drawRect(new Rect(10, 10, 90, 90), paint);//绘制矩形

        paint.setColor(Color.BLUE);//画笔颜色-蓝色
        canvas.drawText(text,15, 120, paint);//绘制文本


    }



}
