package com.xmx.mh.core.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

import com.xmx.mh.module.article.Article;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotFragment extends BaseTitleFragment {

    @Override
    public void loadData() {
        loadingFlag = true;
        loadingLayout.setVisibility(View.GONE);
        dataLayout.setVisibility(View.VISIBLE);

        list = new ArrayList<>();
        for (int i = 0; i < 100; ++i) {
            Article article = new Article();
            article.id = i;
            article.title = "标题" + i;
            if (i % 5 == 0) {
                article.image1 = "image";
            }
            article.author = "作者";
            article.timeCreated = new Date();
            list.add(article);
        }
        listAdapter.updateList(list);
        dataLayout.stopRefresh(true);
        loadingFlag = false;
    }
}
