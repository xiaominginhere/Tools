package com.example.tools.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * 公共工具类
 *
 * @Author Jerry
 * @create at 2020.03.04 12:17
 */
public class Utils {

    /**
     * 判断字符串是否是json字符串格式
     *
     * @param json 检查字符串
     */
    public static boolean isJsonFormat(String json) {
        JsonElement jsonElement;
        try {
            jsonElement = new JsonParser().parse(json);
            return jsonElement == null ? false : (jsonElement.isJsonObject() || jsonElement.isJsonArray());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * double 保留小数点位数
     *
     * @param num     原始的数量
     * @param pattern 保留位数的格式
     */
    public static String keepDecimalPlaces(double num, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        if (0 > num) {
            return 1 > num ? "-0" + df.format(Math.abs(num)) : "-" + df.format(Math.abs(num));
        } else {
            return 1 > num ? "0" + df.format(num) : df.format(num);
        }
    }

    /**
     * double 保留2位小数点
     *
     * @param num 原始的数量
     */
    public static String keep2DecimalPlaces(double num) {
        return keepDecimalPlaces(num, "#.00");
    }

    /**
     * 解析json数据
     *
     * @param json json字符串
     * @param cls  解析的类
     */
    public static Object parserJson(String json, Class cls) {
        return new Gson().fromJson(json, cls);
    }

    /**
     * 设置富文本适配手机屏幕
     *
     * @param content 富文本内容
     */
    public static String setWebViewContent(String content) {
        return "<html>\n" +
                "    <head>\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "        <style>img{max-width: 100%; width:100%; height:auto;}*{margin:0px;}</style>\n" +
                "    </head>\n" +
                "    <body>" + content.trim() + " </body></html>";
    }

    /**
     * 设置时间戳格式
     *
     * @param format            输出时间格式
     * @param currentTimeMillis 时间戳
     */
    public static String setTime2Format(String format, long currentTimeMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(currentTimeMillis);
    }

    /**
     * 判断字符串是否符合手机号码格式
     * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
     * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
     * 电信号段: 133,149,153,170,173,177,180,181,189
     *
     * @param mobileNum
     * @return 待检测的字符串
     */
    public static boolean isMobileNO(String mobileNum) {
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
        if (TextUtils.isEmpty(mobileNum)) {
            return false;
        }else {
            return mobileNum.matches(telRegex);
        }
    }

}
