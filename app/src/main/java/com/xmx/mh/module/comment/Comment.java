package com.xmx.mh.module.comment;

import com.xmx.mh.utils.ExceptionUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by The_onE on 2017/6/28.
 */

public class Comment {
    public int id;
    public Date timeCreated; // 发布时间
    public Date timeModified; // 修改时间
    public String content;
    public int articleId;
    public int userId;
    public int likeCount;

    // TODO
    public Comment() {
    }

    public Comment(Map<String, Object> entity) {
        try {
            id = Integer.parseInt((String) entity.get("id"));
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            timeCreated = format.parse((String) entity.get("timeCreated"));
            timeModified = format.parse((String) entity.get("timeModified"));
            content = (String) entity.get("content");
            articleId = Integer.parseInt((String) entity.get("articleId"));
            userId = Integer.parseInt((String) entity.get("userId"));
            likeCount = Integer.parseInt((String) entity.get("likeCount"));
        } catch (Exception e) {
            ExceptionUtil.normalException(e, null);
        }
    }
}
