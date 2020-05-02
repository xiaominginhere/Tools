package com.example.tools.utils;

import android.text.TextUtils;

import com.orhanobut.hawk.Hawk;

/**
 * 存储数据工具类
 *
 * @Author Jerry
 * @create at 2020/3/4 17:24
 */
public class HawkUtils {

    /**
     * 存放数据
     * @param key 关键字
     * @param value 保存数据
     * */
    public static boolean put(String key,Object value){
        return TextUtils.isEmpty(key) ? false : null == value ? false : Hawk.put(key,value);
    }

    /**
     * 判断是否存在某个关键字的数据
     * @param key 关键字
     * */
    public static boolean isEmpty(String key){
        return TextUtils.isEmpty(key) ? true : !Hawk.contains(key);
    }

    /**
     * 获取某个关键字的数据
     * @param key 关键字
     * */
    public static Object get(String key){
        return TextUtils.isEmpty(key) ? null : isEmpty(key) ? null : Hawk.get(key);
    }

    /**
     * 删除某个关键字的数据
     * @param key 关键字
     * */
    public static boolean delete(String key){
        return TextUtils.isEmpty(key) ? false : Hawk.delete(key);
    }

    /**
     * 清除保存的所有数据
     * */
    public static boolean clean(){
       return Hawk.deleteAll();
    }
}
