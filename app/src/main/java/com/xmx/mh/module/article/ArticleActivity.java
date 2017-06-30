package com.xmx.mh.module.article;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmx.mh.R;
import com.xmx.mh.base.activity.BaseTempActivity;
import com.xmx.mh.common.json.JSONUtil;
import com.xmx.mh.common.net.HttpGetCallback;
import com.xmx.mh.common.net.HttpManager;
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
@ContentView(R.layout.activity_article)
public class ArticleActivity extends BaseTempActivity {
    @ViewInject(R.id.tv_title)
    private TextView titleView;
    @ViewInject(R.id.tv_time)
    private TextView timeView;
    @ViewInject(R.id.tv_author)
    private TextView authorView;
    @ViewInject(R.id.tv_content)
    private TextView contentView;
    @ViewInject(R.id.tv_like)
    private TextView likeView;

    @ViewInject(R.id.tv_comment)
    TextView commentView;
    @ViewInject(R.id.btn_collect)
    Button btnCollect;
    @ViewInject(R.id.btn_comment)
    Button btnComment;

    @ViewInject(R.id.layout_comment)
    private LinearLayout commentLayout;

    Article article;
    int userId;
    boolean loginFlag = false;

    boolean likeFlag = false; // 是否点赞
    boolean collectFlag = false; // 是否收藏

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    protected void setListener() {
        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 切换收藏状态
                if (loginFlag) {
                    Map<String, String> param = new HashMap<>();
                    param.put("user_id", "" + userId);
                    param.put("article_id", "" + article.id);
                    if (!collectFlag) {
                        HttpManager.getInstance().get(NetConstants.COLLECT_ARTICLE_URL, param, new HttpGetCallback() {
                            @Override
                            public void success(String result) {
                                if (result.startsWith("{")) {
                                    try {
                                        Map<String, Object> map = JSONUtil.parseObject(result);
                                        String status = (String) map.get(JSONUtil.RESPONSE_STATUS);
                                        switch (status) {
                                            case JSONUtil.STATUS_EXECUTE_SUCCESS:
                                                showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                                btnCollect.setText("取消收藏");
                                                collectFlag = true;
                                                break;
                                            case JSONUtil.STATUS_ERROR:
                                                showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                                break;
                                        }
                                    } catch (Exception e) {
                                        ExceptionUtil.normalException(e, ArticleActivity.this);
                                        showToast("数据异常");
                                    }
                                } else {
                                    showToast("服务器连接失败");
                                }
                            }

                            @Override
                            public void fail(Exception e) {

                            }
                        });
                    } else {
                        HttpManager.getInstance().get(NetConstants.DISCOLLECT_ARTICLE_URL, param, new HttpGetCallback() {
                            @Override
                            public void success(String result) {
                                if (result.startsWith("{")) {
                                    try {
                                        Map<String, Object> map = JSONUtil.parseObject(result);
                                        String status = (String) map.get(JSONUtil.RESPONSE_STATUS);
                                        switch (status) {
                                            case JSONUtil.STATUS_EXECUTE_SUCCESS:
                                                showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                                btnCollect.setText("收藏");
                                                collectFlag = false;
                                                break;
                                            case JSONUtil.STATUS_ERROR:
                                                showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                                break;
                                        }
                                    } catch (Exception e) {
                                        ExceptionUtil.normalException(e, ArticleActivity.this);
                                        showToast("数据异常");
                                    }
                                } else {
                                    showToast("服务器连接失败");
                                }
                            }

                            @Override
                            public void fail(Exception e) {

                            }
                        });
                    }
                } else {
                    showToast("请先登录");
                }
            }
        });

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 评论
                final String comment = commentView.getText().toString();
                if (comment.trim().equals("")) {
                    showToast("评论不能为空");
                } else {
                    if (loginFlag) {
                        Map<String, String> param = new HashMap<>();
                        param.put("user_id", "" + userId);
                        param.put("article_id", "" + article.id);
                        param.put("content", comment.trim());
                        if (!likeFlag) {
                            HttpManager.getInstance().get(NetConstants.ADD_COMMENT_URL, param, new HttpGetCallback() {
                                @Override
                                public void success(String result) {
                                    if (result.startsWith("{")) {
                                        try {
                                            Map<String, Object> map = JSONUtil.parseObject(result);
                                            String status = (String) map.get(JSONUtil.RESPONSE_STATUS);
                                            switch (status) {
                                                case JSONUtil.STATUS_EXECUTE_SUCCESS:
                                                    View c = View.inflate(ArticleActivity.this, R.layout.item_comment, null);
                                                    TextView commentNicknameView = (TextView) c.findViewById(R.id.item_nickname);
                                                    TextView commentTimeView = (TextView) c.findViewById(R.id.item_time);
                                                    TextView commentContentView = (TextView) c.findViewById(R.id.item_content);

                                                    commentNicknameView.setText("我:");
                                                    commentContentView.setText(comment);
                                                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                    String timeString = df.format(new Date());
                                                    commentTimeView.setText(timeString);
                                                    commentLayout.addView(c);
                                                    commentLayout.addView(createSeparatorView());
                                                    showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                                    commentView.setText("");
                                                    break;
                                                case JSONUtil.STATUS_ERROR:
                                                    showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                                    break;
                                            }
                                        } catch (Exception e) {
                                            ExceptionUtil.normalException(e, ArticleActivity.this);
                                            showToast("数据异常");
                                        }
                                    } else {
                                        showToast("服务器连接失败");
                                    }
                                }

                                @Override
                                public void fail(Exception e) {

                                }
                            });
                        } else {
                            HttpManager.getInstance().get(NetConstants.DISLIKE_ARTICLE_URL, param, new HttpGetCallback() {
                                @Override
                                public void success(String result) {
                                    if (result.startsWith("{")) {
                                        try {
                                            Map<String, Object> map = JSONUtil.parseObject(result);
                                            String status = (String) map.get(JSONUtil.RESPONSE_STATUS);
                                            switch (status) {
                                                case JSONUtil.STATUS_EXECUTE_SUCCESS:
                                                    showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                                    likeView.setText("♡" + (--article.likeCount));
                                                    likeFlag = false;
                                                    break;
                                                case JSONUtil.STATUS_ERROR:
                                                    showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                                    break;
                                            }
                                        } catch (Exception e) {
                                            ExceptionUtil.normalException(e, ArticleActivity.this);
                                            showToast("数据异常");
                                        }
                                    } else {
                                        showToast("服务器连接失败");
                                    }
                                }

                                @Override
                                public void fail(Exception e) {

                                }
                            });
                        }
                    } else {
                        showToast("请先登录");
                    }
                }
            }
        });

        likeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 切换点赞状态
                if (loginFlag) {
                    Map<String, String> param = new HashMap<>();
                    param.put("user_id", "" + userId);
                    param.put("article_id", "" + article.id);
                    if (!likeFlag) {
                        HttpManager.getInstance().get(NetConstants.LIKE_ARTICLE_URL, param, new HttpGetCallback() {
                            @Override
                            public void success(String result) {
                                if (result.startsWith("{")) {
                                    try {
                                        Map<String, Object> map = JSONUtil.parseObject(result);
                                        String status = (String) map.get(JSONUtil.RESPONSE_STATUS);
                                        switch (status) {
                                            case JSONUtil.STATUS_EXECUTE_SUCCESS:
                                                showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                                likeView.setText("♥" + (++article.likeCount));
                                                likeFlag = true;
                                                break;
                                            case JSONUtil.STATUS_ERROR:
                                                showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                                break;
                                        }
                                    } catch (Exception e) {
                                        ExceptionUtil.normalException(e, ArticleActivity.this);
                                        showToast("数据异常");
                                    }
                                } else {
                                    showToast("服务器连接失败");
                                }
                            }

                            @Override
                            public void fail(Exception e) {

                            }
                        });
                    } else {
                        HttpManager.getInstance().get(NetConstants.DISLIKE_ARTICLE_URL, param, new HttpGetCallback() {
                            @Override
                            public void success(String result) {
                                if (result.startsWith("{")) {
                                    try {
                                        Map<String, Object> map = JSONUtil.parseObject(result);
                                        String status = (String) map.get(JSONUtil.RESPONSE_STATUS);
                                        switch (status) {
                                            case JSONUtil.STATUS_EXECUTE_SUCCESS:
                                                showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                                likeView.setText("♡" + (--article.likeCount));
                                                likeFlag = false;
                                                break;
                                            case JSONUtil.STATUS_ERROR:
                                                showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                                break;
                                        }
                                    } catch (Exception e) {
                                        ExceptionUtil.normalException(e, ArticleActivity.this);
                                        showToast("数据异常");
                                    }
                                } else {
                                    showToast("服务器连接失败");
                                }
                            }

                            @Override
                            public void fail(Exception e) {

                            }
                        });
                    }
                } else {
                    showToast("请先登录");
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        UserManager.getInstance().checkLogin(new UserCallback() {
            @Override
            public void success(int id, String nickname) {
                loginFlag = true;
                userId = id;

                // TODO 加载点赞信息
                Map<String, String> likeParam = new HashMap<>();
                likeParam.put("user_id", "" + userId);
                likeParam.put("article_id", "" + article.id);
                HttpManager.getInstance().get(NetConstants.GET_LIKE_INFO_URL, likeParam, new HttpGetCallback() {
                    @Override
                    public void success(String result) {
                        result = result.trim();
                        if (result.startsWith("{")) {
                            try {
                                Map<String, Object> map = JSONUtil.parseObject(result);
                                String status = (String) map.get(JSONUtil.RESPONSE_STATUS);
                                switch (status) {
                                    case JSONUtil.STATUS_EXECUTE_SUCCESS:
                                        likeFlag = Boolean.parseBoolean((String) map.get("isLike"));
                                        if (likeFlag) {
                                            likeView.setText("♥" + article.likeCount);
                                        } else {
                                            likeView.setText("♡" + article.likeCount);
                                        }
                                        break;
                                    case JSONUtil.STATUS_ERROR:
                                        showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                        break;
                                }
                            } catch (Exception e) {
                                ExceptionUtil.normalException(e, ArticleActivity.this);
                                showToast("数据异常");
                            }
                        } else {
                            showToast("服务器连接失败");
                        }
                    }

                    @Override
                    public void fail(Exception e) {
                        ExceptionUtil.normalException(e, ArticleActivity.this);
                    }
                });

                // TODO 获取收藏状态
                Map<String, String> collectParam = new HashMap<>();
                collectParam.put("user_id", "" + userId);
                collectParam.put("article_id", "" + article.id);
                HttpManager.getInstance().get(NetConstants.GET_COLLECT_INFO_URL, collectParam, new HttpGetCallback() {
                    @Override
                    public void success(String result) {
                        result = result.trim();
                        if (result.startsWith("{")) {
                            try {
                                Map<String, Object> map = JSONUtil.parseObject(result);
                                String status = (String) map.get(JSONUtil.RESPONSE_STATUS);
                                switch (status) {
                                    case JSONUtil.STATUS_EXECUTE_SUCCESS:
                                        collectFlag = Boolean.parseBoolean((String) map.get("isCollect"));
                                        if (collectFlag) {
                                            btnCollect.setText("取消收藏");
                                        } else {
                                            btnCollect.setText("收藏");
                                        }
                                        break;
                                    case JSONUtil.STATUS_ERROR:
                                        showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                        break;
                                }
                            } catch (Exception e) {
                                ExceptionUtil.normalException(e, ArticleActivity.this);
                                showToast("数据异常");
                            }
                        } else {
                            showToast("服务器连接失败");
                        }
                    }

                    @Override
                    public void fail(Exception e) {
                        ExceptionUtil.normalException(e, ArticleActivity.this);
                    }
                });
            }

            @Override
            public void fail(String prompt) {
//                showToast(prompt);
            }

            @Override
            public void error(Exception e) {
                ExceptionUtil.normalException(e, ArticleActivity.this);
            }
        });

        int id = getIntent().getIntExtra("id", -1);
        // 解析从列表传来的本章
        article = (Article) getIntent().getSerializableExtra("article");
        if (article != null && id >= 0) {
            titleView.setText(article.title);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeString = df.format(article.timeCreated);
            timeView.setText(timeString);
            authorView.setText(article.author);
        } else {
            finish();
            return;
        }
        // TODO 加载文章内容
        Map<String, String> contentParam = new HashMap<>();
        contentParam.put("id", "" + id);
        HttpManager.getInstance().get(NetConstants.GET_ARTICLE_CONTENT_URL, contentParam,
                new HttpGetCallback() {
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
                                        Map<String, Object> content = (Map<String, Object>) array.get(0);
                                        String c = (String) content.get("content");
                                        contentView.setText(c);
                                        break;
                                    case JSONUtil.STATUS_ERROR:
                                    case JSONUtil.STATUS_EXECUTE_SUCCESS:
                                        showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                        break;
                                }
                            } catch (Exception e) {
                                ExceptionUtil.normalException(e, ArticleActivity.this);
                                showToast("数据异常");
                            }
                        } else {
                            showToast("服务器连接失败");
                        }
                    }

                    @Override
                    public void fail(Exception e) {
                        ExceptionUtil.normalException(e, ArticleActivity.this);
                        showToast("文章内容加载失败");
                    }
                });

        // TODO 加载推荐文章
        // TODO 加载文章评论
        Map<String, String> commentParam = new HashMap<>();
        commentParam.put("id", "" + id);
        HttpManager.getInstance().get(NetConstants.LIST_COMMENT_URL, commentParam, new HttpGetCallback() {
            @Override
            public void success(String result) {
                result = result.trim();
                if (result.startsWith("{")) {
                    try {
                        Map<String, Object> map = JSONUtil.parseObject(result);
                        String status = (String) map.get(JSONUtil.RESPONSE_STATUS);
                        switch (status) {
                            case JSONUtil.STATUS_QUERY_SUCCESS:
//                                showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                List<Object> array = (List<Object>) map.get(JSONUtil.RESPONSE_ENTITIES);
                                List<Comment> comments = new ArrayList<>();
                                for (Object item : array) {
                                    Comment c = new Comment((Map<String, Object>) item);
                                    View comment = View.inflate(ArticleActivity.this, R.layout.item_comment, null);
                                    TextView commentNicknameView = (TextView) comment.findViewById(R.id.item_nickname);
                                    TextView commentTimeView = (TextView) comment.findViewById(R.id.item_time);
                                    TextView commentContentView = (TextView) comment.findViewById(R.id.item_content);

                                    if (userId == c.userId) {
                                        commentNicknameView.setText("我:");
                                    }
                                    commentContentView.setText(c.content);
                                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String timeString = df.format(article.timeCreated);
                                    commentTimeView.setText(timeString);
                                    commentLayout.addView(comment);
                                    commentLayout.addView(createSeparatorView());
                                }
                                break;
                            case JSONUtil.STATUS_ERROR:
                                showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                break;
                            case JSONUtil.STATUS_EXECUTE_SUCCESS:
                                break;
                        }
                    } catch (Exception e) {
                        ExceptionUtil.normalException(e, ArticleActivity.this);
                        showToast("数据异常");
                    }
                } else {
                    showToast("服务器连接失败");
                }
            }

            @Override
            public void fail(Exception e) {

            }
        });

    }

    private View createSeparatorView() {
        return View.inflate(this, R.layout.item_separator, null);
    }

    private void processResponse(String s) {

    }
}
