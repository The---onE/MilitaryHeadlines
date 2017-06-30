package com.xmx.mh.module.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.xmx.mh.common.json.JSONUtil;
import com.xmx.mh.common.net.HttpGetCallback;
import com.xmx.mh.common.net.HttpManager;
import com.xmx.mh.module.net.NetConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by The_onE on 2017/6/30.
 */

public class UserManager {
    private static UserManager instance;

    public synchronized static UserManager getInstance() {
        if (null == instance) {
            instance = new UserManager();
        }
        return instance;
    }

    Context mContext;
    boolean loginFlag = false;
    int userId;
    String nickname;

    // 在设备中存储登录信息
    private SharedPreferences mSP;

    /**
     * 设置当前上下文，在Application中调用
     */
    public void setContext(Context context) {
        mContext = context;
        mSP = context.getSharedPreferences("User", Context.MODE_PRIVATE);
    }

    public boolean isLogin() {
        return loginFlag;
    }

    public int getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void login(final String username, String password, final UserCallback callback) {
        Map<String, String> param = new HashMap<>();
        param.put("username", username);
        param.put("password", password);
        HttpManager.getInstance().get(NetConstants.LOGIN_URL, param, new HttpGetCallback() {
            public void success(String result) {
                result = result.trim();
                if (result.startsWith("{")) {
                    try {
                        Map<String, Object> map = JSONUtil.parseObject(result);
                        String status = (String) map.get(JSONUtil.RESPONSE_STATUS);
                        switch (status) {
                            case JSONUtil.STATUS_EXECUTE_SUCCESS:
                                loginFlag = true;
                                userId = Integer.parseInt((String) map.get("user_id"));
                                nickname = (String) map.get("nickname");
                                String checksum = (String) map.get("checksum");
                                SharedPreferences.Editor editor = mSP.edit();
                                editor.putBoolean("login", true);
                                editor.putString("username", username);
                                editor.putString("checksum", checksum);
                                editor.apply();
                                callback.success(userId, nickname);
                                break;
                            case JSONUtil.STATUS_ERROR:
                                callback.fail((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                break;
                        }
                    } catch (Exception e) {
                        callback.error(e);
                    }
                } else {
                    callback.fail("服务器连接失败");
                }
            }

            @Override
            public void fail(Exception e) {
                callback.error(e);
            }
        });
    }

    public void register(final String username, String password, final String nickname, final UserCallback callback) {
        Map<String, String> param = new HashMap<>();
        param.put("username", username);
        param.put("password", password);
        param.put("nickname", nickname);
        HttpManager.getInstance().get(NetConstants.REGISTER_URL, param, new HttpGetCallback() {
            public void success(String result) {
                result = result.trim();
                if (result.startsWith("{")) {
                    try {
                        Map<String, Object> map = JSONUtil.parseObject(result);
                        String status = (String) map.get(JSONUtil.RESPONSE_STATUS);
                        switch (status) {
                            case JSONUtil.STATUS_EXECUTE_SUCCESS:
                                loginFlag = true;
                                userId = Integer.parseInt((String) map.get("user_id"));
                                UserManager.this.nickname = nickname;
                                String checksum = (String) map.get("checksum");
                                SharedPreferences.Editor editor = mSP.edit();
                                editor.putBoolean("login", true);
                                editor.putString("username", username);
                                editor.putString("checksum", checksum);
                                editor.apply();
                                callback.success(userId, nickname);
                                break;
                            case JSONUtil.STATUS_ERROR:
                                callback.fail((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                break;
                        }
                    } catch (Exception e) {
                        callback.error(e);
                    }
                } else {
                    callback.fail("服务器连接失败");
                }
            }

            @Override
            public void fail(Exception e) {
                callback.error(e);
            }
        });
    }

    public void autoLogin(final UserCallback callback) {
        if(!mSP.getBoolean("login", false)) {
            callback.fail("请登录");
            return;
        }
        Map<String, String> param = new HashMap<>();
        param.put("username", mSP.getString("username", ""));
        param.put("checksum", mSP.getString("checksum", ""));
        HttpManager.getInstance().get(NetConstants.AUTO_LOGIN_URL, param, new HttpGetCallback() {
            public void success(String result) {
                result = result.trim();
                if (result.startsWith("{")) {
                    try {
                        Map<String, Object> map = JSONUtil.parseObject(result);
                        String status = (String) map.get(JSONUtil.RESPONSE_STATUS);
                        switch (status) {
                            case JSONUtil.STATUS_EXECUTE_SUCCESS:
                                loginFlag = true;
                                userId = Integer.parseInt((String) map.get("user_id"));
                                nickname = (String) map.get("nickname");
                                String checksum = (String) map.get("checksum");
                                SharedPreferences.Editor editor = mSP.edit();
                                editor.putBoolean("login", true);
                                editor.putString("checksum", checksum);
                                editor.apply();
                                callback.success(userId, nickname);
                                break;
                            case JSONUtil.STATUS_ERROR:
                                callback.fail((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                break;
                        }
                    } catch (Exception e) {
                        callback.error(e);
                    }
                } else {
                    callback.fail("服务器连接失败");
                }
            }

            @Override
            public void fail(Exception e) {
                callback.error(e);
            }
        });
    }

    public void checkLogin(final UserCallback callback) {
        if(!mSP.getBoolean("login", false)) {
            callback.fail("请登录");
            return;
        }
        Map<String, String> param = new HashMap<>();
        param.put("username", mSP.getString("username", ""));
        param.put("checksum", mSP.getString("checksum", ""));
        HttpManager.getInstance().get(NetConstants.CHECK_LOGIN_URL, param, new HttpGetCallback() {
            public void success(String result) {
                result = result.trim();
                if (result.startsWith("{")) {
                    try {
                        Map<String, Object> map = JSONUtil.parseObject(result);
                        String status = (String) map.get(JSONUtil.RESPONSE_STATUS);
                        switch (status) {
                            case JSONUtil.STATUS_EXECUTE_SUCCESS:
                                loginFlag = true;
                                userId = Integer.parseInt((String) map.get("user_id"));
                                nickname = (String) map.get("nickname");
                                callback.success(userId, nickname);
                                break;
                            case JSONUtil.STATUS_ERROR:
                                callback.fail((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                break;
                        }
                    } catch (Exception e) {
                        callback.error(e);
                    }
                } else {
                    callback.fail("服务器连接失败");
                }
            }

            @Override
            public void fail(Exception e) {
                callback.error(e);
            }
        });
    }

    public void logout() {
        SharedPreferences.Editor editor = mSP.edit();
        editor.putBoolean("login", false);
        editor.putString("username", "");
        editor.putString("checksum", "");
        editor.apply();
        loginFlag = false;
        userId = 0;
        nickname = null;
    }
}
