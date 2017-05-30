package com.xmx.mh.module.article;

import com.xmx.mh.utils.ExceptionUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by The_onE on 2017/5/16.
 */

public class ArticleTitle {
    public int id;
    public String title; // 标题
    public String image; // 图片
    public String author; // 作者
    public Date time; // 时间

    public ArticleTitle() {

    }

    public ArticleTitle(Map<String, Object> entity) {
        id = Integer.parseInt((String) entity.get("id"));
        title = (String) entity.get("title");
        image = (String) entity.get("image");
        author = (String) entity.get("author");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        try {
            time = format.parse((String) entity.get("time"));
        } catch (ParseException e) {
            ExceptionUtil.normalException(e, null);
        }
    }
}
