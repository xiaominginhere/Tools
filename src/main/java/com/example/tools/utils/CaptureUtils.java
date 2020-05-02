package com.example.tools.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

/**
 * 截屏工具类
 * create by jerry
 * 适用android版本5.0以上系统
 */
public class CaptureUtils {

    private Activity mActivity;
    private String mImagePath;
    private int mWindowWidth;
    private int mWindowHeight;
    private DisplayMetrics displayMetrics;
    private int mScreenDensity;
    private ImageReader mImageReader;
    private MediaProjectionManager mMediaProjectionManager;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private int time = 10;

    private Intent mResultData;
    private int mResultCode;
    private static final int REQUEST_MEDIA_PROJECTION = 10;
    private static final int WHAT_CAPTURE = 11;
    private String mImageName;
    private Bitmap mBitmap;
    private boolean isEnd = true;

    private long startTime = 0;

    private static CaptureUtils captureUtils;

    private OnScreenCaptureListener onScreenCaptureListener;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (WHAT_CAPTURE == msg.what) {
                startCapture();
            }
        }
    };

    public static CaptureUtils getInstance(Activity activity, String dirPath, String namePrefix) {
        if (null == captureUtils) {
            captureUtils = new CaptureUtils();
        }
        captureUtils.setContext(activity, dirPath, namePrefix);
        return captureUtils;
    }

    private CaptureUtils() {

    }

    private void setContext(Activity activity, String dirPath, String namePrefix) {
        if (null != this.mActivity && this.mActivity.equals(activity)) {
            return;
        }
        WeakReference<Activity> contextWeakReference = new WeakReference<>(activity);
        this.mActivity = contextWeakReference.get();
        if (Build.VERSION.SDK_INT >= 21) {
            initEnvironment(dirPath, namePrefix);
        }
    }

    /**
     * 初始化截屏数据
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initEnvironment(String dirPath, String namePrefix) {
        mImagePath = dirPath + File.pathSeparator + namePrefix;
        WindowManager mWindowManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        mWindowWidth = mWindowManager.getDefaultDisplay().getWidth();
        mWindowHeight = mWindowManager.getDefaultDisplay().getHeight();
        displayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenDensity = displayMetrics.densityDpi;
        mImageReader = ImageReader.newInstance(mWindowWidth, mWindowHeight, 0x1, 2);

        mMediaProjectionManager = (MediaProjectionManager)
                mActivity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    /**
     * 开始截屏
     */
    public void beginScreenCapture() {
        if (0 != startTime && System.currentTimeMillis() - startTime < 100) {
            return;
        }
        //判断是否在执行中
        if (!isEnd) {
            return;
        }
        isEnd = false;
        startTime = System.currentTimeMillis();
        if (startScreenCapture()) {
//            handler.sendEmptyMessageDelayed(WHAT_CAPTURE, time);
            handler.sendEmptyMessage(WHAT_CAPTURE);
        }
    }

    public CaptureUtils setOnScreenCaptureListener(OnScreenCaptureListener onScreenCaptureListener) {
        this.onScreenCaptureListener = onScreenCaptureListener;
        return this;
    }

    /**
     * 判断截图数据状态
     */
    private boolean startScreenCapture() {
        Log.e("CaptureUtils", "startScreenCapture");
        if (this == null) {
            return false;
        }
        if (mMediaProjection != null) {
            Log.e("CaptureUtils", "startScreenCapture 1");
            setUpVirtualDisplay();
            return true;
        } else if (mResultCode != 0 && mResultData != null) {
            Log.e("CaptureUtils", "startScreenCapture 2");
            setUpMediaProjection();
            setUpVirtualDisplay();
            return true;
        } else {
            Log.e("CaptureUtils", "startScreenCapture 3");
            mActivity.startActivityForResult(
                    mMediaProjectionManager.createScreenCaptureIntent(),
                    REQUEST_MEDIA_PROJECTION);
            return false;
        }
    }

    /**
     * 创建MediaProjection对象
     */
    private void setUpMediaProjection() {
        mMediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, mResultData);
    }

    /**
     * 处理ActivityResult结果
     */
    public void toActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode != Activity.RESULT_OK) {
                isEnd = false;
                return;
            }
            if (this == null) {
                isEnd = false;
                return;
            }
            mResultCode = resultCode;
            mResultData = data;
            setUpMediaProjection();
            setUpVirtualDisplay();
            Log.e("CaptureUtils", "onActivityResult: " + requestCode);
            handler.postDelayed(() -> startCapture(), time);
        }
    }

    /**
     * 创建VirtualDisplay对象
     */
    private void setUpVirtualDisplay() {
        if (null != mVirtualDisplay) {
            return;
        }
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture",
                mWindowWidth, mWindowHeight, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
    }

    /**
     * 截图获取图片数据
     */
    private void startCapture() {
        mImageName = System.currentTimeMillis() + ".png";
        Image image = mImageReader.acquireLatestImage();
        if (image == null) {
            return;
        }
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        mBitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        mBitmap.copyPixelsFromBuffer(buffer);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height);
        image.close();
        LogUtils.e("startCapture get Image");
        new Thread(() -> {
            saveToFile();
        }).start();


    }

    /**
     * 把图片数据保存到本地
     */
    private void saveToFile() {
        try {
            File fileFolder = new File(mImagePath);
            if (!fileFolder.exists())
                fileFolder.mkdirs();
            File file = new File(mImagePath + mImageName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            stopScreenCapture();
            if (null != onScreenCaptureListener) {
                LogUtils.e("ScreenCapture to file success");
                onScreenCaptureListener.onScreenCapture();
            }
            out.flush();
            out.close();
            updateDatas(file.getAbsolutePath(), file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateDatas(String absolutePath, String fileName) {
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(mActivity.getContentResolver(),
                    absolutePath, fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + absolutePath)));
    }

    /**
     * 暂停屏幕数据截取
     */
    private void stopScreenCapture() {
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }
        isEnd = true;
    }

    /**
     * 销毁对象，释放资源
     */
    public void destroy() {
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
        stopScreenCapture();
        if (mMediaProjection != null) {
            mMediaProjection.stop();
        }
    }

    public interface OnScreenCaptureListener {
        void onScreenCapture();
    }
}
