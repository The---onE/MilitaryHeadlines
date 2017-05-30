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

import com.xmx.mh.R;
import com.xmx.mh.base.fragment.BaseFragment;
import com.xmx.mh.base.fragment.xUtilsFragment;
import com.xmx.mh.common.net.HttpGetCallback;
import com.xmx.mh.common.net.HttpManager;
import com.xmx.mh.module.article.ArticleActivity;
import com.xmx.mh.module.article.ArticleListAdapter;
import com.xmx.mh.module.article.ArticleTitle;
import com.xmx.mh.module.net.NetConstants;
import com.xmx.mh.utils.ExceptionUtil;
import com.xmx.mh.utils.JSONUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseTitleFragment extends BaseFragment {

    protected ListView listTitle;
    protected ArticleListAdapter listAdapter;
    protected List<ArticleTitle> list;

    protected RelativeLayout loadingLayout;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    protected void initView(View view, Bundle savedInstanceState) {
        listTitle = (ListView) view.findViewById(R.id.listTitle);
        loadingLayout = (RelativeLayout) view.findViewById(R.id.layout_loading);
    }

    protected void setListener(View view) {
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

    @Override
    protected void processLogic(View view, Bundle savedInstanceState) {
        listAdapter = new ArticleListAdapter(getContext(), new ArrayList<ArticleTitle>());
        listTitle.setAdapter(listAdapter);
        listTitle.addFooterView(((LayoutInflater) getActivity().
                getSystemService(LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_article_title, null, false));
        loadData();
    }

    public abstract void loadData();
}
