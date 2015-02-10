package com.lkh.android.base.log;

import android.util.Log;

/**
 * 系统log
 * @author luokanghui
 * @time 17:43
 */
public class LogCommand implements ILog{


    @Override
    public void v(String tag, String msg) {
        Log.v(tag, msg);
    }

    @Override
    public void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    @Override
    public void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    @Override
    public void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    @Override
    public void e(String tag, String msg) {
        Log.e(tag, msg);
    }
}
