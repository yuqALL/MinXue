package com.njit.student.yuqzy.minxue.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.njit.student.yuqzy.minxue.R;

/**
 * Created by Administrator on 2017/1/27.
 */

public class HelpImageAdapter extends BaseAdapter {
    private int[] imageRes = {R.drawable.help_1, R.drawable.help_2, R.drawable.help_3,
            R.drawable.help_4, R.drawable.help_5, R.drawable.help_6, R.drawable.help_7,
            R.drawable.help_8, R.drawable.help_9, R.drawable.help_10, R.drawable.help_11,
            R.drawable.help_12, R.drawable.help_13, R.drawable.help_14, R.drawable.help_15,
            R.drawable.help_16, R.drawable.help_17, R.drawable.help_18, R.drawable.help_19};
    private Context context;

    public HelpImageAdapter(Context context) {

        this.context = context;

    }

    @Override
    public int getCount() {
        return imageRes.length-1;
    }

    @Override
    public Object getItem(int position) {
        return imageRes[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater vi = LayoutInflater.from(context);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (View) vi.inflate(R.layout.help_item, parent, false);
            // binding view parts to view holder
            viewHolder.help_img_item = (ImageView) convertView.findViewById(R.id.help_img_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(imageRes[position]).skipMemoryCache(true).fitCenter().into(viewHolder.help_img_item);
        Log.e("help img","load "+position+" "+imageRes[position]);
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        ImageView help_img_item;
    }
}
