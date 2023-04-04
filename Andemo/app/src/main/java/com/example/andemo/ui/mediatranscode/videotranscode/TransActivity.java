package com.example.andemo.ui.mediatranscode.videotranscode;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andemo.R;
import com.example.andemo.ui.mediatranscode.progresslistener.ProgressListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransActivity extends Activity {
    private static final int REQUEST_FOR_VIDEO_FILE = 1000;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private TextView mVideoInputInfo;
    private TextView mVideoOutputInfo;
    private TextView mIndicator;
    private TextView mProgress;
    private EditText inputfileName;
    private EditText outputfileName;


    private String inputPath;
    private String outputPath;
    private boolean playOver=false;
    private ProgressBar mProgressBar;
    MediaCodecTransCoder MediaCodectranscoder = MediaCodecTransCoder.getMediaCodecTransCoder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        initView();
    }

    private void initView() {
        mVideoInputInfo = findViewById(R.id.tv_input);
        mVideoOutputInfo = findViewById(R.id.tv_output);
        mIndicator = findViewById(R.id.tv_indicator);
        mProgress = findViewById(R.id.tv_progress);
        mProgressBar = findViewById(R.id.pb_compress);
        inputfileName=findViewById(R.id.infileName);

        Button btn_select = (Button) findViewById(R.id.btn_select);
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputfileNameStr=inputfileName.getText().toString();
                if (!TextUtils.isEmpty(inputfileNameStr) ) {
                    if (!playOver){
                        playOver=true;
                        startConvert();

                    }else {
                        Toast.makeText(TransActivity.this, "请稍等！任务正在进行中！", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(TransActivity.this, "文件名为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        outputfileName=findViewById(R.id.outfileName);

    }

    private void startConvert() {
        //获取待处理文件，这里直接是获取文件路径
        String inputFileName = inputfileName.getText().toString();
        inputPath = new File(getExternalFilesDir(""), inputFileName+".mp4").getAbsolutePath();
        /**MediaMetadataRetriever是Android原生提供的获取音视频文件信息的一个类，我们可以通过这个类的相关方法获取一些基本信息，如视频时长、宽高、帧率、方向、某一帧的图片等。
         * （1）public String extractMetadata (int keyCode)
         * 方法描述：在 setDataSource()之后调用此方法。此方法检索与键值相关联的元数据值。当前支持的键代码被列为Meta DATAXXXX常量。使用任何其他值，它返回空指针
         *
         * （2）public byte[] getEmbeddedPicture ()
         * 方法描述：在 setDataSource()之后调用此方法。此方法找到与数据源相关联的可选图形或相册/封面艺术。如果有超过一张照片，（任何）其中一个被退还
         *
         * （3）public Bitmap getFrameAtTime (long timeUs, int option)
         * 方法介绍:在SETDATABORCEL（）之后调用此方法。该方法通过考虑给定的选项，在给定的时间位置找到具有代表性的帧，并将其作为位图返回。这对于生成输入数据源的缩略图或在给定的时间位置获得和显示帧是有用的。 timeUs :帧将被检索的时间位置。当在给定的时间位置检索帧时，不能保证数据源具有位于该位置的帧。当这种情况发生时，附近的一个框架将被返回。如果TimeSo为负，则将忽略时间位置和选项，并且可以将实现视为代表的任何帧返回 option:关于如何找到框架的提示。如果希望检索一个具有比TimeS更早或相同的时间戳的同步帧，请使用opthyPyviousSyc。如果希望检索一个比TimeS晚于或相同的时间戳的同步帧，请使用opthyNXTySyc。如果想检索一个同步帧，它的时间戳与TimeS最接近或相同，则使用opthyLoestStSyc。如果希望检索一个可能是或可能不是同步帧但与TimeS最接近或相同的帧，则使用OpthOffice最接近。如果没有时间同步帧，OpTestIm最常用的性能开销与其他选项相比更大
         *
         * (4)public Bitmap getFrameAtTime (long timeUs)
         * 方法介绍：在SETDATABORCEL（）之后调用此方法。如果可能的话，该方法找到一个接近给定时间位置的有代表性的帧，并将其作为位图返回。这对于生成输入数据源的缩略图是有用的。如果不关心帧是如何找到的，只要它接近给定的时间，调用这个方法；否则，请调用GETFrimeTimeTime timeUs :帧将被检索的时间位置。当在给定的时间位置检索帧时，不能保证数据源具有位于该位置的帧。当这种情况发生时，附近的一个框架将被返回。如果TimeSo为负，则将忽略时间位置和选项，并且可以将实现视为代表的任何帧返回
         *
         * （5）public Bitmap getFrameAtTime ()
         * 方法介绍：在SETDATABORCEL（）之后调用此方法。如果可能的话，该方法在任何时间位置找到一个有代表性的帧，并将其作为位图返回。这对于生成输入数据源的缩略图是有用的。如果不关心框架的位置，请调用此方法；否则，请调用GETFrimeTimeTime:（long）或GETFrimeTimeTime（long，int）
         *
         * （6）public void release ()
         * 方法介绍：当一个对象完成时调用它。此方法释放内存内部分配的内存
         *
         * （7）public void setDataSource (FileDescriptor fd, long offset, long length)
         *  方法介绍：fd 要播放的文件的文件描述符 offset 开始位置 length 结束位置
         *
         * （8）public void setDataSource (String path)
         * 方法介绍：这个方法主要是设置media类型文件的路径，包括音视频等
         *
         * （9）public void setDataSource (FileDescriptor fd)
         * 方法介绍：这个是是设置文件的描述符
         *
         * （10）public void setDataSource (String uri, Map<String, String> headers)
         * 方法介绍：设置网络音视频url地址，第二个参数是设置参数的请求头
         *
         * （11）public void setDataSource (Context context, Uri uri)
         * 方法介绍：设置网络media文件的url路径
         *
         * （1）METADATA_KEY_ALBUM ：检索数据源的专辑标题信息的元数据键。
         * （2）METADATA_KEY_ALBUMARTIST： 检索与数据源相关的表演者或艺术家的信息的元数据键。
         * （3）METADATA_KEY_ARTIST：检索有关数据源的艺术家的信息的元数据键。
         * （4）METADATA_KEY_AUTHOR：检索有关数据源作者的信息的元数据键 。
         * （5）METADATA_KEY_BITRATE：此键检索平均比特率（以比特/秒），如果可用的话。
         * （6）METADATA_KEY_CD_TRACK_NUMBER：元数据关键字，用于检索描述原始数据记录中音频数据源的顺序的数字字符串
         * （7）METADATA_KEY_COMPILATION：检索音乐专辑编辑状态的元数据键
         * （8）METADATA_KEY_COMPOSER：检索有关数据源的作曲家的信息的元数据键
         * （9）METADATA_KEY_DATE：检索或创建数据源时的日期的元数据键
         * （10）METADATA_KEY_DISC_NUMBER：用于检索描述音频数据源的集合的哪一部分的数字字符串的元数据键
         * （11）METADATA_KEY_DURATION：检索数据源回放持续时间的元数据键
         * (12) METADATA_KEY_GENRE:检索数据源的内容类型或类型的元数据键
         * (13) METADATA_KEY_HAS_AUDIO:如果存在此密钥，则媒体包含音频内容
         * (14)METADATA_KEY_HAS_VIDEO:如果存在此密钥，则媒体包含视频内容
         * (15)METADATA_KEY_LOCATION:此键检索位置信息，如果可用的话。该位置应根据ISO-6709标准，在MP4/3GP框“@ XYZ”下指定。例如，经度为90度和纬度为180度的位置将被检索为“-90＋180”。
         * (16)METADATA_KEY_MIMETYPE:检索数据源MIME类型的元数据键。一些示例MIME类型包括："video/mp4", "audio/mp4", "audio/amr-wb"
         * (17)METADATA_KEY_NUM_TRACKS:元数据键，用于检索数据源（如MP4或3GPP文件）中的音轨的数目，如音频、视频、文本。
         * (18)METADATA_KEY_TITLE:检索数据源标题的元数据键
         * (19) METADATA_KEY_VIDEO_HEIGHT:如果媒体包含视频，则该键检索其高度
         * (20)METADATA_KEY_VIDEO_ROTATION:此键检索视频旋转角度的程度，如果可用的话。视频旋转角度可以是0, 90, 180度，也可以是270度
         * (21)METADATA_KEY_VIDEO_WIDTH:如果媒体包含视频，则该密钥检索其宽度
         * (22)METADATA_KEY_WRITER:检索数据源的作者（如歌词作者）信息的元数据键
         * (23) METADATA_KEY_YEAR:检索创建或修改数据源的一年的元数据密钥
         * (24) OPTION_CLOSEST:此选项与GETFrimeTimeTime（long，int）一起使用，以检索与最接近或给定时间的数据源相关联的帧（不一定是关键帧）
         * (25)OPTION_CLOSEST_SYNC:（时间）或给定时间的数据源相关联的同步（或密钥）帧。
         * (26)OPTION_NEXT_SYNC:此选项与GETFrimeTimeTime（long，int）一起使用，以检索与数据源相关联的同步（或密钥）帧，该数据源位于或在给定的时间之后。
         * (27)OPTION_PREVIOUS_SYNC:此选项与GETFrimeTimeTime（long，int）一起使用，以检索与数据源相关联的同步（或密钥）帧，该数据源正好位于给定时间之前或给定时间*/
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(inputPath);//设置视频路径
        String width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        String height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        String bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        File f = new File(inputPath);
        long fileSize = f.length();//获取文件大小
        Date curDate = new Date(System.currentTimeMillis());
        String before = "inputPath:" +inputPath+ "\n"
                + "width:" + width + "\n"
                + "height:" + height + "\n"
                + "bitrate:" + bitrate + "\n"
                + "fileSize:" + Formatter.formatFileSize(TransActivity.this,fileSize) +"\n"
                +"duration(ms):"+duration;
        mVideoInputInfo.setText(before);
        final String destPath = new File(getExternalFilesDir(""), inputFileName+"转换后.mp4").getAbsolutePath();

        //调用convideo函数，进行转换
        MediaCodecTransCodeManager.convertVideo(inputPath, destPath, 1920, 1080, 200 * 360 * 30,
                new ProgressListener() {//设置监听事件，将转换进度与进度条绑定
                    @Override
                    public void onStart() {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mIndicator.setText("");
                    }

                    @Override
                    public void onFinish(boolean result) {
                        if (result) {
                            mProgress.setText("100%");
                            playOver=false;
                            mProgressBar.setVisibility(View.INVISIBLE);
                            mIndicator.setText("Convert Success!");
                            Date endDate = new Date(System.currentTimeMillis());
                            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                            retriever.setDataSource(destPath);
                            String width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                            String height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                            String bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
                            int ikeyframe=MediaCodectranscoder.ikey();
                            long converttime = endDate.getTime() - curDate.getTime();
                            File f = new File(destPath);
                            long fileSize = f.length();

                            String after = "outputPath:" +destPath+ "\n"
                                    + "width:" + width + "\n"
                                    + "height:" + height + "\n"
                                    + "bitrate:" + bitrate + "\n"
                                    + "converttime（ms）:" + converttime + "\n"
                                    + "ikeyframecount:" + ikeyframe + "\n"
                                    + "fileSize:" + Formatter.formatFileSize(TransActivity.this,fileSize);
                            mVideoOutputInfo.setText(after);
                        } else {

                            mProgress.setText("0%");
                            mIndicator.setText("Convert Failed!");
                            mProgressBar.setVisibility(View.INVISIBLE);

                        }
                    }


                    @Override
                    public void onProgress(float percent) {
                        mProgress.setText(String.valueOf(percent) + "%");
                    }
                });


    };

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config) {
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config) {
        return config.getLocales().get(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaCodecTransCodeManager.cancelTransCodeTask();
    }

//    @Override
//    public void onClick(View v) {
//
//    }


}