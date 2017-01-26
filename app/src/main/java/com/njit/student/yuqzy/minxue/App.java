package com.njit.student.yuqzy.minxue;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;

/**
 * Created by liyu on 2016/11/2.
 */

public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        if (!BuildConfig.DEBUG) {
            AppExceptionHandler.getInstance().setCrashHanler(this);
        }

    }

    public static Context getContext() {
        return mContext;
    }

}
