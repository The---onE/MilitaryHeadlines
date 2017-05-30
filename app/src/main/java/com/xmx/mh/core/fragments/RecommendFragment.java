package com.xmx.mh.core.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xmx.mh.R;
import com.xmx.mh.base.fragment.xUtilsFragment;
import com.xmx.mh.common.net.HttpGetCallback;
import com.xmx.mh.common.net.HttpManager;
import com.xmx.mh.core.Constants;
import com.xmx.mh.module.article.ArticleActivity;
import com.xmx.mh.module.article.ArticleListAdapter;
import com.xmx.mh.module.article.ArticleTitle;
import com.xmx.mh.module.net.NetConstants;
import com.xmx.mh.utils.ExceptionUtil;
import com.xmx.mh.utils.JSONUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends BaseTitleFragment {

    @Override
    public void loadData() {
        Map<String, String> condition = new HashMap<>();
        condition.put("type", "1");
        HttpManager.getInstance().get(NetConstants.TITLE_LIST_URL, condition, new HttpGetCallback() {
            @Override
            public void success(String result) {
                result = result.trim();
                if (result.startsWith("{")) {
                    try {
                        Map<String, Object> map = JSONUtil.parseObject(result);
                        String status = (String) map.get(JSONUtil.RESPONSE_STATUS);
                        loadingLayout.setVisibility(View.GONE);
                        listTitle.setVisibility(View.VISIBLE);
                        switch (status) {
                            case JSONUtil.STATUS_QUERY_SUCCESS:
                                List<Object> array = (List<Object>) map.get(JSONUtil.RESPONSE_ENTITIES);
                                list = new ArrayList<>();
                                for (Object item : array) {
                                    ArticleTitle articleTitle = new ArticleTitle((Map<String, Object>) item);
                                    list.add(articleTitle);
                                }
                                listAdapter.updateList(list);
                                break;
                            case JSONUtil.STATUS_ERROR:
                                showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                break;
                            case JSONUtil.STATUS_EXECUTE_SUCCESS:
                                showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
                                break;
                        }
                    } catch (Exception e) {
                        ExceptionUtil.normalException(e, getContext());
                        showToast("数据异常");
                    }
                } else {
                    showToast("服务器连接失败");
                }
            }

            @Override
            public void fail(Exception e) {
                showToast("服务器连接失败");
            }
        });
    }
}
