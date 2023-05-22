package com.guadou.kt_demo.demo.demo18_customview.takevideo1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.common.util.concurrent.ListenableFuture;
import com.guadou.kt_demo.R;
import com.guadou.lib_baselib.utils.CommUtils;
import com.guadou.lib_baselib.utils.log.YYLogUtils;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 录制视频的自定义控件。包含SurfaceView和一个自定义的button，自定义videoview和确定取消的布局
 * 此类引入了一个xml布局文件 recorder_video_view.xml
 */
public class RecorderVideoView extends LinearLayout {

    //使用Camera1录制
//    private ICameraAction mCameraAction = new Camera1ActionImpl();
    private ICameraAction mCameraAction = new CameraXActionImpl();

    private ImageView mIvclose;
    private ImageView mIvfinish;
    private ViewGroup mRlbottom;
    private MyVideoView mVideoPlay;
    private RecordedButton mShootBtn;
    private OnRecordFinishListener mOnRecordFinishListener;// 录制完成回调接口

    private int mRecordMaxTime;  // 一次拍摄最长时间

    private int mTimeCount;  // 时间计数

    private float mCurProgress = 0.5f;

    public RecorderVideoView(Context context) {
        this(context, null);
    }

    public RecorderVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    /**
     * 主要实现三个参数的构造方法，自定义属性都是默认的，可以在xml文件中自定义
     */
    public RecorderVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecorderVideoView, defStyle, 0);

        mRecordMaxTime = a.getInteger(R.styleable.RecorderVideoView_record_max_time, 10);// 默认为10秒

        a.recycle();


        /*
         * 自定义录像控件填充自定义的布局
         */
        LayoutInflater.from(context).inflate(R.layout.recorder_video_view, this);

        //找到其他的控件
        mVideoPlay = (MyVideoView) findViewById(R.id.vv_play);
        mRlbottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        mIvfinish = (ImageView) findViewById(R.id.iv_finish);
        mIvclose = (ImageView) findViewById(R.id.iv_close);
        mShootBtn = (RecordedButton) findViewById(R.id.shoot_button);
        ViewGroup flCameraContrainer = findViewById(R.id.fl_camera_contrainer);

        // todo CameraAction初始化相机
        flCameraContrainer.addView(mCameraAction.initCamera(getContext()));

        createRecordDir();

        initListener();
    }

    /* =========================== 录制按钮的一些逻辑 =====================================**/

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {

                finishRecode(true);

            } else if (msg.what == 2) {

                mCurProgress += 0.016;

                mShootBtn.setProgress(mCurProgress);

                mHandler.sendEmptyMessageDelayed(2, 16);

            } else if (msg.what == 100) {

                //执行倒计时，计算已录制的时间
                mTimeCount++;

                if (mTimeCount >= mRecordMaxTime) {  // 达到指定时间，停止拍摄
                    mShootBtn.setProgress(mRecordMaxTime);

                    if (mOnRecordFinishListener != null) {
                        mOnRecordFinishListener.onRecordFinish();
                    }

                } else {
                    mHandler.sendEmptyMessageDelayed(100, 1000);
                }

            }
        }
    };


    private void finishRecode(boolean success) {

        stop();

        stopRecord(success);
        releaseRecord();



    }

    /**
     * 点击事件
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {

        mShootBtn.setMax(mRecordMaxTime);

        mShootBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    mShootBtn.startAnim(0, 0.2f);
                    mCurProgress = 0.5f;

                    startRecord(new RecorderVideoView.OnRecordFinishListener() {
                        @Override
                        public void onRecordFinish() {
                            mHandler.sendEmptyMessage(1);
                        }
                    });

                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    if (getTimeCount() > 1)
                        mHandler.sendEmptyMessage(1);

                    else {

                        /* 录制时间小于1秒 录制失败 并且删除保存的文件  */
                        if (getVecordFile() != null) {
                            getVecordFile().delete();
                        }

                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mShootBtn.startAnim(0.2f, 0);
                            }
                        }, 400);

                        finishRecode(false);

                        Toast.makeText(getContext(), "视频录制时间太短", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });


        /*  点击取消 恢复控件显示状态 删除文件 */
        mIvclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoPlay.stop();

                clearWindow();

                mShootBtn.clearProgress();
                mVideoPlay.setVisibility(View.GONE);

                // todo CameraAction是否展示预览页面
                mCameraAction.isShowCameraView(true);

                mRlbottom.setVisibility(View.GONE);
                mShootBtn.setVisibility(View.VISIBLE);

                getVecordFile().delete();
            }
        });


        /*  点击确认 录制完成 可以选择发送或者到另一个界面看视频 */
        mIvfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "录制完成,视频保存的地址：" + getVecordFile().toString(), Toast.LENGTH_SHORT).show();

                if (mCompleteListener != null) {
                    mCompleteListener.onComplete();
                }
            }
        });

    }


    private void createRecordDir() {

        File sampleDir = new File(getContext().getCacheDir().getAbsolutePath() + File.separator + "/videos/");
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }

        File vecordDir = sampleDir;

        // 创建文件
        try {
            File outFile = File.createTempFile("recording", ".mp4", vecordDir);//mp4格式

            // todo CameraAction设置输出文件目标
            mCameraAction.setOutFile(outFile);

//            Log.e("视频输出的文件路径", mVecordFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始录制视频
     */
    public void startRecord(final OnRecordFinishListener onRecordFinishListener) {
        //设置监听
        this.mOnRecordFinishListener = onRecordFinishListener;

        //动画执行
        mHandler.sendEmptyMessage(2);

        // 录制时间记录
        mTimeCount = 0;
        mHandler.sendEmptyMessageDelayed(100, 1000);

        // todo CameraAction调用录制
        mCameraAction.startCameraRecord();
    }

    /**
     * 停止拍摄
     */
    public void stop() {
        mHandler.removeMessages(2);
        mHandler.removeMessages(100);

        mShootBtn.setProgress(0);


        //todo CameraAction释放摄像头资源
        mCameraAction.releaseCamera();
    }

    /**
     * 停止录制
     *
     * @param success
     */
    public void stopRecord(boolean success) {
        //todo CameraAction录制的相关控制
        mCameraAction.stopCameraRecord(() -> {

            if (success) {

                mHandler.post(() -> {

                    /*  录制完成显示 控制控件的显示和隐藏  */
                    mVideoPlay.setVisibility(View.VISIBLE);

                    // todo CameraAction是否展示预览页面
                    mCameraAction.isShowCameraView(false);

                    mRlbottom.setVisibility(View.VISIBLE);

                    mShootBtn.startAnim(0.2f, 0);
                    ValueAnimator anim = mShootBtn.getButtonAnim();

                    if (anim != null && anim.isRunning()) {
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mShootBtn.setVisibility(View.GONE);
                            }
                        });
                    }


                    //录制完成之后展示已经录制的路径下的视频文件
                    mVideoPlay.setVideoPath(getVecordFile().getAbsolutePath());
                    mVideoPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mVideoPlay.setLooping(true);
                            mVideoPlay.start();
                        }
                    });
                    if (mVideoPlay.isPrepared()) {
                        mVideoPlay.setLooping(true);
                        mVideoPlay.start();
                    }

                });

            }
        });
    }

    /**
     * 释放资源
     */
    private void releaseRecord() {
        //todo CameraAction录制的相关控制
        mCameraAction.releaseCameraRecord();
    }


    /**
     * 暴露外界，录制的视频时长
     */
    public int getTimeCount() {
        return mTimeCount;
    }


    /**
     * 暴露外界 保存的视频地址
     */
    public File getVecordFile() {

        // todo CameraAction设置输出文件目标
        return mCameraAction.getOutFile();
    }


    /**
     * 暴露外界，当surfaceview上面有残留画面的时候。重新初始化
     */
    public void clearWindow() {
//        try {
//            mSurfaceView.clearFocus();
//            initCamera();

        // todo CameraAction是否展示预览页面
        mCameraAction.isShowCameraView(true);

        mShootBtn.clearProgress();
        mVideoPlay.setVisibility(View.GONE);
        mRlbottom.setVisibility(View.GONE);
        mShootBtn.setVisibility(View.VISIBLE);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 销毁全部的资源
     */
    public void destoryAll() {
        mShootBtn.clearProgress();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 录制完成回调接口
     */
    public interface OnRecordFinishListener {
        void onRecordFinish();
    }

    /**
     * 用户确认保存录制的视频回调接口.给外部调用
     */
    private OnUserSureCompleteListener mCompleteListener;

    public void setOnUserSureCompleteListener(OnUserSureCompleteListener listener) {
        this.mCompleteListener = listener;
    }

    public interface OnUserSureCompleteListener {
        void onComplete();
    }

}
