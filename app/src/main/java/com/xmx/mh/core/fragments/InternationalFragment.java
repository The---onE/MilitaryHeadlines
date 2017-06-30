package com.xmx.mh.core.fragments;

import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class InternationalFragment extends BaseTitleFragment {

    @Override
    public Map<String, String> getCondition() {
        Map<String, String> condition = new HashMap<>();
        condition.put("type", "国际");

        return condition;
    }
}
