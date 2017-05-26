package com.xmx.mh.utils;

import android.os.Handler;

/**
 * Created by The_onE on 2016/3/23.
 */
public abstract class Timer {
    private long delay = 1000;
    private boolean onceFlag = false;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timer();
            if (!onceFlag) {
                handler.postDelayed(this, delay);
            }
        }
    };

    public abstract void timer();

    public void start(long d, boolean once) {
        delay = d;
        onceFlag = once;
        handler.postDelayed(runnable, delay);
    }

    public void start(long d) {
        start(d, false);
    }

    public void stop() {
        handler.removeCallbacks(runnable);
    }

    public void execute() {
        timer();
    }
}
