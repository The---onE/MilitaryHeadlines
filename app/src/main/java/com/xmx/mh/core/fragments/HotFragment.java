package com.xmx.mh.core.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.xmx.mh.R;
import com.xmx.mh.base.fragment.xUtilsFragment;
import com.xmx.mh.module.article.ArticleActivity;
import com.xmx.mh.module.article.ArticleListAdapter;
import com.xmx.mh.module.article.ArticleTitle;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotFragment extends BaseTitleFragment {

    @Override
    public void loadData() {
        loadingLayout.setVisibility(View.GONE);
        listTitle.setVisibility(View.VISIBLE);

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
        listAdapter = new ArticleListAdapter(getContext(), list);
        listTitle.setAdapter(listAdapter);
        listTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                ArticleTitle articleTitle = list.get(i);
                intent.putExtra("id", articleTitle.id);
                startActivity(intent);
            }
        });
    }
}
