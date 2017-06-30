package com.xmx.mh.module.user;

/**
 * Created by The_onE on 2017/6/30.
 */

public abstract class UserCallback {
    public abstract void success(int id, String nickname);

    public abstract void fail(String prompt);

    public abstract void error(Exception e);
}
