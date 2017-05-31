package com.xmx.mh.core.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

import com.xmx.mh.module.article.ArticleTitle;

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
            ArticleTitle articleTitle = new ArticleTitle();
            articleTitle.id = i;
            articleTitle.title = "标题" + i;
            if (i % 5 == 0) {
                articleTitle.image = "image";
            }
            articleTitle.author = "作者";
            articleTitle.time = new Date();
            list.add(articleTitle);
        }
        listAdapter.updateList(list);
        dataLayout.stopRefresh(true);
        loadingFlag = false;
    }
}
