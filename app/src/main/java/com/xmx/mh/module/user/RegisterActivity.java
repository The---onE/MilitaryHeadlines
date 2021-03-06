package com.xmx.mh.module.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xmx.mh.R;
import com.xmx.mh.base.activity.BaseTempActivity;
import com.xmx.mh.utils.ExceptionUtil;

public class RegisterActivity extends BaseTempActivity {

    private UserManager userManager = UserManager.getInstance();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void setListener() {
        final Button register = getViewById(R.id.register_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nn = getViewById(R.id.register_nickname);
                final String nickname = nn.getText().toString();
                EditText un = getViewById(R.id.register_username);
                final String username = un.getText().toString();
                EditText pw = getViewById(R.id.register_password);
                final String password = pw.getText().toString();
                EditText pw2 = getViewById(R.id.register_password2);
                String password2 = pw2.getText().toString();

                if (nickname.equals("")) {
                    showToast(R.string.nickname_hint);
                    return;
                }
                if (username.equals("")) {
                    showToast(R.string.username_empty);
                    return;
                }
                if (password.equals("")) {
                    showToast(R.string.password_empty);
                    return;
                }
                if (!password.equals(password2)) {
                    showToast(R.string.password_different);
                    return;
                }

                register.setEnabled(false);

                userManager.register(username, password, nickname, new UserCallback() {
                    @Override
                    public void success(int id, String nickname) {
                        showToast("注册成功");
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void fail(String prompt) {
                        showToast(prompt);
                        register.setEnabled(true);
                    }

                    @Override
                    public void error(Exception e) {
                        ExceptionUtil.normalException(e, RegisterActivity.this);
                        register.setEnabled(true);
                    }
                });
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
