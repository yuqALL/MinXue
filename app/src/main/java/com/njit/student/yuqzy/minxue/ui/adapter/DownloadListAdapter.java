package com.njit.student.yuqzy.minxue.ui.adapter;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.njit.student.yuqzy.minxue.R;
import com.njit.student.yuqzy.minxue.database.URL;
import com.njit.student.yuqzy.minxue.model.MinxueDetail;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
public class DownloadListAdapter extends ArrayAdapter<MinxueDetail> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    private Context context;

    public DownloadListAdapter(Context context, List<MinxueDetail> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        MinxueDetail item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            convertView = (View) vi.inflate(R.layout.download_item, parent, false);
            // binding view parts to view holder
            viewHolder.imgDownIcon = (ImageView) convertView.findViewById(R.id.img_down_img);
            viewHolder.tvDownTitle = (TextView) convertView.findViewById(R.id.down_title);
            viewHolder.downHeadimg = (ImageView) convertView.findViewById(R.id.down_head_img);
            viewHolder.lv_downurl_content = (ListView) convertView.findViewById(R.id.lv_downurl_content);
            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        // bind data from selected element to view through view holder
        Glide.with(context).load(item.getIcon()).skipMemoryCache(true).fitCenter().into(viewHolder.downHeadimg);
        //Glide.with(context).load(item.getIcon()).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(viewHolder.downHeadimg);
        viewHolder.tvDownTitle.setText(item.getTitle());
        urlListAdapter adapter = new urlListAdapter(context, item);
        viewHolder.lv_downurl_content.setAdapter(adapter);
        GETHeight.setListViewHeightBasedOnChildren(viewHolder.lv_downurl_content);
        return convertView;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    // View lookup cache
    private static class ViewHolder {
        ImageView imgDownIcon;
        TextView tvDownTitle;
        ImageView downHeadimg;
        ListView lv_downurl_content;
    }

}

class urlListAdapter extends BaseAdapter {

    private MinxueDetail detail;
    private Context context;
    ArrayList<String> key;

    public urlListAdapter(Context context, MinxueDetail detail) {
        this.detail = detail;
        this.context = context;
        key = new ArrayList<>();
        for (Map.Entry<String, String> vo : detail.getUrl().entrySet()) {
            if (vo.getValue() != "http://www.minxue.net/channel-name-help.html") {
                key.add(vo.getKey());
            }
        }
    }

    @Override
    public int getCount() {
        return key == null ? 0 : key.size();
    }

    @Override
    public String getItem(int position) {
        return key == null ? "" : key.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater vi = LayoutInflater.from(context);
        final ViewHolder viewHolder;
        final String urlKey = key.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (View) vi.inflate(R.layout.item_downurl_content, parent, false);
            // binding view parts to view holder
            viewHolder.item_url_name = (TextView) convertView.findViewById(R.id.item_url_name);
            viewHolder.item_url_down = (ImageView) convertView.findViewById(R.id.item_url_down);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.item_url_name.setText(urlKey);
        viewHolder.item_url_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText(urlKey, detail.getUrl().get(urlKey));
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                //launchapp(context);

                DownApp downApp=new DownApp(context);
                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new GridHolder(3))
                        .setHeader(R.layout.downapp_header)
                        .setFooter(R.layout.downapp_footer)
                        .setCancelable(true)
                        .setAdapter(downApp)
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {
                                switch (view.getId()) {
                                    case R.id.footer_close_button:
                                        dialog.dismiss();
                                        break;
                                    case R.id.footer_confirm_button:
                                        break;
                                }
                            }
                        })
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                Log.e("DialogPlus", "onItemClick() called with: " + "item = [" +
                                        item + "], position = [" + position + "]");
                                switch (position)
                                {
                                    case 0:
                                        launchapp(context,APP_PACKAGE_NAME1);
                                        break;
                                    case 1:
                                        launchapp(context,APP_PACKAGE_NAME3);
                                        break;
                                    case 2:
                                        launchapp(context,APP_PACKAGE_NAME2);
                                        break;
                                }
                            }
                        })
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setOverlayBackgroundResource(android.R.color.transparent)
                        .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog.show();


            }
        });
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView item_url_name;
        ImageView item_url_down;
    }

    public static final String APP_PACKAGE_NAME1 = "com.xunlei.downloadprovider";//包名
    public static final String APP_PACKAGE_NAME2 = "com.baidu.netdisk";//包名
    public static final String APP_PACKAGE_NAME3 = "com.xunlei.downloadprovider.pad";//包名

    /**
     * 启动App
     *
     * @param context
     */
    public static void launchapp(Context context,String package_name) {
        // 判断是否安装过App，否则去市场下载
        if (isAppInstalled(context, package_name)) {
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(package_name));
        } else {
            goToMarket(context, package_name);
        }
    }

    /**
     * 检测某个应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 去市场下载页面
     */
    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
        }
    }

}

class GETHeight {

    public static void setListViewHeightBasedOnChildren(ListView listview) {

        ListAdapter listAdapter = listview.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listview);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listview.getLayoutParams();
        params.height = totalHeight + (listview.getDividerHeight() * (listAdapter.getCount() - 1)) + 100;
        listview.setLayoutParams(params);
    }
}
