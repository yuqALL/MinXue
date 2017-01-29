package com.njit.student.yuqzy.minxue;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.njit.student.yuqzy.minxue.event.ThemeChangedEvent;
import com.njit.student.yuqzy.minxue.ui.info.*;
import com.njit.student.yuqzy.minxue.utils.DoubleClickExit;
import com.njit.student.yuqzy.minxue.utils.SettingsUtil;
import com.njit.student.yuqzy.minxue.utils.SimpleSubscriber;
import com.njit.student.yuqzy.minxue.utils.ThemeUtil;
import com.njit.student.yuqzy.minxue.utils.UpdateUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,ColorChooserDialog.ColorCallback{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager fragmentManager;
    private String currentFragmentTag;
    private Toolbar toolbar;
    private ImageSwitcher nav_img_switcher;
    private Subscription subscription;
    private int[] imgs={
            R.drawable.nav_bac_1,R.drawable.nav_bac_2,R.drawable.nav_bac_3,
            R.drawable.nav_bac_5,R.drawable.nav_bac_7,R.drawable.nav_bac_8,
            R.drawable.nav_bac_9,R.drawable.nav_bac_11,R.drawable.nav_bac_12,
            R.drawable.nav_bac_13,R.drawable.nav_bac_14,R.drawable.nav_bac_15,R.drawable.nav_bac_16,
            R.drawable.nav_bac_17,R.drawable.nav_bac_18,R.drawable.nav_bac_19,R.drawable.nav_bac_20,
            R.drawable.nav_bac_21,R.drawable.nav_bac_22
    };

    //抽屉主菜单
    private static final String FRAGMENT_TAG_MAIN = "minxue main page";//主页
    private static final String FRAGMENT_TAG_SEARCH = "search";//搜索
    private static final String FRAGMENT_TAG_STAR = "star source";//收藏
    private static final String FRAGMENT_TAG_DOWNLOAD = "download";//下载

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AppGlobal.CURRENT_INDEX, currentFragmentTag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        nav_img_switcher=(ImageSwitcher) navigationView.inflateHeaderView(R.layout.nav_header_main).findViewById(R.id.nav_header_imageSwitcher);
        //nav_img.setBackgroundResource(R.drawable.nav_bac_3);
        nav_img_switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(nav_img_switcher.getContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }
        });
        nav_img_switcher.setInAnimation(AnimationUtils.loadAnimation(this,
                R.anim.zoom_in));
        nav_img_switcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                R.anim.zoom_out));
        navigationView.setNavigationItemSelectedListener(this);
        initFragment(savedInstanceState);

        loadData();
        UpdateUtil.check(MainActivity.this, true);
    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            switchContent(FRAGMENT_TAG_MAIN);
        } else {
            currentFragmentTag = savedInstanceState.getString(AppGlobal.CURRENT_INDEX);
            switchContent(currentFragmentTag);
        }
    }

    protected void loadData() {
        nav_img_switcher.post(new Runnable() {
            @Override
            public void run() {
                loadImage();
            }
        });
        subscription = Observable.interval(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        loadImage();
                    }
                });
    }

    private void loadImage() {
        Glide.with(this).load(imgs[new Random().nextInt(19)]).into(new SimpleTarget<GlideDrawable>(nav_img_switcher.getWidth(), nav_img_switcher.getHeight()) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                nav_img_switcher.setImageDrawable(resource);
            }
        });
    }

    public void initDrawer(Toolbar toolbar) {
        if (toolbar != null) {
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };
            mDrawerToggle.syncState();
            mDrawerLayout.addDrawerListener(mDrawerToggle);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (!DoubleClickExit.check()) {
                Snackbar.make(MainActivity.this.getWindow().getDecorView().findViewById(android.R.id.content), "再按一次退出 App!", Snackbar.LENGTH_SHORT).show();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            item.setChecked(true);
            switchContent(FRAGMENT_TAG_MAIN);
        } else if (id == R.id.nav_search) {
            item.setChecked(true);
            switchContent(FRAGMENT_TAG_SEARCH);
        } else if (id == R.id.nav_star) {
            item.setChecked(true);
            switchContent(FRAGMENT_TAG_STAR);
        } else if (id == R.id.nav_download) {
            item.setChecked(true);
            switchContent(FRAGMENT_TAG_DOWNLOAD);
        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(MainActivity.this, MinxueSettingActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, MinxueAboutActivity.class));
        } else if (id == R.id.nav_theme) {
            new ColorChooserDialog.Builder(this, R.string.theme)
                    .customColors(R.array.colors, null)
                    .doneButton(R.string.done)
                    .cancelButton(R.string.cancel)
                    .allowUserColorInput(false)
                    .allowUserColorInputAlpha(false)
                    .show();
        } else if (id == R.id.nav_donate) {
            startActivity(new Intent(MainActivity.this, PayActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchContent(String name) {
        if (currentFragmentTag != null && currentFragmentTag.equals(name))
            return;

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        Fragment currentFragment = fragmentManager.findFragmentByTag(currentFragmentTag);
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }

        Fragment foundFragment = fragmentManager.findFragmentByTag(name);

        if (foundFragment == null) {
            switch (name) {
                case FRAGMENT_TAG_MAIN:
                    foundFragment = new MinxueFragment();
                    break;
                case FRAGMENT_TAG_SEARCH:
                    foundFragment = new MinxueSearchFragment();
                    break;
                case FRAGMENT_TAG_STAR:
                    foundFragment = new MinxueFavoriteFragment();
                    break;
                case FRAGMENT_TAG_DOWNLOAD:
                    foundFragment = new DownloadFragment();
                    break;

            }
        }

        if (foundFragment == null) {

        } else if (foundFragment.isAdded()) {

            if(name!=FRAGMENT_TAG_DOWNLOAD) {
                ft.show(foundFragment);
            }else {
                ft.remove(foundFragment);
                foundFragment = new DownloadFragment();
                ft.add(R.id.content_main, foundFragment, name);
            }
        } else {
            ft.add(R.id.content_main, foundFragment, name);
        }
        ft.commit();
        currentFragmentTag = name;
        invalidateOptionsMenu();
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        if (selectedColor == ThemeUtil.getThemeColor(this, R.attr.colorPrimary))
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(selectedColor);
        }
        if (selectedColor == getResources().getColor(R.color.lapis_blue)) {
            setTheme(R.style.LapisBlueTheme);
            SettingsUtil.setTheme(0);
        } else if (selectedColor == getResources().getColor(R.color.pale_dogwood)) {
            setTheme(R.style.PaleDogwoodTheme);
            SettingsUtil.setTheme(1);
        } else if (selectedColor == getResources().getColor(R.color.greenery)) {
            setTheme(R.style.GreeneryTheme);
            SettingsUtil.setTheme(2);
        } else if (selectedColor == getResources().getColor(R.color.primrose_yellow)) {
            setTheme(R.style.PrimroseYellowTheme);
            SettingsUtil.setTheme(3);
        } else if (selectedColor == getResources().getColor(R.color.flame)) {
            setTheme(R.style.FlameTheme);
            SettingsUtil.setTheme(4);
        } else if (selectedColor == getResources().getColor(R.color.island_paradise)) {
            setTheme(R.style.IslandParadiseTheme);
            SettingsUtil.setTheme(5);
        } else if (selectedColor == getResources().getColor(R.color.kale)) {
            setTheme(R.style.KaleTheme);
            SettingsUtil.setTheme(6);
        } else if (selectedColor == getResources().getColor(R.color.pink_yarrow)) {
            setTheme(R.style.PinkYarrowTheme);
            SettingsUtil.setTheme(7);
        } else if (selectedColor == getResources().getColor(R.color.niagara)) {
            setTheme(R.style.NiagaraTheme);
            SettingsUtil.setTheme(8);
        }
        EventBus.getDefault().post(new ThemeChangedEvent(selectedColor));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onThemeChanged(ThemeChangedEvent event) {
        this.recreate();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        if (!subscription.isUnsubscribed())
            subscription.unsubscribe();
        super.onDestroy();
    }


    private void initTheme() {
        int themeIndex = SettingsUtil.getTheme();
        switch (themeIndex) {
            case 0:
                setTheme(R.style.LapisBlueTheme);
                break;
            case 1:
                setTheme(R.style.PaleDogwoodTheme);
                break;
            case 2:
                setTheme(R.style.GreeneryTheme);
                break;
            case 3:
                setTheme(R.style.PrimroseYellowTheme);
                break;
            case 4:
                setTheme(R.style.FlameTheme);
                break;
            case 5:
                setTheme(R.style.IslandParadiseTheme);
                break;
            case 6:
                setTheme(R.style.KaleTheme);
                break;
            case 7:
                setTheme(R.style.PinkYarrowTheme);
                break;
            case 8:
                setTheme(R.style.NiagaraTheme);
                break;

        }
    }

}
