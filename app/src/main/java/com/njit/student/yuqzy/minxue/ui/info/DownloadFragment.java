package com.njit.student.yuqzy.minxue.ui.info;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


import com.andexert.expandablelayout.library.ExpandableLayoutListView;
import com.njit.student.yuqzy.minxue.MainActivity;
import com.njit.student.yuqzy.minxue.R;
import com.njit.student.yuqzy.minxue.database.URL;
import com.njit.student.yuqzy.minxue.database.mx;
import com.njit.student.yuqzy.minxue.model.MinxueDetail;

import com.njit.student.yuqzy.minxue.ui.adapter.DownloadListAdapter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DownloadFragment extends Fragment {
    private Toolbar mToolbar;
    private Realm realmDownload;
    private RealmConfiguration downloadFileConfig;
    private RealmResults<mx> results;
    private ExpandableLayoutListView expandableLayoutListView;

    public DownloadFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Realm.init(getContext());
        downloadFileConfig = new RealmConfiguration.Builder()
                .name("download_file_list")
                .schemaVersion(1)
                .build();
        realmDownload = Realm.getInstance(downloadFileConfig);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_download, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("资源下载");
        ((MainActivity) getActivity()).initDrawer(mToolbar);
        expandableLayoutListView = (ExpandableLayoutListView) view.findViewById(R.id.lv_download);
        loadData();
        return view;
    }

    public void loadData() {

        final ArrayList<MinxueDetail> items = new ArrayList<>();
        results = realmDownload.where(mx.class).findAll();
        for (int i = 0; i < results.size(); i++) {
            mx m = results.get(i);
            MinxueDetail item = new MinxueDetail();
            item.setTitle(m.getArticle_title());
            item.setIcon(m.getArticle_img());
            item.setText(m.getArticle_content());
            Map<String, String> url = new HashMap<>();
            for (int j = 0; j < m.getArticle_download_urls().size(); j++) {
                URL item_url = m.getArticle_download_urls().get(j);
                url.put(item_url.getFileName(), item_url.getFileURL());

            }

            item.setUrl(url);
            items.add(item);
        }
        final DownloadListAdapter adapter = new DownloadListAdapter(getContext(), items);
        expandableLayoutListView.setAdapter(adapter);
        expandableLayoutListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final MinxueDetail d = items.get(position);
                DialogPlus dialog = DialogPlus.newDialog(getContext())
                        .setContentHolder(new ViewHolder(R.layout.downapp_content))
                        .setFooter(R.layout.downapp_footer)
                        .setCancelable(true)
                        .setGravity(Gravity.CENTER)
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {
                                switch (view.getId()) {
                                    case R.id.footer_close_button:
                                        dialog.dismiss();
                                        break;
                                    case R.id.footer_confirm_button:
                                        realmDownload.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {

                                                results.deleteFromRealm(position);
                                                adapter.remove(d);
                                            }
                                        });
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        })
                        .setExpanded(false)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .create();
                dialog.show();

                return true;
            }
        });
    }


    public void onButtonPressed() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



}
