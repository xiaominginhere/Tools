package com.example.tools;

import android.content.Context;

import com.example.tools.utils.EasyUtils;
import com.example.tools.utils.PackageUtils;
import com.orhanobut.hawk.Hawk;
import com.tencent.bugly.crashreport.CrashReport;


import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ToolInitial {

    /**
     * 初始化工具库
     *
     * @param mContext
     */
    public static void init(Context mContext, String fileProviderAuthority) {
        Hawk.init(mContext).build();
        EasyUtils.init(fileProviderAuthority);
        handleSSLHandshake();
    }

    /**
     * 初始化工具库，带有腾讯异常上报工具
     *
     * @param mContext
     * @param appId    Bugly的appid
     */
    public static void init(Context mContext, String fileProviderAuthority, String appId) {
        init(mContext, fileProviderAuthority);
        initError(mContext, appId);
    }

    /**
     * 忽略https的证书校验
     * 避免Glide加载https图片报错：
     * javax.net.ssl.SSLHandshakeException: java.security.cert.CertPathValidatorException: Trust
     * anchor for certification path not found.
     */
    private static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    /**
     * 初始化Bugly
     * 实现异常上报
     */
    public static void initError(Context mContext, String appId) {
        CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(mContext);
        userStrategy.setAppChannel(PackageUtils.getAppName(mContext));
        userStrategy.setAppPackageName(mContext.getPackageName());
        userStrategy.setAppVersion(PackageUtils.getVersionName(mContext));
        CrashReport.initCrashReport(mContext, appId, false, userStrategy);
    }
}
