package com.example.andemo.ui.mediatranscode.videotranscode;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;
import android.os.Build;
import android.util.Log;

import com.example.andemo.ui.designpattern.singletondemo.SingletonDCL;
import com.example.andemo.ui.mediatranscode.opengl.CodecInputSurface;
import com.example.andemo.ui.mediatranscode.progresslistener.SlimProgressListener;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Set;

public class MediaCodecTransCoder {
    private String OUTPUT_DIR="/sdcard/Android/data/com.example.andemo/files/";
    public static final String TAG = "MediaCodecTransCoder";
    private static final boolean VERBOSE = true;
    public static int iKeyFrame=0;

    private final static String MIME_TYPE = "video/avc";

    private static final int OUTPUT_FRAME_RATE = 25;               // 25fps
    private static final int OUTPUT_IFRAME_INTERVAL = 10;          // 10 seconds between I-frames
    private static final int TIMEOUT_USEC = 2500;

//    private OutputImageFormat  outputImageFileType ;
    private static final int COLOR_FormatI420 = 1;
    private static final int COLOR_FormatNV21 = 2;
    private String mSrcVideoPath;
    private String mOutputVideoPath;

    private MediaMuxer mMediaMuxer;
    private MediaCodec mVideoEncoder;
    private MediaCodec mVideoDecoder;
    private MediaCodec.BufferInfo mBufferInfo;
    private int mMuxerVideoTrackIndex;
    private CodecInputSurface mInputSurface;

    private int mNewWidth = -1;
    private int mNewHeight = -1;
    private int mNewBitRate = -1;

    private int mOldWith;
    private int mOldHeight;
    private long mOldDuration;

    // 输入视频到最大输入大小
    private int mMaxVideoIntputSize;
    // 输入视频的帧率
    private int mVideoFrameRate;

    // 输入音频到最大输入大小
    private int mMaxAudioIntputSize;

    private SlimProgressListener mListener;

    private volatile static MediaCodecTransCoder instance;
    public MediaCodecTransCoder() {

    }
    public static MediaCodecTransCoder getMediaCodecTransCoder() {
        if (instance == null) {
            synchronized (SingletonDCL.class) {
                if (instance == null) {
                    instance = new MediaCodecTransCoder();
                }
            }
        }
        return instance;
    }




    /**
     * @param srcVideoPath
     * @param outputVideoPath
     * @param newWidth
     * @param newHeight
     * @param newBitrate
     * @param listener
     * @return
     */
    public boolean convertVideo(final String srcVideoPath,
                                String outputVideoPath,
                                int newWidth,
                                int newHeight,
                                int newBitrate,
                                SlimProgressListener listener) {

        this.mSrcVideoPath = srcVideoPath;
        this.mOutputVideoPath = outputVideoPath;
        this.mListener = listener;

        if (checkParamsError(newWidth, newHeight, newBitrate)) {
            return false;
        }

        // 原始视频的信息
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(mSrcVideoPath);
        mOldWith = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        mOldHeight = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        String rotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        mOldDuration = Long.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) * 1000;

        mNewBitRate = newBitrate;
        mNewWidth = newWidth;
        mNewHeight = newHeight;


        File outputFile = new File(outputVideoPath);
        File inputFile = new File(mSrcVideoPath);
        if (!inputFile.canRead()) {
            return false;
        }

        boolean result = false;

        MediaExtractor videoExtractor = null;
        MediaExtractor audioExtractor = null;

        try {
            videoExtractor = createExtractor();
            int videoExtractorTrackIndex = getAndSelectTrackIndex(videoExtractor, true);

            audioExtractor = createExtractor();
            int audioExtractorTrackIndex = getAndSelectTrackIndex(audioExtractor, false);


            /**MediaMuxer的作用是生成音频或视频文件；还可以把音频与视频混合成一个音视频文件

             相关API介绍：

             MediaMuxer(String path, int format)：path:输出文件的名称、format:输出文件的格式；当前只支持MP4格式；
             addTrack(MediaFormat format)：添加通道；我们更多的是使用MediaCodec.getOutpurForma()
                 或Extractor.getTrackFormat(int index)来获取MediaFormat; 也可以自己创建；
             start()：开始合成文件
             writeSampleData(int trackIndex, ByteBuffer byteBuf, MediaCodec.BufferInfo
             bufferInfo)：把ByteBuffer中的数据写入到在构造器设置的文件中；
             stop()：停止合成文件
             release()：释放资源
**/
            //创建mMediaMuxer实例
            //mOutputVideoPath: 输出文件的名称；MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4: 输出文件的格式，当前只支持 MP4 格式
            mMediaMuxer = new MediaMuxer(mOutputVideoPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);


            //开始分离视频
            if (videoExtractorTrackIndex != -1) {
                //获取制定轨道的通道格式
                MediaFormat videoTrackFormat = videoExtractor.getTrackFormat(videoExtractorTrackIndex);
                Log.e(TAG, "doDownload : videoTrackFormat =  ");
                outputMediaFormat(videoTrackFormat);

                mMaxVideoIntputSize = videoTrackFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
                mVideoFrameRate = videoTrackFormat.getInteger(MediaFormat.KEY_FRAME_RATE);

                Log.e(TAG, "doDownload :"
                        + " mMaxVideoIntputSize =  " + mMaxVideoIntputSize
                        + " mVideoFrameRate = " + mVideoFrameRate);

            }

            //开始分离音频
            int muxerAudioTrackIndex = -1;
            if (audioExtractorTrackIndex >= 0) {



                /**
                seekTo(long timeUs, int mode): 定位到指定的时间点。
                seekTo是MediaExtractor提供的一个方法，用于定位到指定时间点的位置，以便于读取对应时间点的音频或视频数据。

                具体来说，seekTo方法有两个参数，分别是timeUs和mode。其中，timeUs是指定的时间点，单位是微秒，表示从媒体文件的起始位置开始计算的时间。

                其中mode参数表示seek模式，具体包括以下三种：

                **MediaExtractor.SEEK_TO_PREVIOUS_SYNC：**将时间戳定位到当前位置之前最近的一个关键帧位置，这样做可以提高解码器解码的效率，因为可以避免解码无用的数据。

                **MediaExtractor.SEEK_TO_NEXT_SYNC：**将时间戳定位到当前位置之后最近的一个关键帧位置，和SEEK_TO_PREVIOUS_SYNC的效果相反。

                **MediaExtractor.SEEK_TO_CLOSEST_SYNC：**将时间戳定位到离指定时间戳最近的一个关键帧位置，这样做可以提高解码器解码的效率。*/

                audioExtractor.seekTo(0, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);

                MediaFormat audioTrackFormat = audioExtractor.getTrackFormat(audioExtractorTrackIndex);
                Log.e(TAG, "audioTrackFormat =  ");
                outputMediaFormat(audioTrackFormat);
                mMaxAudioIntputSize = audioTrackFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);

                //mMediaMuxer 将音频轨轨添加到MediaMuxer，返回新的轨道
                muxerAudioTrackIndex = mMediaMuxer.addTrack(audioTrackFormat);

            }

            if (newWidth == mOldWith && newHeight == mOldHeight) {
//                PlayerLog.d(TAG, "-------宽高相同，不解码，不编码-------");

                // 宽高相同，不解码，不编码
                //mMediaMuxer 将音频轨轨添加到MediaMuxer，返回新的轨道
                int muxerVideoTrackIndex = mMediaMuxer.addTrack(videoExtractor.getTrackFormat(videoExtractorTrackIndex));
                //mMediaMuxer 开始将视频和音频合成
                mMediaMuxer.start();
                // 写抽取器到muxer
                writeTrackToMuxer(videoExtractor, muxerVideoTrackIndex, mMaxVideoIntputSize, true);

            } else {

                // 解码，再编码 ，使用新的格式
                decodeAndEncodeVideo(videoExtractor, videoExtractorTrackIndex);

            }

            // 直接写音频到muxer
            writeTrackToMuxer(audioExtractor, muxerAudioTrackIndex, mMaxAudioIntputSize, false);

            result = true;

        } catch (Exception e) {
            e.printStackTrace();
            result = false;

        } finally {
            if (audioExtractor != null) {
                audioExtractor.release();
                audioExtractor = null;
            }
        }

        releaseCoder();
        return result;

    }


    private boolean checkParamsError(int newWidth, int newHeight, int newBitrate) {
        if (newWidth <= 0 || newHeight <= 0 || newBitrate <= 0)
            return true;
        else
            return false;
    }

    /**
     * 不解码，编码extractor直接写到muxer
     *
     * @param muxerTrackIndex
     * @param maxIuputSize
     * @param isVideo
     */
    //将音频写到混合器muxer上
    @SuppressLint("WrongConstant")
    private void writeTrackToMuxer(MediaExtractor mediaExtractor, int muxerTrackIndex, int maxIuputSize, boolean isVideo) {


        if (muxerTrackIndex != -1) {
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            bufferInfo.presentationTimeUs = 0;

            ByteBuffer buffer = ByteBuffer.allocate(maxIuputSize);

            boolean extractorDone = false;

            //循环将音频轨或视频轨的数据写到文件
            while (!extractorDone) {
                int sampleSize = mediaExtractor.readSampleData(buffer, 0);
                if (sampleSize < 0) {
                    break;
                }
                if (isVideo) {
                    bufferInfo.offset = 0;
                    bufferInfo.size = sampleSize;
                    bufferInfo.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
                    bufferInfo.presentationTimeUs += 1000 * 1000 / mVideoFrameRate;
                } else {
                    bufferInfo.offset = 0;
                    bufferInfo.size = sampleSize;
                    bufferInfo.flags = mediaExtractor.getSampleFlags();
                    bufferInfo.presentationTimeUs = mediaExtractor.getSampleTime();
                }

                mMediaMuxer.writeSampleData(muxerTrackIndex, buffer, bufferInfo);

                boolean extractorFrameDone = mediaExtractor.advance();
                if (!extractorFrameDone) {
                    extractorDone = true;
                }
            }
        }
    }

    /**
     * 解码->编码视频
     * @param videoExtractor
     * @param videoExtractorTrackIndex
     */
    private void decodeAndEncodeVideo(MediaExtractor videoExtractor, int videoExtractorTrackIndex) {

        MediaFormat videoExtractorTrackFormat = videoExtractor.getTrackFormat(videoExtractorTrackIndex);
        outputMediaFormat(videoExtractorTrackFormat);

        // 初始化解码器 和编码器
        prepareEncoder(videoExtractorTrackFormat);

        try {
            boolean outputDone = false;
            boolean inputDone = false;
            boolean decoderDone = false;

            ByteBuffer[] decoderInputBuffers = null;
            ByteBuffer[] encoderOutputBuffers = null;


            decoderInputBuffers = mVideoDecoder.getInputBuffers();
            encoderOutputBuffers = mVideoEncoder.getOutputBuffers();
            int muxerVideoTrackIndex = -1;


            // 1, 每个循环抽取 一帧
            // 2，内循环 遍历 编码器和解码器的输出buffer

            while (!outputDone) {

                if (!inputDone) {
                    // 抽取一帧

                    boolean extractorSampleDone = false;
                    int index = videoExtractor.getSampleTrackIndex();
                    if (index == -1) {
                        // 抽取采样结束 写EOS帧
                        extractorSampleDone = true;
                        int inputBufIndex = mVideoDecoder.dequeueInputBuffer(TIMEOUT_USEC);

                        if (inputBufIndex >= 0) {
                            mVideoDecoder.queueInputBuffer(inputBufIndex, 0, 0, 0L, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                            // 整个文件输入结束
                            inputDone = true;


                        }
                    } else if (index >= 0) {

                        int inputBufferIndex = mVideoDecoder.dequeueInputBuffer(TIMEOUT_USEC);
                        if (inputBufferIndex >= 0) {
                            ByteBuffer inputBuffer;
                            if (Build.VERSION.SDK_INT < 21) {
                                inputBuffer = decoderInputBuffers[inputBufferIndex];
                            } else {
                                inputBuffer = mVideoDecoder.getInputBuffer(inputBufferIndex);
                            }

                            // 将样本数据存储到字节缓存区
                            // 读取数据到buffer
                            int sampleSize = videoExtractor.readSampleData(inputBuffer, 0);

                            // 如果没有可获取的样本，退出循环
                            if (sampleSize < 0) {
                                mVideoDecoder.queueInputBuffer(inputBufferIndex, 0, 0, 0L, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                                //  整个文件输入结束
                                inputDone = true;


                            } else {
                                mVideoDecoder.queueInputBuffer(inputBufferIndex, 0, sampleSize, videoExtractor.getSampleTime(), 0);
                                // 指针指向 下一帧 读取下一帧数据
                                videoExtractor.advance();

                            }
                        }
                    }
                }

                // 解码器 输出可用
                boolean decoderOutputAvailable = !decoderDone;
                // 编码器 输出可用
                boolean encoderOutputAvailable = true;



                while (decoderOutputAvailable || encoderOutputAvailable) {

                    // 获取带有数据输出buffer
                    int encoderStatus = mVideoEncoder.dequeueOutputBuffer(mBufferInfo, TIMEOUT_USEC);
                    if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {

                        // 暂无编码器输出
                        encoderOutputAvailable = false;

                    } else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {

                        if (Build.VERSION.SDK_INT < 21) {
                            encoderOutputBuffers = mVideoEncoder.getOutputBuffers();
                        }

                    } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {

                        // 新格式，只会调用一次
                        MediaFormat newFormat = mVideoEncoder.getOutputFormat();
                        if (muxerVideoTrackIndex == -1) {
                            muxerVideoTrackIndex = mMediaMuxer.addTrack(newFormat);
                            mMuxerVideoTrackIndex = muxerVideoTrackIndex;
                            mMediaMuxer.start();

                        }

                    } else if (encoderStatus < 0) {
                        throw new RuntimeException("unexpected result from mVideoEncoder.dequeueOutputBuffer: " + encoderStatus);
                    } else {
                        // 编码器的输出Buffer 是要写到 混合器Muxer的

                        ByteBuffer encodedData;
                        if (Build.VERSION.SDK_INT < 21) {
                            encodedData = encoderOutputBuffers[encoderStatus];
                        } else {
                            encodedData = mVideoEncoder.getOutputBuffer(encoderStatus);
                        }
                        if (encodedData == null) {
                            throw new RuntimeException("encoderOutputBuffer " + encoderStatus + " was null");
                        }

                        if (mBufferInfo.size > 1) {
                            if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) == 0) {

                                // 向muxer写数据
                                mMediaMuxer.writeSampleData(mMuxerVideoTrackIndex, encodedData, mBufferInfo);

                            } else if (muxerVideoTrackIndex == -1) {
                                // 刚开始 没有muxer轨道时

                                byte[] csd = new byte[mBufferInfo.size];
                                encodedData.limit(mBufferInfo.offset + mBufferInfo.size);
                                encodedData.position(mBufferInfo.offset);
                                encodedData.get(csd);
                                ByteBuffer sps = null;
                                ByteBuffer pps = null;
                                for (int a = mBufferInfo.size - 1; a >= 0; a--) {
                                    if (a > 3) {
                                        if (csd[a] == 1 && csd[a - 1] == 0 && csd[a - 2] == 0 && csd[a - 3] == 0) {
                                            sps = ByteBuffer.allocate(a - 3);
                                            pps = ByteBuffer.allocate(mBufferInfo.size - (a - 3));
                                            sps.put(csd, 0, a - 3).position(0);
                                            pps.put(csd, a - 3, mBufferInfo.size - (a - 3)).position(0);
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                                // 其实是直接改变Muxer的帧格式 就行
                                MediaFormat newFormat = MediaFormat.createVideoFormat(MIME_TYPE, mNewWidth, mNewHeight);
                                if (sps != null && pps != null) {
                                    newFormat.setByteBuffer("csd-0", sps);
                                    newFormat.setByteBuffer("csd-1", pps);
                                }
                                muxerVideoTrackIndex = mMediaMuxer.addTrack(newFormat);
                                mMediaMuxer.start();
                            }
                        }
                        // 编码器输出完成
                        outputDone = (mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0;

                        // 释放buffer给编码器(encoder)，或者 把buffer渲染到 Surface上(decoder)
                        mVideoEncoder.releaseOutputBuffer(encoderStatus, false);

                    }

                    // 编码器没有输出 下次循环
                    if (encoderStatus != MediaCodec.INFO_TRY_AGAIN_LATER) {

                        continue;
                    }


                    // 编码器 有数据输出了
                    if (!decoderDone) {

                        // 获取解码器输出buffer
                        int decoderStatus = mVideoDecoder.dequeueOutputBuffer(mBufferInfo, TIMEOUT_USEC);


                        if (decoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                            decoderOutputAvailable = false;
                        } else if (decoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {


                        } else if (decoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {

                            MediaFormat newFormat = mVideoDecoder.getOutputFormat();
                            Log.i(TAG, "newFormat = " + newFormat);
                        } else if (decoderStatus < 0) {
                            throw new RuntimeException("unexpected result from mVideoDecoder.dequeueOutputBuffer: " + decoderStatus);
                        } else {
                            boolean doRender = false;
                            // 可以渲染
                            doRender = mBufferInfo.size != 0;

                            // 解码器的输出 是要渲染到 Surface，当然也可以 写到buffer, 这里是把Buffer给SurfaceTexture的图像流
                            mVideoDecoder.releaseOutputBuffer(decoderStatus, doRender);

                            if (doRender) {

                                boolean errorWait = false;
                                try {
                                    mInputSurface.awaitNewImage();
                                } catch (Exception e) {
                                    errorWait = true;
                                    Log.e(TAG, e.getMessage());
                                }
                                if (!errorWait) {
                                    // opengl 画纹理图像Id到Surface
                                    mInputSurface.drawImage();
                                    mInputSurface.setPresentationTime(mBufferInfo.presentationTimeUs * 1000);

                                    if (mListener != null) {
                                        mListener.onProgress((float) mBufferInfo.presentationTimeUs / (float) mOldDuration * 100);
                                    }
                                    // 把当前帧 送给 后边的编码器，去编码
                                    mInputSurface.swapBuffers();
                                }
                            }


/***统计关键帧的数量*/
                            if (MediaCodec.BUFFER_FLAG_KEY_FRAME!=0){
                                iKeyFrame++;
                            }
                            //判断帧
                            if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {


                               decoderDone = true;
                                decoderOutputAvailable = false;
                                Log.e(TAG, "decoder stream end");
                                mVideoEncoder.signalEndOfInputStream();
                            }
                        }
                    }
                }
            }

        }catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (videoExtractor != null) {
                videoExtractor.release();
                videoExtractor = null;
            }
        }

    }


    public int ikey(){
        return iKeyFrame;
    }

    /**
     * 创建收抽取器
     * @return
     * @throws IOException
     */

    /**
     * MediaExtractor的作用是把音频和视频的数据进行分离，即通过MediaExtractor把音频或视频给单独抽取出来

     主要API介绍：

     setDataSource(String path)：即可以设置本地文件又可以设置网络文件
     getTrackCount()：得到源文件通道数
     getTrackFormat(int index)：获取指定（index）的通道格式
     getSampleTime()：返回当前的时间戳
     readSampleData(ByteBuffer byteBuf, int offset)：把指定通道中的数据按偏移量读取到ByteBuffer中；
     advance()：读取下一帧数据
     release():读取结束后释放资源
     **/
    public MediaExtractor createExtractor() throws IOException {
        MediaExtractor mediaExtractor = new MediaExtractor();
        mediaExtractor.setDataSource(mSrcVideoPath);
        return mediaExtractor;
    }

    /**
     * 获取并且选择 轨道
     * @param mediaExtractor
     * @param isVideo
     * @return
     */
    public int getAndSelectTrackIndex(MediaExtractor mediaExtractor, boolean isVideo) {
        //index 轨道索引
        for (int index = 0 ; index < mediaExtractor.getTrackCount(); index ++) {
            // 视频轨道格式信息
            MediaFormat mediaFormat  = mediaExtractor.getTrackFormat(index);

            Log.e(TAG, "getAndSelectTrackIndex " + " index = " + index);
            outputMediaFormat(mediaFormat);

            if (isVideo) {
                if (isVideoTrack(mediaFormat)) {

                    // 切换到想要的轨道
                    mediaExtractor.selectTrack(index);
                    return index;
                }
            } else {
                if (isAudioTrack(mediaFormat)) {

                    // 切换到想要的轨道
                    mediaExtractor.selectTrack(index);
                    return index;
                }
            }
        }
        return -1;
    }


    /**
     * 是否 video轨道
     * @param mediaFormat
     * @return
     */
    public boolean isVideoTrack(MediaFormat mediaFormat) {
        return mediaFormat != null && mediaFormat.getString(MediaFormat.KEY_MIME).startsWith("video/");
    }

    /**
     * 是否 audio 轨道
     * @param mediaFormat
     * @return
     */
    public boolean isAudioTrack(MediaFormat mediaFormat) {
        return mediaFormat != null && mediaFormat.getString(MediaFormat.KEY_MIME).startsWith("audio/");
    }

    /**
     * @param mediaFormat
     */




    private void outputMediaFormat(MediaFormat mediaFormat) {
        Class clazz = MediaFormat.class;
        try {
            Method getMap = clazz.getDeclaredMethod("getMap");
            //获取私有权限
            getMap.setAccessible(true);
            HashMap<String, Object> map = (HashMap<String, Object>) getMap.invoke(mediaFormat);

            if (map != null) {
                Set<String> keySet = map.keySet();
                for (String key : keySet) {
                    Object value = map.get(key);
                    String strValue = "";
                    if (value instanceof Integer) {
                        strValue = ((Integer) value).intValue() + "";
                    } else if (value instanceof Float) {
                        strValue = ((Float) value).floatValue() + "";
                    } else if (value instanceof Long) {
                        strValue = ((Long) value).longValue() + "";
                    } else if (value instanceof String){
                        strValue = (String) value;
                    }

                    Log.e(TAG, "outputMediaFormat " + " key = " + key + " value = " + value);
                }
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }






    /**
     * Configures encoder and muxer state, and prepares the input Surface.
     *  初始化编解码器
     */
    private void prepareEncoder(MediaFormat videoDecoderInputFormat) {


        mBufferInfo = new MediaCodec.BufferInfo();


        // Create a MediaCodec encoder, and configure it with our format.  Get a Surface
        // we can use for input and wrap it with a class that handles the EGL work.
        //
        // If you want to have two EGL contexts -- one for display, one for recording --
        // you will likely want to defer instantiation of CodecInputSurface until after the
        // "display" EGL context is created, then modify the eglCreateContext call to
        // take eglGetCurrentContext() as the share_context argument.
        // 创建编码器
        try {
            mVideoEncoder = MediaCodec.createEncoderByType(MIME_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 配置编码器输出格式
        MediaFormat videoEncoderFormat = MediaFormat.createVideoFormat(MIME_TYPE, mNewWidth, mNewHeight);
        videoEncoderFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        videoEncoderFormat.setInteger(MediaFormat.KEY_BIT_RATE, mNewBitRate);
        videoEncoderFormat.setInteger(MediaFormat.KEY_FRAME_RATE, OUTPUT_FRAME_RATE);
        videoEncoderFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, OUTPUT_IFRAME_INTERVAL);

//        PlayerLog.e(TAG, "videoEncoderFormat: " + videoEncoderFormat);

        // 配置编码器
        mVideoEncoder.configure(videoEncoderFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        // 创建 编码器输入的Surface，作为编码器的数据输入，把buffer渲染到Surface上即是对编码器写数据
        mInputSurface = new CodecInputSurface(mVideoEncoder.createInputSurface());
        mInputSurface.makeCurrent();
        mVideoEncoder.start();

        // 创建解码器
        try {
            mVideoDecoder = MediaCodec.createDecoderByType(videoDecoderInputFormat.getString(MediaFormat.KEY_MIME));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        PlayerLog.e(TAG, "videoDecoderInputFormat: " + videoDecoderInputFormat);

        // 创建opengl 渲染器
        mInputSurface.createRender();
        // 配置 解码器 传入一个Surface，解码数据渲染到surface
        mVideoDecoder.configure(videoDecoderInputFormat, mInputSurface.getSurface(), null, 0);
        mVideoDecoder.start();

        // 解码器的输出和编码器的输入已Surface 相连接

        // Output filename.  Ideally this would use Context.getFilesDir() rather than a
        // hard-coded output directory.

        // Create a MediaMuxer.  We can't add the video track and start() the muxer here,
        // because our MediaFormat doesn't have the Magic Goodies.  These can only be
        // obtained from the encoder after it has started processing data.
        //
        // We're not actually interested in multiplexing audio.  We just want to convert
        // the raw H.264 elementary stream we get from MediaCodec into a .mp4 file.

        mMuxerVideoTrackIndex = -1;
    }

    /**
     * Releases encoder resources.  May be called after partial / failed initialization.
     */
    private void releaseCoder() {
        if (VERBOSE) Log.d(TAG, "releasing encoder objects");
        if (mVideoEncoder != null) {
            mVideoEncoder.stop();
            mVideoEncoder.release();
            mVideoEncoder = null;
        }
        if (mVideoDecoder != null) {
            mVideoDecoder.stop();
            mVideoDecoder.release();
            mVideoDecoder = null;
        }
        if (mInputSurface != null) {
            mInputSurface.release();
            mInputSurface = null;
        }
        if (mMediaMuxer != null) {
            mMediaMuxer.stop();
            mMediaMuxer.release();
            mMediaMuxer = null;
        }
    }

}
