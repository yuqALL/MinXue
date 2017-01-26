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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;


public class MinxueFavoriteFragment extends BaseFragment {
    private static final String CACHE_MINXUE_CATE = "minxue_cache";

    private Toolbar mToolbar;
    private ACache mCache;

    public MinxueFavoriteFragment() {
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
        mToolbar.setTitle("收藏");
        ((MainActivity) getActivity()).initDrawer(mToolbar);
    }

    @Override
    protected void lazyFetchData() {
        initTabLayout();
    }


    private void initTabLayout() {
        TabLayout tabLayout = findView(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        ViewPager viewPager = findView(R.id.viewPager);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        Fragment fragment = new MinxueFavoriteCategoryFragment();
        adapter.addFrag(fragment);
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

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

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }
}
