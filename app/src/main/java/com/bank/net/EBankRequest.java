package com.bank.net;

import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import okhttp3.Cookie;

/**
 * Created by yanwe on 2016/4/29.
 */
public class EBankRequest {

    private static final String TAG = "EBankRequest";
    private static String sessionid;
//    private String BaseUrl = "http://182.254.246.163//";
    private static String BaseUrl = "http://10.0.2.2//";

    public static void getEnteryList(int page,StringCallback callback){
        String url = BaseUrl+"index/Allnotice.action";
        int limit = 15;
        int offset = page*limit;
        OkHttpUtils
                .post()
                .url(url)
                .addHeader("Cookie", "hudson_auto_refresh=true")
                .addParams("index_pid", "1")
                .addParams("limit",limit+"")
                .addParams("offset", offset+"")
                .addParams("tableName", "index_entry_view")
                .addParams("timefmt", "MM/dd")
                .build()
                .execute(callback);
//        sessionid = OkHttpUtils.getInstance().getCookieStore().getCookies().get(0).value();
    }

    public static void login(StringCallback callback){
        String url = BaseUrl+"user/login.action";
        RequestCall reqcall = OkHttpUtils
                .post()
                .url(url)
                .addHeader("Cookie", "hudson_auto_refresh=true")
                .addParams("user_account", "505717760@qq.com")
                .addParams("user_password", "111")
                .addParams("user_code", "8meb3")
                .build();
        reqcall.execute(callback);
        Log.d(TAG, "login() called with: " + "callback = [" + OkHttpUtils.getInstance().getCookieStore().getCookies().size() + "]");
        for (Cookie cookie:
                OkHttpUtils.getInstance().getCookieStore().getCookies()) {
            Log.d(TAG, "login() called with: " + "callback = [" + cookie.value() + "]");
        }
    }
}
