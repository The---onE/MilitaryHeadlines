package com.xmx.mh.core.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.andview.refreshview.XRefreshView;
import com.xmx.mh.R;
import com.xmx.mh.base.fragment.BaseFragment;
import com.xmx.mh.module.article.ArticleActivity;
import com.xmx.mh.module.article.ArticleListAdapter;
import com.xmx.mh.module.article.Article;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseTitleFragment extends BaseFragment {

    protected ListView listTitle;
    protected ArticleListAdapter listAdapter;
    protected List<Article> list;

    protected RelativeLayout loadingLayout;
    protected XRefreshView dataLayout;

    protected boolean loadingFlag = false;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    protected void initView(View view, Bundle savedInstanceState) {
        listTitle = (ListView) view.findViewById(R.id.listTitle);
        loadingLayout = (RelativeLayout) view.findViewById(R.id.layout_loading);
        dataLayout = (XRefreshView) view.findViewById(R.id.layout_data);
    }

    protected void setListener(View view) {
        loadingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!loadingFlag) {
                    loadData();
                }
            }
        });

        listTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                Article article = list.get(i);
                intent.putExtra("id", article.id);
                intent.putExtra("article", article);
                startActivity(intent);
            }
        });

        dataLayout.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onRefresh(boolean isPullDown) {
                loadData();
            }

            @Override
            public void onLoadMore(boolean isSilence) {

            }

            @Override
            public void onRelease(float direction) {

            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {

            }
        });
    }

    @Override
    protected void processLogic(View view, Bundle savedInstanceState) {
        listAdapter = new ArticleListAdapter(getContext(), new ArrayList<Article>());
        listTitle.setAdapter(listAdapter);
        listTitle.addFooterView(((LayoutInflater) getActivity().
                getSystemService(LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_article_list, null, false));
        dataLayout.setAutoRefresh(true);
    }

    public abstract void loadData();
}
