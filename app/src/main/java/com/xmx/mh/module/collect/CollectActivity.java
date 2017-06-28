package com.xmx.mh.module.collect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmx.mh.R;
import com.xmx.mh.base.activity.BaseTempActivity;
import com.xmx.mh.module.article.Article;
import com.xmx.mh.module.article.ArticleContent;
import com.xmx.mh.module.comment.Comment;

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
@ContentView(R.layout.activity_article)
public class CollectActivity extends BaseTempActivity {
    @ViewInject(R.id.tv_title)
    private TextView titleView;
    @ViewInject(R.id.tv_time)
    private TextView timeView;
    @ViewInject(R.id.tv_author)
    private TextView authorView;
    @ViewInject(R.id.tv_content)
    private TextView contentView;

    @ViewInject(R.id.tv_comment)
    TextView commentView;
    @ViewInject(R.id.btn_collect)
    Button btnCollect;
    @ViewInject(R.id.btn_comment)
    Button btnComment;

    @ViewInject(R.id.layout_comment)
    private LinearLayout commentLayout;

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    protected void setListener() {
        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 收藏文章
            }
        });

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = commentView.getText().toString();
                if (comment.trim().equals("")) {
                    showToast("评论不能为空");
                } else {
                    // TODO 发表评论
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        int id = getIntent().getIntExtra("id", -1);
        Map<String, String> param = new HashMap<>();
        param.put("id", "" + id);
        // 解析从列表传来的本章
        Article article = (Article) getIntent().getSerializableExtra("article");
        if (article != null && id >= 0) {
            titleView.setText(article.title);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeString = df.format(article.timeCreated);
            timeView.setText(timeString);
            authorView.setText(article.author);
        }
        // TODO 加载文章内容
        ArticleContent content = new ArticleContent();
        content.content = "内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容";
        contentView.setText(content.content);
//        HttpManager.getInstance().get(NetConstants.ARTICLE_CONTENT_URL, param, new HttpGetCallback() {
//            @Override
//            public void success(String result) {
//                showToast(result);
//
//            }
//
//            @Override
//            public void fail(Exception e) {
//                ExceptionUtil.normalException(e, CollectActivity.this);
//            }
//        });

        // TODO 加载点赞信息
        // TODO 加载推荐文章
        // TODO 加载文章评论
        List<Comment> comments = new ArrayList<>();
        Comment c1 = new Comment();
        c1.id = 0;
        c1.timeCreated = new Date();
        c1.content = "aaa";
        c1.userId = 1;
        comments.add(c1);
        Comment c2 = new Comment();
        c2.id = 0;
        c2.timeCreated = new Date();
        c2.content = "bbb";
        c2.userId = 1;
        comments.add(c2);
        for (Comment c : comments) {
            View comment = View.inflate(this, R.layout.item_comment, null);
            TextView commentNicknameView = (TextView) comment.findViewById(R.id.item_nickname);
            TextView commentTimeView = (TextView) comment.findViewById(R.id.item_time);
            TextView commentLikeView = (TextView) comment.findViewById(R.id.item_like);
            TextView commentContentView = (TextView) comment.findViewById(R.id.item_content);

            commentNicknameView.setText("123");
            commentContentView.setText(c.content);
            commentLikeView.setText("❤" + c.likeCount);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeString = df.format(article.timeCreated);
            commentTimeView.setText(timeString);
            commentLayout.addView(comment);
            commentLayout.addView(createSeparatorView());
        }
    }

    private View createSeparatorView() {
        return View.inflate(this, R.layout.item_separator, null);
    }
}
