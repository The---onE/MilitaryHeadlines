package com.xmx.mh.module.collect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xmx.mh.R;
import com.xmx.mh.base.activity.BaseTempActivity;
import com.xmx.mh.common.json.JSONUtil;
import com.xmx.mh.common.net.HttpGetCallback;
import com.xmx.mh.common.net.HttpManager;
import com.xmx.mh.module.article.Article;
import com.xmx.mh.module.article.ArticleContent;
import com.xmx.mh.module.article.ArticleListAdapter;
import com.xmx.mh.module.comment.Comment;
import com.xmx.mh.module.net.NetConstants;
import com.xmx.mh.module.user.UserCallback;
import com.xmx.mh.module.user.UserManager;
import com.xmx.mh.utils.ExceptionUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.activity_collect)
public class CollectActivity extends BaseTempActivity {

    @ViewInject(R.id.listArticle)
    ListView listArticle;
    protected ArticleListAdapter listAdapter;

    List<Article> list;

    @Override
    protected void initView(Bundle savedInstanceState) {
        listAdapter = new ArticleListAdapter(CollectActivity.this, new ArrayList<Article>());
        listArticle.setAdapter(listAdapter);
        UserManager.getInstance().checkLogin(new UserCallback() {
            @Override
            public void success(int id, String nickname) {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", "" + id);
                HttpManager.getInstance().get(NetConstants.LIST_COLLECTED_ARTICLE_URL, param, new HttpGetCallback() {
                    @Override
                    public void success(String result) {
                        result = result.trim();
                        if (result.startsWith("{")) {
                            try {
                                Map<String, Object> map = JSONUtil.parseObject(result);
                                String status = (String) map.get(JSONUtil.RESPONSE_STATUS);
                                switch (status) {
                                    case JSONUtil.STATUS_QUERY_SUCCESS:
                                        List<Object> array = (List<Object>) map.get(JSONUtil.RESPONSE_ENTITIES);
                                        list = new ArrayList<>();
                                        for (Object item : array) {
                                            Article article = new Article((Map<String, Object>) item);
                                            list.add(article);
                                        }
                                        listAdapter.updateList(list);
                                        break;
                                    case JSONUtil.STATUS_ERROR:
                                        showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                        break;
                                    case JSONUtil.STATUS_EXECUTE_SUCCESS:
                                        break;
                                }
                            } catch (Exception e) {
                                ExceptionUtil.normalException(e, CollectActivity.this);
                                showToast("数据异常");
                            }
                        } else {
                            showToast("服务器连接失败");
                        }
                    }

                    @Override
                    public void fail(Exception e) {
                        showToast("服务器连接失败");
                    }
                });
            }

            @Override
            public void fail(String prompt) {
                showToast(prompt);
                finish();
            }

            @Override
            public void error(Exception e) {
                ExceptionUtil.normalException(e, CollectActivity.this);
            }
        });
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
