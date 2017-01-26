package com.njit.student.yuqzy.minxue.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.njit.student.yuqzy.minxue.R;
import com.njit.student.yuqzy.minxue.model.MinxueItem;
import com.njit.student.yuqzy.minxue.model.MinxueSearchItem;
import com.njit.student.yuqzy.minxue.ui.info.MinxueDetailsActivity;

import java.util.List;


public class MinxueSearchAdapter extends RecyclerView.Adapter<MinxueSearchAdapter.XianViewHolder> {

    private List<MinxueSearchItem> minxue;
    private Context context;

    private LayoutInflater inflater;

    public MinxueSearchAdapter(Context context, List<MinxueSearchItem> list) {
        this.context = context;
        this.minxue = list;
        this.inflater = LayoutInflater.from(context);
    }

    public void setNewData(List<MinxueSearchItem> data) {
        this.minxue = data;
        notifyDataSetChanged();
    }

    public List<MinxueSearchItem> getData() {
        return minxue;
    }

    public void addData(int position, List<MinxueSearchItem> data) {
        this.minxue.addAll(position, data);
        this.notifyItemRangeInserted(position, data.size());
    }

    @Override
    public XianViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_minxue_search, parent, false);
        XianViewHolder holder = new XianViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final XianViewHolder holder, int position) {
        final MinxueSearchItem item = minxue.get(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //WebUtils.openInternal(context, item.getName(),item.getUrl());
                Intent intent=new Intent(context,MinxueDetailsActivity.class);
                intent.putExtra("come","search");
                intent.putExtra("minxue",item);
                context.startActivity(intent);
            }
        });
        holder.tv_title.setText(String.format("%s. %s", position + 1, item.getTitle()));
        holder.tv_abstract.setText(item.getItemAbstract());
        holder.tv_showurl.setText(item.getShowUrl());
        if(item.getImg().indexOf("http")>=0) {
            Log.e("adapter","http img :"+item.getImg());
            Glide.with(context).load(item.getImg()).skipMemoryCache(true).fitCenter().into(holder.iv);
        }else
        {
            Log.e("adapter","local img");
            holder.iv.setImageResource(R.drawable.logo);
        }
    }

    @Override
    public int getItemCount() {
        return minxue == null ? 0 : minxue.size();
    }


    @Override
    public long getItemId(int position) {
        return minxue.get(position).hashCode();
    }

    class XianViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_showurl;
        TextView tv_abstract;
        ImageView iv;
        View rootView;

        public XianViewHolder(View view) {
            super(view);
            rootView = view;
            iv = (ImageView) view.findViewById(R.id.iv_img);
            tv_title = (TextView) view.findViewById(R.id.tv_mxsearch_title);
            tv_abstract = (TextView) view.findViewById(R.id.tv_mxsearch_abstract);
            tv_showurl = (TextView) view.findViewById(R.id.tv_mxsearch_url);
        }

    }



}
