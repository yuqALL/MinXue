package com.njit.student.yuqzy.minxue.ui.info;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.njit.student.yuqzy.minxue.R;
import com.njit.student.yuqzy.minxue.model.MinxueItem;
import com.njit.student.yuqzy.minxue.model.MinxueSearchItem;
import com.njit.student.yuqzy.minxue.ui.adapter.MinxueAdapter;
import com.njit.student.yuqzy.minxue.ui.adapter.MinxueSearchAdapter;
import com.njit.student.yuqzy.minxue.ui.base.BaseContentFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class MinxueSearchCategoryFragment extends BaseContentFragment {

    private RecyclerView recyclerView;
    private MinxueSearchAdapter adapter;
    private String baseUrl = "http://zhannei.baidu.com/cse/search?q=test&p=";
    private int currentPage = 0;
    private boolean isLoading = false;
    private String preSearch = "test";
    private String search = "";

    public void setSearch(String search) {
        Log.e("mx", "set search" + search);
        try {
            this.search = URLEncoder.encode(search, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        currentPage = 0;

        if (adapter.getItemCount() > 0) {
            adapter.setNewData(null);
        }

        getMinxueFromServer();
    }

    private Subscription subscription;

    public MinxueSearchCategoryFragment() {

    }

    @Override
    protected int getLayoutId() {
        Log.e("mx", "getLayoutId");
        return R.layout.fragment_gank;
    }

    @Override
    protected void initViews() {
        super.initViews();
        Log.e("mx", "initViews");
        recyclerView = findView(R.id.rv_gank);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MinxueSearchAdapter(getActivity(), null);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    isLoading = true;
                    getMinxueFromServer();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }


    private void getMinxueFromServer() {
        Log.e("mx", "getMinxueFromServer");
        showRefreshing(true);
        Log.e("search", search + " " + preSearch);
        if (search != preSearch) {

            baseUrl = baseUrl.replace(preSearch, search);
            baseUrl.contains("test");

            Log.e("url", "baseurl: " + baseUrl + baseUrl.contains("test"));
            preSearch = search;
        }
        Log.e("currentPage", currentPage + "");
        String url = baseUrl + currentPage + "&s=2964056310544173399";
        Log.e("url", "url: " + url);
        subscription = Observable.just(url).subscribeOn(Schedulers.io()).map(new Func1<String, List<MinxueSearchItem>>() {
            @Override
            public List<MinxueSearchItem> call(String url) {
                List<MinxueSearchItem> mxSearchItems = new ArrayList<>();
                try {

                    Document doc = Jsoup.connect(url).userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)").timeout(10000).get();
                    //Log.e("doc", doc.outerHtml() + "");
                    Element body = doc.select("div#results.content-main").first();
                    //Log.e("result body", body.className() + "");
                    Elements results = body.select("div.result.f.s0");
                    //Log.e("result", results.size() + "");

                    for (Element e : results) {
                        //Log.e("element",e.html());
                        MinxueSearchItem mxItem = new MinxueSearchItem();
                        Element title = e.select("h3.c-title").select("a").first();
                        mxItem.setTitle(title.text());
                        mxItem.setUrl(title.attr("href"));
                        Elements img = e.select("img");
                        if (img.size() > 0) {
                            mxItem.setImg(e.select("img").first().attr("src"));
                        }
                        mxItem.setItemAbstract(e.select("div.c-abstract").first().text());
                        mxItem.setShowUrl(e.select("span.c-showurl").first().text());
                        //Log.e("element content",mxItem.toString());
                        mxSearchItems.add(mxItem);
                    }
//                    for(int i=0;i<20;i++) {
//                        MinxueSearchItem mxItem = new MinxueSearchItem();
//                        mxItem.setTitle("测试");
//                        mxItem.setUrl("www.minxue.net");
//                        mxItem.setImg("http://i8.baidu.com/it/u=3328039291,1881895526&fm=85&s=6118613257E578BA12ECC9C30300E0B3");
//                        mxItem.setItemAbstract("摘要内容");
//                        mxItem.setShowUrl("缩略地址信息");
//                        mxSearchItems.add(mxItem);
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return mxSearchItems;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<MinxueSearchItem>>() {
            @Override
            public void onCompleted() {
                isLoading = false;
            }

            @Override
            public void onError(Throwable e) {
                isLoading = false;
                showRefreshing(false);
            }

            @Override
            public void onNext(List<MinxueSearchItem> mxSearchItems) {
                currentPage++;
                showRefreshing(false);

                if (adapter.getData() == null || adapter.getData().size() == 0) {
                    adapter.setNewData(mxSearchItems);
                } else {
                    adapter.addData(adapter.getData().size(), mxSearchItems);
                }
            }
        });
    }

    @Override
    protected void lazyFetchData() {
        Log.e("mx", "lazyFetchData");
        adapter.setNewData(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

}
