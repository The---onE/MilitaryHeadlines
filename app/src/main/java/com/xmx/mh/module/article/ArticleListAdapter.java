package com.xmx.mh.module.article;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmx.mh.R;
import com.xmx.mh.common.data.BaseEntityAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by The_onE on 2017/5/16.
 */

public class ArticleListAdapter extends BaseEntityAdapter<Article> {
    public ArticleListAdapter(Context context, List<Article> data) {
        super(context, data);
    }

    private static class ViewHolder {
        TextView title;
        ImageView image;
        TextView author;
        TextView time;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_article, null);
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.item_title);
            holder.image = (ImageView) view.findViewById(R.id.item_image);
            holder.author = (TextView) view.findViewById(R.id.item_author);
            holder.time = (TextView) view.findViewById(R.id.item_time);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (i < mData.size()) {
            Article article = mData.get(i);
            holder.title.setText(article.title);
            if (article.image1 != null) {
                holder.image.setVisibility(View.VISIBLE);
            } else {
                holder.image.setVisibility(View.GONE);
            }
            holder.author.setText(article.author);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeString = df.format(article.timeCreated);
            holder.time.setText(timeString);
        } else {
            holder.title.setText("加载失败");
        }

        return view;
    }
}
