package com.njit.student.yuqzy.minxue.ui.info;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.njit.student.yuqzy.minxue.MainActivity;
import com.njit.student.yuqzy.minxue.R;
import com.njit.student.yuqzy.minxue.model.MinxueCategory;
import com.njit.student.yuqzy.minxue.ui.base.BaseFragment;
import com.njit.student.yuqzy.minxue.utils.ACache;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MinxueFragment extends BaseFragment {
    private static final String CACHE_MINXUE_CATE = "minxue_cache";

    private Toolbar mToolbar;
    private ACache mCache;


    private Subscription subscription;
    public MinxueFragment() {
        // Required empty public constructor
    }




    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_viewpager;
    }

    @Override
    protected void initViews() {
        mCache = ACache.get(getActivity());
        mToolbar = findView(R.id.toolbar);
        mToolbar.setTitle("敏学网");
        ((MainActivity) getActivity()).initDrawer(mToolbar);

    }

    @Override
    protected void lazyFetchData() {
        List<MinxueCategory> minxueCategories = (List<MinxueCategory>) mCache.getAsObject(CACHE_MINXUE_CATE);
        if (minxueCategories != null && minxueCategories.size() > 0) {
            initTabLayout(minxueCategories);
            return;
        }
        //lm_list.jsp?urltype=tree.TreeTempUrl&amp;wbtreeid=1035
        //http://jwc.njit.edu.cn/lm_list.jsp?urltype=tree.TreeTempUrl&wbtreeid=1052
        String host = "http://www.minxue.net/";
        subscription = Observable.just(host).subscribeOn(Schedulers.io()).map(new Func1<String, List<MinxueCategory>>() {
            @Override
            public List<MinxueCategory> call(String host) {
                List<MinxueCategory> list = new ArrayList<>();
                try {
                    Document doc = Jsoup.connect(host).timeout(10000).get();
                    Element cate = doc.select("div.main_nav").first();
                    Elements links = cate.select("a[href]");
                    for (Element element : links) {
                        MinxueCategory minxue = new MinxueCategory();
                        if(element.text().indexOf("帮助")<0) {
                            minxue.setName(element.text());
                            minxue.setUrl(element.attr("abs:href"));
                            list.add(minxue);
                        }
                    }
                } catch (IOException e) {
                    Snackbar.make(getView(), "获取分类失败!", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            lazyFetchData();
                        }
                    }).setActionTextColor(getActivity().getResources().getColor(R.color.actionColor)).show();
                }

                return list;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<MinxueCategory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(getView(), "获取分类失败!", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lazyFetchData();
                    }
                }).setActionTextColor(getActivity().getResources().getColor(R.color.actionColor)).show();
            }

            @Override
            public void onNext(List<MinxueCategory> minxueCategories) {
                mCache.put(CACHE_MINXUE_CATE, (Serializable) minxueCategories);
                initTabLayout(minxueCategories);
            }
        });
    }

    private void initTabLayout(List<MinxueCategory> minxueCategories) {
        TabLayout tabLayout = findView(R.id.tabs);
        ViewPager viewPager = findView(R.id.viewPager);
        setupViewPager(viewPager, minxueCategories);
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private void setupViewPager(ViewPager viewPager, List<MinxueCategory> minxueCategories) {
        MinxueFragment.ViewPagerAdapter adapter = new MinxueFragment.ViewPagerAdapter(getChildFragmentManager());

        for (MinxueCategory minxue : minxueCategories) {
            Fragment fragment = new MinxueCategoryFragment();
            Bundle data = new Bundle();
            data.putString("url", minxue.getUrl());
            fragment.setArguments(data);
            adapter.addFrag(fragment, minxue.getName());
        }

        viewPager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

}
