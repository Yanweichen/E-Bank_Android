package com.bank.util;

import android.content.Context;

/**
 * Created by yanwe on 2016/5/6.
 */
public class SharedPreferencesFactory {

    private static SharedPreferencesUtils UserInfosharedPreferencesUtils;
    public final static String USERINFO = "UserInfo";

    public static SharedPreferencesUtils getSharedPreferencesUtils(Context context,String name){
        SharedPreferencesUtils sharedPreferencesUtils = null;
        switch(name){
            case "UserInfo":
                if (UserInfosharedPreferencesUtils==null){
                    UserInfosharedPreferencesUtils = new SharedPreferencesUtils(context,"UserInfo");
                }
                sharedPreferencesUtils = UserInfosharedPreferencesUtils;
                break;
        }
        return sharedPreferencesUtils;
    }
}
