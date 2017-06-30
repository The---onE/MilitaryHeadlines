package com.xmx.mh.module.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.xmx.mh.base.activity.BaseTempActivity;
import com.xmx.mh.core.Constants;
import com.xmx.mh.R;
import com.xmx.mh.utils.ExceptionUtil;

import org.greenrobot.eventbus.EventBus;

public class LoginActivity extends BaseTempActivity {
    private long mExitTime = 0;
    public boolean mustFlag = false;

    int REGISTER_REQUEST_CODE = 100;

    private UserManager userManager = UserManager.getInstance();

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
                    userManager.login(username, password, new UserCallback() {
                        @Override
                        public void success(int id, String nickname) {
                            showToast("登录成功");
                            EventBus.getDefault().post(new LoginEvent());
                            finish();
                        }

                        @Override
                        public void fail(String prompt) {
                            showToast(prompt);
                            login.setEnabled(true);
                        }

                        @Override
                        public void error(Exception e) {
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
                        REGISTER_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 若从注册页注册成功返回
        if (requestCode == REGISTER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(RESULT_OK, new Intent());
                EventBus.getDefault().post(new LoginEvent());
                finish();
            }
        }
    }
}
