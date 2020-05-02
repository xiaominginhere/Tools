package com.example.tools.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import com.example.tools.utils.engines.GlideEngine;
import com.huantansheng.easyphotos.Builder.AlbumBuilder;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.constant.Type;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.utils.bitmap.SaveBitmapCallBack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 图片选择工具类
 *
 * @Author Jerry
 * @create at 2020/3/4 18:13
 */
public class EasyUtils {
    private final String TAG = "PhotoUtils";

    private static String fileProviderAuthority = "";

    /**
     * 初始化保存FileProvider的配置
     *
     * @param fileProviderAuthority FileProvider的配置路径
     */
    public static void init(String fileProviderAuthority) {
        EasyUtils.fileProviderAuthority = fileProviderAuthority;
    }

    /**
     * 多图片选择器
     *
     * @param activity
     * @param isShowCamera  是否显示摄像机按钮
     * @param isShowGif     是否显示gif图片
     * @param maxCount      最大选择数量
     * @param selects       已选择的图片数组
     * @param selectAdapter 选择的图片返回回调
     */
    public static void openMultipleImages(Activity activity, boolean isShowCamera
            , boolean isShowGif, int maxCount, ArrayList<Photo> selects, SelectAdapter selectAdapter) {
        checkInit();
        EasyPhotos.createAlbum(activity, isShowCamera,
                GlideEngine.getInstance())
                .setFileProviderAuthority(fileProviderAuthority)
                .setCount(maxCount)
                .setGif(isShowGif)
                .setSelectedPhotos(selects)
                .start(new SelectCallback() {
                    @Override
                    public void onResult(ArrayList<Photo> photos,
                                         boolean isOriginal) {
                        if (null != selectAdapter) {
                            if (null == photos || photos.isEmpty()) {
                                selectAdapter.onUnSelected();
                            } else {
                                selectAdapter.onSelected(photos, isOriginal);
                            }
                        }
                    }
                });
    }

    /**
     * 多媒体选择器
     *
     * @param activity
     * @param isShowCamera  是否显示摄像机按钮
     * @param maxImageCount 图片最大选择数量
     * @param maxVideoCount 视频最大选择数量
     * @param selects       已选择的图片数组
     * @param selectAdapter 选择的图片返回回调
     */
    public static void openMultipleMedia(Activity activity, boolean isShowCamera
            , int maxImageCount, int maxVideoCount, ArrayList<Photo> selects
            , SelectAdapter selectAdapter) {
        checkInit();
        EasyPhotos.createAlbum(activity, isShowCamera,
                GlideEngine.getInstance())
                .setFileProviderAuthority(fileProviderAuthority)
                .setVideo(true)
                .setVideoCount(maxVideoCount)
                .setPictureCount(maxImageCount)
                .setGif(true)
                .setSelectedPhotos(selects)
                .start(new SelectCallback() {
                    @Override
                    public void onResult(ArrayList<Photo> photos,
                                         boolean isOriginal) {
                        if (null != selectAdapter) {
                            if (null == photos || photos.isEmpty()) {
                                selectAdapter.onUnSelected();
                            } else {
                                selectAdapter.onSelected(photos, isOriginal);
                            }
                        }
                    }
                });
    }

    /**
     * 多视频选择器
     *
     * @param activity
     * @param isShowCamera  是否显示摄像机按钮
     * @param maxVideoCount 视频最大选择数量
     * @param selects       已选择的图片数组
     * @param selectAdapter 选择的图片返回回调
     */
    public static void openMultipleVideo(Activity activity, boolean isShowCamera
            , int maxVideoCount, ArrayList<Photo> selects
            , SelectAdapter selectAdapter) {
        checkInit();
        EasyPhotos.createAlbum(activity, isShowCamera,
                GlideEngine.getInstance())
                .setFileProviderAuthority(fileProviderAuthority)
                .setVideo(true)
                .setVideoCount(maxVideoCount)
                .filter(Type.VIDEO)
                .setSelectedPhotos(selects)
                .start(new SelectCallback() {
                    @Override
                    public void onResult(ArrayList<Photo> photos,
                                         boolean isOriginal) {
                        if (null != selectAdapter) {
                            if (null == photos || photos.isEmpty()) {
                                selectAdapter.onUnSelected();
                            } else {
                                selectAdapter.onSelected(photos, isOriginal);
                            }
                        }
                    }
                });
    }

    /**
     * 多图片选择器(筛选满足设置图片最小尺寸)
     *
     * @param activity
     * @param isShowCamera  是否显示摄像机按钮
     * @param isShowGif     是否显示gif图片
     * @param maxCount      最大选择数量
     * @param minWidth      最小宽度
     * @param minHeight     最小高度
     * @param selects       已选择的图片数组
     * @param selectAdapter 选择的图片返回回调
     */
    public static void openMultipleImages(Activity activity, boolean isShowCamera
            , boolean isShowGif, int maxCount, int minWidth, int minHeight
            , ArrayList<Photo> selects, SelectAdapter selectAdapter) {
        checkInit();
        AlbumBuilder albumBuilder = EasyPhotos.createAlbum(activity, isShowCamera,
                GlideEngine.getInstance())
                .setFileProviderAuthority(fileProviderAuthority)
                .setCount(maxCount)
                .setGif(isShowGif)
                .setSelectedPhotos(selects);
        if (0 < minWidth) {
            albumBuilder.setMinWidth(minWidth);
        }
        if (0 < minHeight) {
            albumBuilder.setMinHeight(minHeight);
        }
        albumBuilder.start(new SelectCallback() {
            @Override
            public void onResult(ArrayList<Photo> photos,
                                 boolean isOriginal) {
                if (null != selectAdapter) {
                    if (null == photos || photos.isEmpty()) {
                        selectAdapter.onUnSelected();
                    } else {
                        selectAdapter.onSelected(photos, isOriginal);
                    }
                }
            }
        });
    }

    /**
     * 多图片选择器(筛选满足设置图片最小尺寸和显示照片的最小文件大小)
     *
     * @param activity
     * @param isShowCamera  是否显示摄像机按钮
     * @param isShowGif     是否显示gif图片
     * @param maxCount      最大选择数量
     * @param minWidth      最小宽度
     * @param minHeight     最小高度
     * @param minFileSize   设置显示照片的最小文件大小
     * @param selects       已选择的图片数组
     * @param selectAdapter 选择的图片返回回调
     */
    public static void openMultipleImages(Activity activity, boolean isShowCamera
            , boolean isShowGif, int maxCount, int minWidth, long minFileSize, int minHeight
            , ArrayList<Photo> selects, SelectAdapter selectAdapter) {
        checkInit();
        AlbumBuilder albumBuilder = EasyPhotos.createAlbum(activity, isShowCamera,
                GlideEngine.getInstance())
                .setFileProviderAuthority(fileProviderAuthority)
                .setCount(maxCount)
                .setGif(isShowGif)
                .setSelectedPhotos(selects);
        if (minWidth > 0) {
            albumBuilder.setMinWidth(minWidth);
        }
        if (0 < minHeight) {
            albumBuilder.setMinHeight(minHeight);
        }
        if (0 < minFileSize) {
            albumBuilder.setMinFileSize(minFileSize);
        }
        albumBuilder.start(new SelectCallback() {
            @Override
            public void onResult(ArrayList<Photo> photos,
                                 boolean isOriginal) {
                if (null != selectAdapter) {
                    if (null == photos || photos.isEmpty()) {
                        selectAdapter.onUnSelected();
                    } else {
                        selectAdapter.onSelected(photos, isOriginal);
                    }
                }
            }
        });
    }

    /**
     * 多图片选择器(筛选满足显示照片的最小文件大小)
     *
     * @param activity
     * @param isShowCamera  是否显示摄像机按钮
     * @param isShowGif     是否显示gif图片
     * @param maxCount      最大选择数量
     * @param minFileSize   设置显示照片的最小文件大小
     * @param selects       已选择的图片数组
     * @param selectAdapter 选择的图片返回回调
     */
    public static void openMultipleImages(Activity activity, boolean isShowCamera
            , boolean isShowGif, int maxCount, long minFileSize
            , ArrayList<Photo> selects, SelectAdapter selectAdapter) {
        checkInit();
        EasyPhotos.createAlbum(activity, isShowCamera,
                GlideEngine.getInstance())
                .setFileProviderAuthority(fileProviderAuthority)
                .setCount(maxCount)
                .setMinFileSize(minFileSize)
                .setGif(isShowGif)
                .setSelectedPhotos(selects)
                .start(new SelectCallback() {
                    @Override
                    public void onResult(ArrayList<Photo> photos,
                                         boolean isOriginal) {
                        if (null != selectAdapter) {
                            if (null == photos || photos.isEmpty()) {
                                selectAdapter.onUnSelected();
                            } else {
                                selectAdapter.onSelected(photos, isOriginal);
                            }
                        }
                    }
                });
    }


    /**
     * 多视频选择器 (过滤掉小于多少时长的视频和过滤掉大于多少时长的视频)
     *
     * @param activity
     * @param isShowCamera  是否显示摄像机按钮
     * @param maxVideoCount 视频最大选择数量
     * @param minSecond     过滤掉小于多少时长的视频
     * @param maxSecond     过滤掉大于多少时长的视频
     * @param selects       已选择的图片数组
     * @param selectAdapter 选择的图片返回回调
     */
    public static void openMultipleVideo(Activity activity, boolean isShowCamera
            , int maxVideoCount, ArrayList<Photo> selects, int minSecond, int maxSecond
            , SelectAdapter selectAdapter) {
        checkInit();
        AlbumBuilder albumBuilder = EasyPhotos.createAlbum(activity, isShowCamera,
                GlideEngine.getInstance())
                .setFileProviderAuthority(fileProviderAuthority)
                .setVideo(true)
                .setVideoCount(maxVideoCount)
                .filter(Type.VIDEO)
                .setSelectedPhotos(selects);
        if (0 < minSecond) {
            albumBuilder.setVideoMinSecond(minSecond);
        }
        if (0 < maxSecond) {
            albumBuilder.setVideoMaxSecond(maxSecond);
        }
        albumBuilder.start(new SelectCallback() {
            @Override
            public void onResult(ArrayList<Photo> photos,
                                 boolean isOriginal) {
                if (null != selectAdapter) {
                    if (null == photos || photos.isEmpty()) {
                        selectAdapter.onUnSelected();
                    } else {
                        selectAdapter.onSelected(photos, isOriginal);
                    }
                }
            }
        });
    }

    /**
     * 多视频选择器 (过滤掉小于多少时长的视频和过滤掉大于多少时长的视频和满足显示视频的最小文件大小)
     *
     * @param activity
     * @param isShowCamera  是否显示摄像机按钮
     * @param maxVideoCount 视频最大选择数量
     * @param minSecond     过滤掉小于多少时长的视频
     * @param maxSecond     过滤掉大于多少时长的视频
     * @param selects       已选择的图片数组
     * @param minFileSize   设置显示照片的最小文件大小
     * @param selectAdapter 选择的图片返回回调
     */
    public static void openMultipleVideo(Activity activity, boolean isShowCamera
            , int maxVideoCount, ArrayList<Photo> selects, int minSecond, int maxSecond
            , long minFileSize, SelectAdapter selectAdapter) {
        checkInit();
        AlbumBuilder albumBuilder = EasyPhotos.createAlbum(activity, isShowCamera,
                GlideEngine.getInstance())
                .setFileProviderAuthority(fileProviderAuthority)
                .setVideo(true)
                .filter(Type.VIDEO)
                .setVideoCount(maxVideoCount)
                .setSelectedPhotos(selects);
        if (0 < minSecond) {
            albumBuilder.setVideoMinSecond(minSecond);
        }
        if (0 < maxSecond) {
            albumBuilder.setVideoMaxSecond(maxSecond);
        }
        if (0 < minFileSize) {
            albumBuilder.setMinFileSize(minFileSize);
        }
        albumBuilder.start(new SelectCallback() {
            @Override
            public void onResult(ArrayList<Photo> photos,
                                 boolean isOriginal) {
                if (null != selectAdapter) {
                    if (null == photos || photos.isEmpty()) {
                        selectAdapter.onUnSelected();
                    } else {
                        selectAdapter.onSelected(photos, isOriginal);
                    }
                }
            }
        });
    }

    /**
     * 多视频选择器 (满足显示视频的最小文件大小)
     *
     * @param activity
     * @param isShowCamera  是否显示摄像机按钮
     * @param maxVideoCount 视频最大选择数量
     * @param selects       已选择的图片数组
     * @param minFileSize   设置显示照片的最小文件大小
     * @param selectAdapter 选择的图片返回回调
     */
    public static void openMultipleVideo(Activity activity, boolean isShowCamera
            , int maxVideoCount, ArrayList<Photo> selects
            , long minFileSize, SelectAdapter selectAdapter) {
        checkInit();
        EasyPhotos.createAlbum(activity, isShowCamera,
                GlideEngine.getInstance())
                .setFileProviderAuthority(fileProviderAuthority)
                .setVideo(true)
                .setVideoCount(maxVideoCount)
                .setMinFileSize(minFileSize)
                .filter(Type.VIDEO)
                .setSelectedPhotos(selects)
                .start(new SelectCallback() {
                    @Override
                    public void onResult(ArrayList<Photo> photos,
                                         boolean isOriginal) {
                        if (null != selectAdapter) {
                            if (null == photos || photos.isEmpty()) {
                                selectAdapter.onUnSelected();
                            } else {
                                selectAdapter.onSelected(photos, isOriginal);
                            }
                        }
                    }
                });
    }

    /**
     * 保存Bitmap到指定文件夹
     *
     * @param act         上下文
     * @param dirPath     文件夹全路径
     * @param bitmap      bitmap
     * @param namePrefix  保存文件的前缀名，文件最终名称格式为：前缀名+自动生成的唯一数字字符+.png
     * @param notifyMedia 是否更新到媒体库
     * @param callBack    保存图片后的回调，回调已经处于UI线程
     */
    public static void saveBitmap(Activity act, String dirPath, String namePrefix
            , Bitmap mBitmap, boolean notifyMedia, SaveBitmapCallBack callBack) {
        //EasyPhotos.saveBitmapToDir(act, dirPath, namePrefix, bitmap, notifyMedia, callBack); // 暂未发现有什么鬼用包权限异常
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File fileFolder = new File(dirPath);
                    if (!fileFolder.exists())
                        fileFolder.mkdirs();
                    File file = new File(dirPath, namePrefix + System.currentTimeMillis() + ".png");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(file);
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (notifyMedia) {
                                updateMediaStore(act, file);
                            }
                            if (null != callBack) {
                                callBack.onSuccess(file);
                            }
                        }
                    });
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != callBack) {
                                callBack.onIOFailed(e);
                            }
                        }
                    });
                }
                recycleBitmap(mBitmap);
            }
        }).start();
    }

    /**
     * 更新系统文件列表
     * */
    public static void updateMediaStore(Activity act, File file) {
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(act.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        act.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
                , Uri.fromFile(file)));
    }

    /**
     * 回收bitmap数组中的所有图片
     *
     * @param bitmaps 要回收的bitmap数组
     */
    public static void recycleBitmap(Bitmap... bitmaps) {
        EasyPhotos.recycle(bitmaps);
    }

    /**
     * 把View画成Bitmap
     *
     * @param view 要处理的View
     * @return Bitmap
     */
    public static Bitmap createBitmapFromView(View view) {
        return EasyPhotos.createBitmapFromView(view);
    }

    /**
     * 把View转为图片保存到内存
     *
     * @param act        上下文
     * @param dirPath    文件夹全路径
     * @param namePrefix 保存文件的前缀名，文件最终名称格式为：前缀名+自动生成的唯一数字字符+.png
     * @param view       要处理的View
     * @param callBack   保存图片后的回调，回调已经处于UI线程
     */
    public static void saveViewToFile(Activity act, String dirPath, String namePrefix, View view, SaveBitmapCallBack callBack) {
        Bitmap bitmap = createBitmapFromView(view);
        saveBitmap(act, dirPath, namePrefix, bitmap, true, callBack);
    }

    /**
     * 判断是否已配置FileProvider字符串
     * 没有则抛出空指针异常
     */
    private static void checkInit() {
        if (TextUtils.isEmpty(fileProviderAuthority)) {
            throw new NullPointerException("TOOL IS NOT INIT");
        }
    }

    public static class SelectAdapter extends OnSelectedCallBack {

        @Override
        protected void onSelected(ArrayList<Photo> photos, boolean isOriginal) {

        }

        @Override
        protected void onUnSelected() {

        }
    }

    private static abstract class OnSelectedCallBack {
        protected abstract void onSelected(ArrayList<Photo> photos, boolean isOriginal);

        protected abstract void onUnSelected();
    }


}
