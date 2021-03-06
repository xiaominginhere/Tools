package com.example.tools.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;



/**
 * Toast统一管理类
 */
public class ToastUtil {

    private static Toast toast;

    private static Toast initToast(Context mContext,CharSequence message, int duration) {
        if (toast == null) {
            toast = Toast.makeText(mContext, message, duration);
        } else {
            toast.setText(message);
            toast.setDuration(duration);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }


    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(Context context,CharSequence message) {
        initToast(context,message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短时间显示Toast
     *
     * @param strResId
     */
    public static void showShort(Context context,int strResId) {
        initToast(context,context.getResources().getText(strResId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(Context context,CharSequence message) {
        initToast(context,message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param strResId
     */
    public static void showLong(Context context,int strResId) {
        initToast(context,context.getResources().getText(strResId), Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(Context context,CharSequence message, int duration) {
        initToast(context,message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param strResId
     * @param duration
     */
    public static void show(Context context, int strResId, int duration) {
        initToast(context,context.getResources().getText(strResId), duration).show();
    }

}
