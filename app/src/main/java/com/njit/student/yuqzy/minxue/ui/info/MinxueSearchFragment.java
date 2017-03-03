package com.njit.student.yuqzy.minxue.ui.info;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import static android.view.View.GONE;

public class MinxueSearchFragment extends BaseFragment {

    private Toolbar mToolbar;

    private MinxueSearchCategoryFragment fragment;

    public MinxueSearchFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_viewpager;
    }

    @Override
    protected void initViews() {
        mToolbar = findView(R.id.toolbar);
        mToolbar.setTitle("敏学搜索");
        ((MainActivity) getActivity()).initDrawer(mToolbar);
    }

    @Override
    protected void lazyFetchData() {


        initTabLayout();

    }

    private EditText searchText;
    private ImageView ivDelete;
    private ViewPager viewPager;

    private void initTabLayout() {
        searchText = findView(R.id.edit_search);
        ivDelete = findView(R.id.search_iv_delete);
        viewPager = findView(R.id.viewPager);
        setupViewPager(viewPager);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
            ivDelete.setVisibility(View.VISIBLE);
        } else {
            ivDelete.setVisibility(GONE);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
});
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    onSearch(searchText.getText().toString());
                }
                return true;
            }
        });
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText("");
                ivDelete.setVisibility(GONE);
            }
        });
    }


    //搜索
    private void onSearch(String text) {
        Toast.makeText(getContext(), "" + text, Toast.LENGTH_SHORT).show();
        fragment.setSearch(text);
    }


    private void setupViewPager(ViewPager viewPager) {
        MinxueSearchFragment.ViewPagerAdapter adapter = new MinxueSearchFragment.ViewPagerAdapter(getChildFragmentManager());
        fragment = new MinxueSearchCategoryFragment();
        adapter.addFrag(fragment);
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

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
