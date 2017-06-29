package com.xmx.mh.core.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

import com.xmx.mh.common.json.JSONUtil;
import com.xmx.mh.common.net.HttpGetCallback;
import com.xmx.mh.common.net.HttpManager;
import com.xmx.mh.module.article.Article;
import com.xmx.mh.module.net.NetConstants;
import com.xmx.mh.utils.ExceptionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends BaseTitleFragment {

    @Override
    public void loadData() {
        loadingFlag = true;
        Map<String, String> condition = new HashMap<>();
        condition.put("type", "推荐");
        HttpManager.getInstance().get(NetConstants.TITLE_LIST_URL, condition, new HttpGetCallback() {
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
                                showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
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
                                showToast((String) map.get(JSONUtil.RESPONSE_PROMPT));
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
}
