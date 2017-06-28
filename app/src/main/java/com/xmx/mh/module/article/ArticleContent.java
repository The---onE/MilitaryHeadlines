package com.xmx.mh.module.article;

import com.xmx.mh.utils.ExceptionUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by The_onE on 2017/6/28.
 */

public class ArticleContent {
    public int id;
    public Date timeCreated; // 发布时间
    public Date timeModified; // 修改时间
    public String content; // 文章内容
    public int articleId; // 关联文章

    // TODO
    public ArticleContent() {}

    public ArticleContent(Map<String, Object> entity) {
        try {
            id = Integer.parseInt((String) entity.get("id"));
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            timeCreated = format.parse((String) entity.get("timeCreated"));
            timeModified = format.parse((String) entity.get("timeModified"));
            content = (String) entity.get("content");
            articleId = Integer.parseInt((String) entity.get("article_id"));
        } catch (Exception e) {
            ExceptionUtil.normalException(e, null);
        }
    }
}
