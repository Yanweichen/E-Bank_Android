package com.bank.util;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by yanwe on 2016/4/28.
 */
public class MyApplication extends Application {

    public static RequestQueue queues;

    @Override
    public void onCreate() {
        queues = Volley.newRequestQueue(getApplicationContext());
        super.onCreate();
    }
}
