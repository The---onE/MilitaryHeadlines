package com.xmx.mh.module.article;

import com.xmx.mh.utils.ExceptionUtil;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by The_onE on 2017/5/16.
 */

public class Article implements Serializable {
    public int id;
    public Date timeCreated; // 发布时间
    public Date timeModified; // 修改时间
    public String author; // 作者/来源
    public String category; // 类别
    public int shareCount; // 分享数
    public int likeCount; // 点赞数
    public String title; // 标题
    public String image1; // 封面图1
    public String image2; // 封面图2
    public String image3; // 封面图3

    // TODO
    public Article() {}

    public Article(Map<String, Object> entity) {
        try {
            id = Integer.parseInt((String) entity.get("id"));
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            timeCreated = format.parse((String) entity.get("timeCreated"));
            timeModified = format.parse((String) entity.get("timeModified"));
            author = (String) entity.get("author");
            category = (String) entity.get("category");
            shareCount = Integer.parseInt((String) entity.get("shareCount"));
            likeCount = Integer.parseInt((String) entity.get("likeCount"));
            title = (String) entity.get("title");
            image1 = (String) entity.get("image1");
            image2 = (String) entity.get("image2");
            image3 = (String) entity.get("image3");
        } catch (Exception e) {
            ExceptionUtil.normalException(e, null);
        }
    }
}
