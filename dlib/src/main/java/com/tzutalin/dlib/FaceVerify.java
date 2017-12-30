package com.tzutalin.dlib;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;


/**
 * Created by ckt on 4/5/17.
 */
public class FaceVerify {
    private static final String TAG = "dlib";

    private long mNativeContext;
    private String mLandMarkPath = "";
    static {
        try {
            System.loadLibrary("android_dlib");
            Log.d(TAG, "jniNativeClassInit success");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "library not found");
        }
        try {
            jniNativeClassFVInit();
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "jniNativeClassFVInit fail");
        }
    }

    public FaceVerify(String landMarkPath,String verifyPath) {
        mLandMarkPath = landMarkPath;
        Log.d(TAG, "FaceVerify in");
        jniFvInit(mLandMarkPath,verifyPath);
    }
    @Nullable
    @WorkerThread

    public float[][] verify(@NonNull String path) {
        float[][] veryRets = jniVerify(path);
        return veryRets;
    }
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    public void release() {
        jniFvDeInit();
    }

  //  @Keep
  //  private native static void jniNativeClassInit();
    @Keep
    private native static void jniNativeClassFVInit();

    @Keep
    private synchronized native int jniFvInit(String landmarkModelPath,String VerifyModelPath);

    @Keep
    private synchronized native int jniFvDeInit();

    @Keep
    private synchronized native float[][] jniVerify(String path);
}
