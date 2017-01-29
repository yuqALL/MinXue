package com.njit.student.yuqzy.minxue.ui.info;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.njit.student.yuqzy.minxue.R;
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

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class MinxueCategoryFragment extends BaseContentFragment {

    private RecyclerView recyclerView;
    private MinxueAdapter adapter;
    private String baseUrl = "";
    private int currentPage = 1;
    private boolean isLoading = false;

    private Subscription subscription;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gank;
    }

    @Override
    protected void initViews() {
        super.initViews();
        baseUrl = getArguments().getString("url");
        Log.e("url","baseurl: "+baseUrl);
        recyclerView = findView(R.id.rv_gank);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MinxueAdapter(getActivity(), null);
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
        showRefreshing(true);
        String url="";
        if(baseUrl.equals("http://www.minxue.net/"))
        {
            url = baseUrl;
        }else if(baseUrl.equals("http://www.minxue.net/news.html"))
        {
            url ="http://www.minxue.net/index.php?action-news-page-" + currentPage;
        }else if(baseUrl.equals("http://www.minxue.net/english.html"))
        {
            url ="http://www.minxue.net/index.php?action-english-page-" + currentPage;
        }else if(baseUrl.equals("http://www.minxue.net/language.html"))
        {
            url ="http://www.minxue.net/index.php?action-language-page-" + currentPage;
        }else if(baseUrl.equals("http://www.minxue.net/jingjiguanli.html"))
        {
            url ="http://www.minxue.net/index.php?action-jingjiguanli-page-" + currentPage;
        }else if(baseUrl.equals("http://www.minxue.net/edu.html"))
        {
            url ="http://www.minxue.net/index.php?action-edu-page-" + currentPage;
        }else if(baseUrl.equals("http://www.minxue.net/science.html"))
        {
            url ="http://www.minxue.net/index.php?action-science-page-" + currentPage;
        }else if(baseUrl.equals("http://www.minxue.net/renwen.html"))
        {
            url ="http://www.minxue.net/index.php?action-renwen-page-" + currentPage;
        }else if(baseUrl.equals("http://www.minxue.net/art.html"))
        {
            url ="http://www.minxue.net/index.php?action-art-page-" + currentPage;
        }else if(baseUrl.equals("http://www.minxue.net/children.html"))
        {
            url ="http://www.minxue.net/index.php?action-children-page-" + currentPage;
        }else if(baseUrl.equals("http://www.minxue.net/life.html"))
        {
            url ="http://www.minxue.net/index.php?action-life-page-" + currentPage;
        }else if(baseUrl.equals("http://www.minxue.net/yule.html"))
        {
            url ="http://www.minxue.net/index.php?action-yule-page-" + currentPage;
        }else if(baseUrl.equals("http://www.minxue.net/sucai.html"))
        {
            url ="http://www.minxue.net/index.php?action-sucai-page-" + currentPage;
        }else if(baseUrl.equals("http://www.minxue.net/index.php?action-channel-name-help"))
        {
            url =baseUrl;
        }

        subscription = Observable.just(url).subscribeOn(Schedulers.io()).map(new Func1<String, List<MinxueItem>>() {
            @Override
            public List<MinxueItem> call(String url) {
                List<MinxueItem> minxueItems = new ArrayList<>();
                try {

                    Document doc = Jsoup.connect(url).timeout(10000).get();
                    Elements items;
                    Elements times=null;
                    if(url.equals("http://www.minxue.net/")) {
                        Element total = doc.select("div#hot_news").first();
                        items = total.select("div.hot_news_list");
                    }else
                    {
                        Element total=doc.select("ul.global_tx_list4").first();
                        items=total.select("a[href]");
                        times=total.select("span.box_r");
                    }
                    for(int i=0;i<items.size();i++)
                    {
                        Element element=items.get(i);
                        MinxueItem item = new MinxueItem();
                        item.setName(element.text());
                        if(times!=null) {
                            item.setUpdateTime(times.get(i).text());
                        }else{
                            item.setUpdateTime("");
                        }
                        item.setUrl(element.select("a[href]").attr("href"));
                        minxueItems.add(item);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return minxueItems;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<MinxueItem>>() {
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
            public void onNext(List<MinxueItem> minxueItems) {
                currentPage++;
                showRefreshing(false);

                if (adapter.getData() == null || adapter.getData().size() == 0) {
                    adapter.setNewData(minxueItems);
                } else {
                    adapter.addData(adapter.getData().size(), minxueItems);
                }
            }
        });
    }

    @Override
    protected void lazyFetchData() {
        adapter.setNewData(null);
        getMinxueFromServer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

}
