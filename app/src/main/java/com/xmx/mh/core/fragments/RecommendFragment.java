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
    public Map<String, String> getCondition() {
        Map<String, String> condition = new HashMap<>();
        condition.put("type", "推荐");

        return condition;
    }
}
