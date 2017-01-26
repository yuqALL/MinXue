package com.njit.student.yuqzy.minxue.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.njit.student.yuqzy.minxue.R;
import com.njit.student.yuqzy.minxue.model.MinxueItem;
import com.njit.student.yuqzy.minxue.ui.info.MinxueDetailsActivity;

import java.util.List;


public class MinxueAdapter extends RecyclerView.Adapter<MinxueAdapter.XianViewHolder> {

    private List<MinxueItem> minxue;
    private Context context;

    private LayoutInflater inflater;

    public MinxueAdapter(Context context, List<MinxueItem> list) {
        this.context = context;
        this.minxue = list;
        this.inflater = LayoutInflater.from(context);
    }

    public void setNewData(List<MinxueItem> data) {
        this.minxue = data;
        notifyDataSetChanged();
    }

    public List<MinxueItem> getData() {
        return minxue;
    }

    public void addData(int position, List<MinxueItem> data) {
        this.minxue.addAll(position, data);
        this.notifyItemRangeInserted(position, data.size());
    }

    @Override
    public XianViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_minxue, parent, false);
        XianViewHolder holder = new XianViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final XianViewHolder holder, int position) {
        final MinxueItem item = minxue.get(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //WebUtils.openInternal(context, item.getName(),item.getUrl());
                Intent intent=new Intent(context,MinxueDetailsActivity.class);
                intent.putExtra("come","read");
                intent.putExtra("minxue",item);
                context.startActivity(intent);
            }
        });
        holder.tv_name.setText(String.format("%s. %s", position + 1, item.getName()));
        holder.tv_info.setText(item.getUpdateTime() + " • 敏学网");
        holder.iv.setImageResource(chooseIcon(item.getName()));
    }

    @Override
    public int getItemCount() {
        return minxue == null ? 0 : minxue.size();
    }

    public int chooseIcon(String name){
        int n=-1;
        if(name.indexOf("PDF")>=0||name.indexOf("pdf")>=0){
            return R.drawable.ic_pdf;
        }else if(name.indexOf("视频教程")>=0||name.indexOf("rmvb")>=0||name.indexOf("RMVB")>=0||name.indexOf("720P")>=0||name.indexOf("1080P")>=0)
        {
            return R.drawable.ic_files_mp4;
        }else if(name.indexOf("MP4")>=0||name.indexOf("mp4")>=0)
        {
            return R.drawable.ic_mp4;
        }else if(name.indexOf("iso")>=0||name.indexOf("ISO")>=0||name.indexOf("光盘镜像")>=0)
        {
            return R.drawable.ic_iso;
        }else if(name.indexOf("压缩包")>=0||name.indexOf("zip")>=0||name.indexOf("ZIP")>=0)
        {
            return R.drawable.ic_zip;
        }else if(name.indexOf("MP3")>=0||name.indexOf("mp3")>=0)
        {
            return R.drawable.ic_mp3;
        }else if(name.indexOf("电子书")>=0||name.indexOf("EPUB")>=0||name.indexOf("TXT")>=0||name.indexOf("txt")>=0)
        {
            return R.drawable.ic_book;
        }else if(name.indexOf("安装程序")>=0||name.indexOf("安装包")>=0||name.indexOf("EXE")>=0||name.indexOf("exe")>=0||name.indexOf("APK")>=0||name.indexOf("apk")>=0)
        {
            return R.drawable.ic_package;
        }
        return R.drawable.ic_file;
    }

    @Override
    public long getItemId(int position) {
        return minxue.get(position).hashCode();
    }

    class XianViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        TextView tv_info;
        ImageView iv;
        View rootView;

        public XianViewHolder(View view) {
            super(view);
            rootView = view;
            iv = (ImageView) view.findViewById(R.id.iv_download_icon);
            tv_name = (TextView) view.findViewById(R.id.tv_minxue_name);
            tv_info = (TextView) view.findViewById(R.id.tv_minxue_info);
        }

    }



}
