package com.example.andemo.ui.mediatranscode.videotranscode;

import android.os.AsyncTask;

import com.example.andemo.ui.mediatranscode.progresslistener.ProgressListener;
import com.example.andemo.ui.mediatranscode.progresslistener.SlimProgressListener;


/**
 * A asyncTask to convert video
**/
public class TransCodeTask extends AsyncTask<Object, Float, Boolean> {
    private ProgressListener mListener;

    public TransCodeTask(ProgressListener listener) {
        mListener = listener;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mListener != null) {
            mListener.onStart();
        }
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        // 开始转换
        return new MediaCodecTransCoder().convertVideo(
                (String)params[0],
                (String)params[1],
                (Integer)params[2],
                (Integer)params[3],
                (Integer)params[4], new SlimProgressListener() {
            @Override
            public void onProgress(float percent) {
                publishProgress(percent);
            }
        });
    }

    @Override
    protected void onProgressUpdate(Float... percent) {
        super.onProgressUpdate(percent);
        if (mListener != null) {
            mListener.onProgress(percent[0]);
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (mListener != null) {
            if (result) {
                mListener.onFinish(true);
            } else {
                mListener.onFinish(false);
            }
        }
    }

}
