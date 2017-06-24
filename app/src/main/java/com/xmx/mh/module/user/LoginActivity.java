package com.xmx.mh.module.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.xmx.mh.base.activity.BaseTempActivity;
import com.xmx.mh.common.json.JSONUtil;
import com.xmx.mh.common.net.HttpGetCallback;
import com.xmx.mh.common.net.HttpManager;
import com.xmx.mh.common.user.IUserManager;
import com.xmx.mh.common.user.UserConstants;
import com.xmx.mh.common.user.UserManager;
import com.xmx.mh.core.Constants;
import com.xmx.mh.R;
import com.xmx.mh.module.net.NetConstants;
import com.xmx.mh.utils.ExceptionUtil;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseTempActivity {
    private long mExitTime = 0;
    public boolean mustFlag = false;

    private IUserManager userManager = UserManager.getInstance();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void setListener() {
        final Button login = getViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoCompleteTextView usernameView = getViewById(R.id.tv_username);
                String username = usernameView.getText().toString();
                EditText passwordView = getViewById(R.id.tv_password);
                String password = passwordView.getText().toString();
                if (username.equals("")) {
                    showToast(R.string.username_empty);
                } else if (password.equals("")) {
                    showToast(R.string.password_empty);
                } else {
                    login.setEnabled(false);
                    Map<String, String> params = new HashMap<>();
                    params.put("user", username);
                    params.put("pwd", password);

                    HttpManager.getInstance().get(NetConstants.LOGIN_URL, params,
                            new HttpGetCallback() {
                                @Override
                                public void success(String result) {
                                    login.setEnabled(true);
                                    try {
                                        Map<String, Object> map = JSONUtil.parseObject(result);
                                        String status = map.get("status").toString();
                                        String prompt = map.get("prompt").toString();
                                        switch (status) {
                                            case "0":
                                                showToast(prompt);
                                                break;
                                            case "1":
                                                showToast(prompt);
                                                finish();
                                                break;
                                        }
                                    } catch (Exception e) {
                                        ExceptionUtil.normalException(e, LoginActivity.this);
                                    }
                                }

                                @Override
                                public void fail(Exception e) {
                                    ExceptionUtil.normalException(e, LoginActivity.this);
                                    login.setEnabled(true);
                                }
                            });
                }
            }
        });

        Button register = getViewById(R.id.btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class),
                        UserConstants.REGISTER_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onBackPressed() {
        if (mustFlag) {
            if ((System.currentTimeMillis() - mExitTime) > Constants.LONGEST_EXIT_TIME) {
                showToast(R.string.confirm_exit);
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 若从注册页注册成功返回
        if (requestCode == UserConstants.REGISTER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(RESULT_OK, new Intent());
                finish();
            }
        }
    }
}
