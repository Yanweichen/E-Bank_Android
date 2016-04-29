package com.bank.net;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * Created by yanwe on 2016/4/29.
 */
public class EBankRequest {

    public static void getEnteryList(int page,StringCallback callback){
        String url = "http://182.254.246.163//index/Allnotice.action";
        int limit = 15;
        int offset = page*limit;
        OkHttpUtils
                .get()
                .url(url)
                .addParams("index_pid", "1")
                .addParams("limit",limit+"")
                .addParams("offset", offset+"")
                .addParams("tableName", "index_entry_view")
                .addParams("timefmt", "MM/dd")
                .build()
                .execute(callback);
    }
}
