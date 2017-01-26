package com.njit.student.yuqzy.minxue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.njit.student.yuqzy.minxue.ui.info.MinxueAboutActivity;
import com.njit.student.yuqzy.minxue.utils.SPUtil;
import com.njit.student.yuqzy.minxue.utils.SettingsUtil;

public class PayActivity extends AppCompatActivity implements View.OnLongClickListener{

    private Toolbar toolbar;
    private ImageView WeChatPay,ZhiFuBaoPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(R.layout.activity_pay);
        toolbar=(Toolbar)findViewById(R.id.title);
        setSupportActionBar(toolbar);
        toolbar.setTitle("感谢支持");
        initViews();
    }
    protected void setDisplayHomeAsUpEnabled(boolean enable) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void initViews() {
        setDisplayHomeAsUpEnabled(true);
        WeChatPay=(ImageView)findViewById(R.id.wechat_pay_qrcode);
        ZhiFuBaoPay=(ImageView)findViewById(R.id.zhifubao_pay_qrcode);
        WeChatPay.setOnLongClickListener(this);
        ZhiFuBaoPay.setOnLongClickListener(this);
    }

    private void initTheme(){
        int themeIndex = SettingsUtil.getTheme();
        switch (themeIndex){
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

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId())
        {
            case R.id.wechat_pay_qrcode:
                break;
            case R.id.zhifubao_pay_qrcode:
                break;
        }
        return false;
    }
}
