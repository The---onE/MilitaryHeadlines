package com.xmx.mh.base.activity;

import android.content.Intent;

import com.xmx.mh.core.activity.MainActivity;

/**
 * Created by The_onE on 2016/10/8.
 * 启动Activity基类，APP启动页，预处理部分数据后跳转至内容页
 */
public abstract class BaseSplashActivity extends BaseActivity {

    /**
     * 跳转至主页
     */
    protected void startMainActivity() {
        Intent mainIntent = new Intent(BaseSplashActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
