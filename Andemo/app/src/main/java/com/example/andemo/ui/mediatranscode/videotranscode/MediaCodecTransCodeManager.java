package com.example.andemo.ui.mediatranscode.videotranscode;

import com.example.andemo.ui.mediatranscode.progresslistener.ProgressListener;


public class MediaCodecTransCodeManager {

    private static  TransCodeTask task;

    /**
     * @param srcPath     原地址
     * @param destPath    输出地址
     * @param outputWidth  新宽
     * @param outputHeight 新高
     * @param bitrate      新码率
     * @param listener     监听器
     * @return
     */
    public static TransCodeTask convertVideo(String srcPath, String destPath, int outputWidth, int outputHeight, int bitrate, ProgressListener listener) {
        task = new TransCodeTask(listener);
        task.execute(srcPath, destPath, outputWidth, outputHeight, bitrate);
        return task;
    }

    public static void cancelTransCodeTask() {
        if(task != null) {
            task.cancel(true);
        }
    }


}
