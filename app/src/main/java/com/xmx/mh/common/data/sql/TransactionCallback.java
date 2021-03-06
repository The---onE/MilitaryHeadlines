package com.xmx.mh.common.data.sql;

/**
 * Created by The_onE on 2016/11/9.
 */

public abstract class TransactionCallback {
    public abstract int operation() throws Exception;

    public abstract void success(int total);

    public abstract void error(Exception e);
}
