package com.guadou.kt_demo.demo.demo18_customview.takevideo1.camear2_mamager;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.guadou.lib_baselib.utils.log.YYLogUtils;

/**
 * 自定义HanderThread与Handler
 */
public abstract class BaseMessageLoop {

    private volatile MsgHandlerThread mHandlerThread;
    private volatile Handler mHandler;
    private String mName;

    public BaseMessageLoop(Context context, String name) {
        mName = name;
    }

    public MsgHandlerThread getHandlerThread() {
        return mHandlerThread;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void Run() {
        Quit();
        //LogUtil.v(TAG,  mName + " HandlerThread Run");
        synchronized (this) {
            mHandlerThread = new MsgHandlerThread(mName);
            mHandlerThread.start();
            mHandler = new Handler(mHandlerThread.getLooper(), mHandlerThread);
        }
    }

    public void Quit() {
        //LogUtil.v(TAG,  mName + " HandlerThread Quit");
        synchronized (this) {
            if (mHandlerThread != null) {
                mHandlerThread.quit();
            }

            if (mHandler != null) {
                mHandler.removeCallbacks(mHandlerThread);
            }

            mHandlerThread = null;
            mHandler = null;
        }
    }

    public void sendMessage(int what, int arg1, int arg2) {
        synchronized (this) {
            if (mHandler != null) {
                mHandler.sendMessage(mHandler.obtainMessage(what, arg1, arg2));
            } else {
                YYLogUtils.e( "mHandler == null");
            }
        }
    }

    public void sendMessage(int what, int arg1, int arg2, Object obj) {
        synchronized (this) {
            if (mHandler != null) {
                mHandler.sendMessage(mHandler.obtainMessage(what, arg1, arg2, obj));
            } else {
                YYLogUtils.e( "mHandler == null");
            }
        }
    }

    public void sendMessageDelayed(int what, int arg1, int arg2, Object obj, long delayMillis) {
        synchronized (this) {
            if (mHandler != null) {
                mHandler.sendMessageDelayed(mHandler.obtainMessage(what, arg1, arg2, obj), delayMillis);
            } else {
                YYLogUtils.e( "mHandler == null");
            }
        }
    }


    public void sendEmptyMessage(int what) {
        synchronized (this) {
            if (mHandler != null) {
                mHandler.sendEmptyMessage(what);
            } else {
                YYLogUtils.e( "mHandler == null");
            }
        }
    }

    public void sendEmptyMessageDelayed(int what, long delayMillis) {
        synchronized (this) {
            if (mHandler != null) {
                mHandler.sendEmptyMessageDelayed(what, delayMillis);
            } else {
                YYLogUtils.e( "mHandler == null");
            }
        }
    }

    public boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        synchronized (this) {
            if (mHandler != null) {
                return mHandler.sendEmptyMessageAtTime(what, uptimeMillis);
            } else {
                YYLogUtils.e( "mHandler == null");
            }
        }
        return false;
    }

    public void removeMessages(int what) {
        synchronized (this) {
            if (mHandler != null) {
                mHandler.removeMessages(what);
            } else {
                YYLogUtils.e( "mHandler == null");
            }
        }
    }

    public void removeMessages(int what, Object object) {
        synchronized (this) {
            if (mHandler != null) {
                mHandler.removeMessages(what, object);
            } else {
                YYLogUtils.e( "mHandler == null");
            }
        }
    }

    abstract protected boolean recvHandleMessage(Message msg);

    private class MsgHandlerThread extends HandlerThread implements Handler.Callback {

        public MsgHandlerThread(String name) {
            super(name);
        }

        public MsgHandlerThread(String name, int priority) {
            super(name, priority);
        }

        @Override
        public boolean handleMessage(Message msg) {
            return recvHandleMessage(msg);
        }
    }

}
