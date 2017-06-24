package com.xmx.mh.module.article;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.xmx.mh.R;
import com.xmx.mh.base.activity.BaseTempActivity;
import com.xmx.mh.common.net.HttpGetCallback;
import com.xmx.mh.common.net.HttpManager;
import com.xmx.mh.module.net.NetConstants;
import com.xmx.mh.utils.ExceptionUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleActivity extends BaseTempActivity {
    private TextView titleView;
    private TextView timeView;
    private TextView authorView;
    private TextView contentView;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_article);
        titleView = getViewById(R.id.tv_title);
        timeView = getViewById(R.id.tv_time);
        authorView = getViewById(R.id.tv_author);
        contentView = getViewById(R.id.tv_content);

        int id = getIntent().getIntExtra("id", -1);
        Map<String, String> param = new HashMap<>();
        param.put("id", "" + id);

        HttpManager.getInstance().get(NetConstants.ARTICLE_CONTENT_URL, param, new HttpGetCallback() {
            @Override
            public void success(String result) {
                showToast(result);

            }

            @Override
            public void fail(Exception e) {
                ExceptionUtil.normalException(e, ArticleActivity.this);
            }
        });

//        Article article;
//        if (id >= 0) {
//            article = new Article();
//            article.id = id;
//            article.title = "标题" + id;
//            article.author = "作者";
//            article.time = new Date();
//            article.content = "";
//            for (int i = 0; i < 1000; ++i) {
//                article.content += "内容" + id;
//            }
//
//            titleView.setText(article.title);
//            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String timeString = df.format(article.time);
//            timeView.setText(timeString);
//            authorView.setText(article.author);
//            contentView.setText(article.content);
//        }
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
