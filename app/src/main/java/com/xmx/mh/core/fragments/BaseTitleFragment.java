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
import com.xmx.mh.common.json.JSONUtil;
import com.xmx.mh.common.net.HttpGetCallback;
import com.xmx.mh.common.net.HttpManager;
import com.xmx.mh.module.article.ArticleActivity;
import com.xmx.mh.module.article.ArticleListAdapter;
import com.xmx.mh.module.article.Article;
import com.xmx.mh.module.net.NetConstants;
import com.xmx.mh.utils.ExceptionUtil;

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

    private void loadData() {
        loadingFlag = true;
        Map<String, String> condition = getCondition();
        HttpManager.getInstance().get(NetConstants.LIST_ARTICLE_URL, condition, new HttpGetCallback() {
            @Override
            public void success(String result) {
                loadingFlag = false;
                result = result.trim();
                if (result.startsWith("{")) {
                    try {
                        Map<String, Object> map = JSONUtil.parseObject(result);
                        String status = (String) map.get(JSONUtil.RESPONSE_STATUS);
                        loadingLayout.setVisibility(View.GONE);
                        dataLayout.setVisibility(View.VISIBLE);
                        switch (status) {
                            case JSONUtil.STATUS_QUERY_SUCCESS:
//                                showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                List<Object> array = (List<Object>) map.get(JSONUtil.RESPONSE_ENTITIES);
                                list = new ArrayList<>();
                                for (Object item : array) {
                                    Article article = new Article((Map<String, Object>) item);
                                    list.add(article);
                                }
                                listAdapter.updateList(list);
                                dataLayout.stopRefresh(true);
                                break;
                            case JSONUtil.STATUS_ERROR:
                                showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                dataLayout.stopRefresh(false);
                                break;
                            case JSONUtil.STATUS_EXECUTE_SUCCESS:
//                                showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                dataLayout.stopRefresh(true);
                                break;
                        }
                    } catch (Exception e) {
                        ExceptionUtil.normalException(e, getContext());
                        showToast("数据异常");
                        dataLayout.stopRefresh(false);
                    }
                } else {
                    showToast("服务器连接失败");
                    dataLayout.stopRefresh(false);
                }
            }

            @Override
            public void fail(Exception e) {
                loadingFlag = false;
                showToast("服务器连接失败");
                dataLayout.stopRefresh(false);
            }
        });
    }

    public abstract Map<String, String> getCondition();
}
