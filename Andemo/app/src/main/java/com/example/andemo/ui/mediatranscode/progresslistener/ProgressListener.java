package com.example.andemo.ui.mediatranscode.progresslistener;


public interface ProgressListener {

    void onStart();
    void onFinish(boolean result);
    void onProgress(float progress);
}
