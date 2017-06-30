package com.xmx.mh.core.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

import com.xmx.mh.module.article.Article;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotFragment extends BaseTitleFragment {

    @Override
    public Map<String, String> getCondition() {
        Map<String, String> condition = new HashMap<>();
        condition.put("type", "热点");

        return condition;
    }
}
