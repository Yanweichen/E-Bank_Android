package com.bank;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by yanwe on 2016/4/28.
 */
public class TestOther {
    private static final String TAG = "TestOther";

    public static void main(String[] args){
        String url = "http://182.254.246.163//index/Allnotice.action";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("index_pid", "1")
                .addParams("limit", "5")
                .addParams("offset", "0")
                .addParams("tableName", "index_entry_view")
                .addParams("timefmt", "MM/dd")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                    }
                });

//        //创建okHttpClient对象
//        OkHttpClient mOkHttpClient = new OkHttpClient();
//        //创建一个Request
//        final Request request = new Request.Builder()
//                .url("https://github.com/hongyangAndroid")
//                .build();
//        //new call
//        Call call = mOkHttpClient.newCall(request);
//        //请求加入调度
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                System.out.println(response);
//            }
//        });
    }
}
