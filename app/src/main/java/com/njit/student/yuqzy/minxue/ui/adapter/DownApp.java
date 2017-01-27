package com.njit.student.yuqzy.minxue.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njit.student.yuqzy.minxue.R;

/**
 * Created by Administrator on 2017/1/26.
 */

public class DownApp extends BaseAdapter {
    private LayoutInflater layoutInflater;

    public DownApp(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {

            view = layoutInflater.inflate(R.layout.simple_grid_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.text_view);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Context context = parent.getContext();
        switch (position) {
            case 0:
                viewHolder.textView.setText("迅雷下载");
                viewHolder.imageView.setImageResource(R.drawable.xunlei);
                break;
            case 1:
                viewHolder.textView.setText("迅雷HD(pad)");
                viewHolder.imageView.setImageResource(R.drawable.xunlei);
                break;
            default:
                viewHolder.textView.setText("百度云盘");
                viewHolder.imageView.setImageResource(R.drawable.baiduyun);
                break;
        }

        return view;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
