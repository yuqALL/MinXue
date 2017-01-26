package com.njit.student.yuqzy.minxue.ui.info;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.njit.student.yuqzy.minxue.R;
import com.njit.student.yuqzy.minxue.database.mx;
import com.njit.student.yuqzy.minxue.model.MinxueItem;
import com.njit.student.yuqzy.minxue.ui.adapter.MinxueAdapter;
import com.njit.student.yuqzy.minxue.ui.base.BaseContentFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class MinxueFavoriteCategoryFragment extends BaseContentFragment {

    private RecyclerView recyclerView;
    private MinxueAdapter adapter;

    private boolean isLoading = false;
    private Realm realm;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gank;
    }

    @Override
    protected void initViews() {
        super.initViews();
        recyclerView = findView(R.id.rv_gank);

        Realm.init(getContext());
        realm = Realm.getDefaultInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MinxueAdapter(getActivity(), null);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    isLoading = true;
                    getMinxueFromDatabase();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        isLoading = true;
        getMinxueFromDatabase();
    }

    private void getMinxueFromDatabase() {
        showRefreshing(true);

        List<MinxueItem> minxueItems = new ArrayList<>();
        RealmQuery<mx> query=realm.where(mx.class);
        RealmResults<mx> results=query.findAll();
        for (int i = 0; i < results.size(); i++) {
            mx m = results.get(i);
            MinxueItem item = new MinxueItem();
            item.setName(m.getArticle_title());
            item.setUpdateTime(m.getArticle_updateTime());
            item.setUrl(m.getArticle_url());
            minxueItems.add(item);
        }

        isLoading = false;

        showRefreshing(false);

        if (adapter.getData() == null || adapter.getData().size() == 0) {
            adapter.setNewData(minxueItems);
        } else {
            adapter.setNewData(minxueItems);
        }

    }

    @Override
    protected void lazyFetchData() {
        adapter.setNewData(null);
        getMinxueFromDatabase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
