package com.example.andemo.ui.openglesdemo;

import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Package com.example.andemo
 * Created by AnShuaiBing on 2023/4/4.
 */
public class GLRenderer implements GLSurfaceView.Renderer {
    private float[] mTriangleArray = {
            0f, 1f, 0f,
            -1f, -1f, 0f,
            1f, -1f, 0f
    };
    //三角形各顶点颜色(三个顶点)
    private float[] mColor = new float[]{
            1, 1, 0, 1,
            0, 1, 1, 1,
            1, 0, 1, 1
    };
    private FloatBuffer mTriangleBuffer;
    private FloatBuffer mColorBuffer;

    public GLRenderer() {
        //点相关
        //先初始化buffer，数组的长度*4，因为一个float占4个字节
        ByteBuffer bb = ByteBuffer.allocateDirect(mTriangleArray.length * 4);
        //以本机字节顺序来修改此缓冲区的字节顺序
        bb.order(ByteOrder.nativeOrder());
        mTriangleBuffer = bb.asFloatBuffer();
        //将给定float[]数据从当前位置开始，依次写入此缓冲区
        mTriangleBuffer.put(mTriangleArray);
        //设置此缓冲区的位置。如果标记已定义并且大于新的位置，则要丢弃该标记。
        mTriangleBuffer.position(0);


        //颜色相关
        ByteBuffer bb2 = ByteBuffer.allocateDirect(mColor.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        mColorBuffer = bb2.asFloatBuffer();
        mColorBuffer.put(mColor);
        mColorBuffer.position(0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {


        // 清除屏幕和深度缓存
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        // 重置当前的模型观察矩阵
        gl.glLoadIdentity();

        // 允许设置顶点
        //GL10.GL_VERTEX_ARRAY顶点数组
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // 允许设置颜色
        //GL10.GL_COLOR_ARRAY颜色数组
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        //将三角形在z轴上移动
        gl.glTranslatef(0f, 0.0f, -2.0f);

        // 设置三角形
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mTriangleBuffer);
        // 设置三角形颜色
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
        // 绘制三角形
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);


        // 取消颜色设置
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        // 取消顶点设置
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        //绘制结束
        gl.glFinish();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = (float) width / height;
        // 设置OpenGL场景的大小,(0,0)表示窗口内部视口的左下角，(w,h)指定了视口的大小
        gl.glViewport(0, 0, width, height);
        // 设置投影矩阵
        gl.glMatrixMode(GL10.GL_PROJECTION);
        // 重置投影矩阵
        gl.glLoadIdentity();
        // 设置视口的大小
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
        //以下两句声明，以后所有的变换都是针对模型(即我们绘制的图形)
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 设置白色为清屏
        gl.glClearColor(1, 1, 1, 1);

    }

//
//
///** Renderer显示背景颜色
// **/
//    @Override
//    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//        /**在绘制3D模型时，我们首先要对画板的背景颜色绘制好，颜色可以随便选。
//         glClearColor函数是设置清屏的颜色，参数分别对应RGBA，我们设置为红色就是glClearColor(1f, 0f, 0f, 0f);
//         ,为啥红色是1呢？而不是我们平时所熟知的255，
//         请注意：在使用OpenGL时，很多地方采用的参数变化范围都是从0到1，比如在贴纹理的时候选择图片区域也是[0,1]。
//         也就是说，rgba的取值都是从0~1**/
//        gl.glClearColor(1f, 0f, 0f, 0f);
//    }
//
//    @Override
//    public void onSurfaceChanged(GL10 gl, int width, int height) {
//        /**然后是设置视角窗口大小glViewport，其实就是决定绘制的矩形区域的大小，
//        当然并不是这么简单，后面我会详细讲，目前就可以把它理解为绘制的区域，
//        在GLSurfaceView窗口大小发生变化时我们动态改变视角窗口。**/
//        gl.glViewport(0, 0, width, height);
//    }
//
//    @Override
//    public void onDrawFrame(GL10 gl) {
//        /**最后就是真正的绘制图形啦，我们先啥也不干，就针对画板“刷一次油漆”，把画板背景颜色设置为红色，
//         glClear(GL10.GL_COLOR_BUFFER_BIT)的意思是，使用glClearColor函数所设置的颜色进行清屏。*/
//        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//    }
}