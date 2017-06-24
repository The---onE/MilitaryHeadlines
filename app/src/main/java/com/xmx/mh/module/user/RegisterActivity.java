package com.xmx.mh.module.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xmx.mh.R;
import com.xmx.mh.base.activity.BaseTempActivity;
import com.xmx.mh.common.json.JSONUtil;
import com.xmx.mh.common.net.HttpGetCallback;
import com.xmx.mh.common.net.HttpManager;
import com.xmx.mh.common.user.IUserManager;
import com.xmx.mh.common.user.UserManager;
import com.xmx.mh.module.net.NetConstants;
import com.xmx.mh.utils.ExceptionUtil;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseTempActivity {

    private IUserManager userManager = UserManager.getInstance();

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

                Map<String, String> params = new HashMap<>();
                params.put("user", username);
                params.put("pwd", password);

                HttpManager.getInstance().get(NetConstants.REGISTER_URL, params,
                        new HttpGetCallback() {
                            @Override
                            public void success(String result) {
                                register.setEnabled(true);
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
                                    ExceptionUtil.normalException(e, RegisterActivity.this);
                                }
                            }

                            @Override
                            public void fail(Exception e) {
                                ExceptionUtil.normalException(e, RegisterActivity.this);
                                register.setEnabled(true);
                            }
                        });
            }
        });

//        getViewById(R.id.register_cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
