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
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.guadou.kt_demo.R;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 录制视频的自定义控件。包含SurfaceView和一个自定义的button，自定义videoview和确定取消的布局
 * 此类引入了一个xml布局文件 recorder_video_view.xml
 */
public class RecorderVideoView extends LinearLayout implements MediaRecorder.OnErrorListener {

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private MediaRecorder mMediaRecorder;
    private android.widget.ImageView mIvclose;
    private android.widget.ImageView mIvfinish;
    private android.widget.RelativeLayout mRlbottom;
    private MyVideoView mVideoPlay;
    private RecordedButton mShootBtn;
    private Camera mCamera;
    private Timer mTimer;// 计时器
    private OnRecordFinishListener mOnRecordFinishListener;// 录制完成回调接口
    private int mWidth;// 视频分辨率宽度
    private int mHeight;// 视频分辨率高度
    private boolean isOpenCamera;// 是否一开始就打开摄像头
    private int mRecordMaxTime;// 一次拍摄最长时间
    private int mTimeCount;// 时间计数
    private File mVecordFile = null;// 文件
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
        mWidth = a.getInteger(R.styleable.RecorderVideoView_record_width, 320);// 默认320
        mHeight = a.getInteger(R.styleable.RecorderVideoView_record_height, 240);// 默认240
        isOpenCamera = a.getBoolean(R.styleable.RecorderVideoView_is_open_camera, true);// 默认打开
        mRecordMaxTime = a.getInteger(R.styleable.RecorderVideoView_record_max_time, 10);// 默认为10

        /*
         * 自定义录像控件填充自定义的布局
         */
        LayoutInflater.from(context).inflate(R.layout.recorder_video_view, this);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        //找到其他的控件
        mVideoPlay = (MyVideoView) findViewById(R.id.vv_play);
        mRlbottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        mIvfinish = (ImageView) findViewById(R.id.iv_finish);
        mIvclose = (ImageView) findViewById(R.id.iv_close);
        mShootBtn = (RecordedButton) findViewById(R.id.shoot_button);


        initEvent();


        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new CustomCallBack());
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        a.recycle();
    }


    /* =========================== 录制按钮的一些逻辑 =====================================**/


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1)
                finishRecode();
            else if (msg.what == 2) {

                mCurProgress += 0.05;

                if (mCurProgress <= 10)
                    mShootBtn.setProgress(mCurProgress);
                else {
                    mShootBtn.setProgress(mCurProgress);
                    mHandler.removeMessages(2);
                }
                mHandler.sendEmptyMessageDelayed(2, 50);
            }
        }
    };


    private void finishRecode() {
        stop();

        /*  录制完成显示 控制控件的显示和隐藏  */
        mVideoPlay.setVisibility(View.VISIBLE);
        mSurfaceView.setVisibility(GONE);
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


        mVideoPlay.setVideoPath(getVecordFile().toString());
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


    }


    /**
     * 点击事件
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {

        mShootBtn.setMax(mRecordMaxTime);

        mShootBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    /*  按下去了超过了10秒 自动回调这个接口 录制完成  */
                    mShootBtn.startAnim(0, 0.2f);
                    mCurProgress = 0.5f;
                    mHandler.sendEmptyMessage(2);
                    record(new RecorderVideoView.OnRecordFinishListener() {
                        @Override
                        public void onRecordFinish() {
                            mHandler.removeMessages(2);
                            mHandler.sendEmptyMessage(1);
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mHandler.removeMessages(2);
                    /*  按下去了没有10秒收到抬起手指 并且录制时间大于1秒 录制完成  */
                    if (getTimeCount() > 1)
                        mHandler.sendEmptyMessage(1);
                    else {

                        /* 录制时间小于1秒 录制失败 并且删除保存的文件  */
                        if (getVecordFile() != null)
                            getVecordFile().delete();
                        stop();
                        Toast.makeText(getContext(), "视频录制时间太短", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });


        /**  点击取消 恢复控件显示状态 删除文件 **/
        mIvclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoPlay.stop();
                clearWindow();
                mShootBtn.clearProgress();
                mVideoPlay.setVisibility(View.GONE);
                mSurfaceView.setVisibility(View.VISIBLE);
                mRlbottom.setVisibility(View.GONE);
                mShootBtn.setVisibility(View.VISIBLE);


                getVecordFile().delete();
            }
        });


        /**  点击确认 录制完成 可以选择发送或者到另一个界面看视频 随意 **/
        mIvfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "录制完成,视频保存的地址：" + getVecordFile().toString(), Toast.LENGTH_SHORT).show();


                if (mCompleteListener != null)
                    mCompleteListener.onComplete();
            }
        });

    }

    /* =========================== SurfaceView的Callback ===================================**/


    /**
     * SurfaceView的回调中创建surface的时候初始化摄像头操作
     * 销毁surface的时候释放资源
     */
    private class CustomCallBack implements SurfaceHolder.Callback {


        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (!isOpenCamera)
                return;
            try {
                initCamera();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


        }


        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (!isOpenCamera)
                return;
            freeCameraResource();
        }
    }


    /* =========================== 摄像头的初始化 =====================================**/


    /**
     * 初始化摄像头。尝试打开摄像头
     */
    private void initCamera() throws IOException {
        if (mCamera != null) {
            freeCameraResource();
        }
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
            freeCameraResource();
        }
        if (mCamera == null)
            return;


        setCameraParams();
        mCamera.setDisplayOrientation(90);   //设置拍摄方向为90度（竖屏）
        mCamera.setPreviewDisplay(mSurfaceHolder);
        mCamera.startPreview();
        mCamera.unlock();
    }


    /**
     * 设置摄像头为竖屏
     */
    private void setCameraParams() {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.set("orientation", "portrait");
            mCamera.setParameters(params);
        }
    }


    /**
     * 释放摄像头资源
     */
    private void freeCameraResource() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;
        }
    }


    /* =========================== 录制视频的输出路径 ===================================**/


    private void createRecordDir() {
        File sampleDir = new File(Environment.getExternalStorageDirectory() + File.separator +
                "im/video/");
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        File vecordDir = sampleDir;
        // 创建文件
        try {
            mVecordFile = File.createTempFile("recording", ".mp4", vecordDir);//mp4格式
//            Log.e("视频输出的文件路径", mVecordFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /* ==================== 初始化Android的MediaRecoder录制 =====================**/


    /**
     * 初始化
     */
    private void initRecord() throws IOException {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.reset();
        if (mCamera != null)
            mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 视频源
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 音频源
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// 视频输出格式
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 音频格式
        mMediaRecorder.setVideoSize(mWidth, mHeight);// 设置分辨率：
        // mMediaRecorder.setVideoFrameRate(16);// 这个我把它去掉了，感觉没什么用
        mMediaRecorder.setVideoEncodingBitRate(1 * 1024 * 512);// 设置帧频率，然后就清晰了
        mMediaRecorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);// 视频录制格式
        // mediaRecorder.setMaxDuration(Constant.MAXVEDIOTIME * 1000);
        mMediaRecorder.setOutputFile(mVecordFile.getAbsolutePath());
        mMediaRecorder.prepare();
        try {
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 开始录制视频
     */
    public void record(final OnRecordFinishListener onRecordFinishListener) {
        this.mOnRecordFinishListener = onRecordFinishListener;
        createRecordDir();
        try {
            if (!isOpenCamera)// 如果未打开摄像头，则打开
                initCamera();


            initRecord();
            mTimeCount = 0;// 时间计数器重新赋值
            mTimer = new Timer();  //开启定时器.看是否到了10秒钟
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mTimeCount++;


                    if (mTimeCount == mRecordMaxTime) {       // 达到指定时间，停止拍摄
                        mHandler.removeMessages(2);           //让handler进度停止
                        stop();
                        if (mOnRecordFinishListener != null)
                            mOnRecordFinishListener.onRecordFinish();
                    }
                }
            }, 0, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 停止拍摄
     */
    public void stop() {
        stopRecord();
        releaseRecord();
        freeCameraResource();
    }


    /**
     * 停止录制
     */
    public void stopRecord() {
        mShootBtn.setProgress(0);
        if (mTimer != null)
            mTimer.cancel();
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 释放资源
     */
    private void releaseRecord() {
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            try {
                mMediaRecorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mMediaRecorder = null;
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
        return mVecordFile;
    }


    /**
     * 暴露外界，当surfaceview上面有残留画面的时候。重新初始化
     */
    public void clearWindow() {
        try {
            mSurfaceView.clearFocus();
            initCamera();
            mShootBtn.clearProgress();
            mVideoPlay.setVisibility(View.GONE);
            mSurfaceView.setVisibility(View.VISIBLE);
            mRlbottom.setVisibility(View.GONE);
            mShootBtn.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        try {
            if (mr != null)
                mr.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
