package com.njit.student.yuqzy.minxue.ui.info;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.njit.student.yuqzy.minxue.R;
import com.njit.student.yuqzy.minxue.database.URL;
import com.njit.student.yuqzy.minxue.database.mx;
import com.njit.student.yuqzy.minxue.model.MinxueDetail;
import com.njit.student.yuqzy.minxue.model.MinxueItem;
import com.njit.student.yuqzy.minxue.model.MinxueSearchItem;
import com.njit.student.yuqzy.minxue.utils.WebUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.njit.student.yuqzy.minxue.AppGlobal.PRIMARY_KEY;

public class MinxueDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private Subscription subscription;
    private ImageView imgIcon;
    private TextView textDetail, textDownload;
    private Button btnDown, btnStar, btnOriginal;
    private String item_url = "";
    private String item_name = "";
    private String item_updateTime = "";
    private MinxueDetail detail;
    private Realm realm, realmDownload;
    private RealmConfiguration downloadFileConfig;
    private RealmQuery<mx> query;
    private RealmResults<mx> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minxue_details);
        Intent intent = getIntent();
        String come = intent.getStringExtra("come");
        if (come.equals("search")) {
            MinxueSearchItem item = (MinxueSearchItem) intent.getSerializableExtra("minxue");
            item_url = item.getUrl();
            item_name = item.getTitle();
            item_updateTime = item.getShowUrl();
        } else if (come.equals("read")) {
            MinxueItem item = (MinxueItem) intent.getSerializableExtra("minxue");
            item_url = item.getUrl();
            item_name = item.getName();
            item_updateTime = item.getUpdateTime();
        }
        imgIcon = (ImageView) findViewById(R.id.detail_image);
        textDetail = (TextView) findViewById(R.id.detail_text);
        textDownload = (TextView) findViewById(R.id.detail_download);
        btnDown = (Button) findViewById(R.id.btn_download);
        btnStar = (Button) findViewById(R.id.btn_star);
        btnOriginal = (Button) findViewById(R.id.btn_original);

        getDetailsPage(item_url);

        btnOriginal.setOnClickListener(this);
        btnDown.setOnClickListener(this);
        btnStar.setOnClickListener(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(item_name);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();
        query = realm.where(mx.class);
        results = query.equalTo("article_url", item_url).findAll();
        if (results.size() > 0) {
            btnStar.setText("取消收藏");
        }

        downloadFileConfig = new RealmConfiguration.Builder()
                .name("download_file_list")
                .schemaVersion(1)
                .build();
        realmDownload = Realm.getInstance(downloadFileConfig);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getDetailsPage(String item_url) {

        String url = item_url;
        Log.e("detail_url", url);
        subscription = Observable.just(url).subscribeOn(Schedulers.io()).map(new Func1<String, MinxueDetail>() {
            @Override
            public MinxueDetail call(String url) {
                detail = new MinxueDetail();
                Map<String, String> Aurl = new HashMap<String, String>();
                try {
                    Document doc = Jsoup.connect(url).userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)").timeout(10000).get();
                    Element element = doc.select("div#article_body").first();
                    Log.e("get doc element", element.html());
                    Elements urls = element.select("a");
                    if (urls.size() > 0) {
                        for (Element a : urls) {
                            if(a.attr("href").indexOf("channel-name-help.html")<0) {
                                    Aurl.put(a.text(), a.attr("href"));
                            }
                        }
                        detail.setUrl(Aurl);
                    } else {
                        detail.setUrl(null);
                    }
                    Log.e("urls", "" + urls.size());

                    Elements detailContents = element.select("div#iptcomContents.iptcom");
                    detail.setTitle(item_name);
                    String s = "";
                    if (detailContents.size() > 0) {
                        s = br2nl(detailContents.html());
                        Elements img = detailContents.first().select("img");
                        if (img.size() > 0) detail.setIcon(img.first().attr("src"));
                    } else {
                        Elements img = element.select("img");
                        if (img.size() > 0) detail.setIcon(img.first().attr("src"));
                    }
                    if (s == null || s == "") {
                        s = br2nl(element.html());
                    }

                    Log.e("icon", detail.getIcon());
                    if (detail.getIcon() != "") {
                        if (detail.getIcon().indexOf("www.minxue.net") < 0) {
                            String icon = "http://www.minxue.net" + detail.getIcon();
                            detail.setIcon(icon);
                        }
                    }
                    detail.setText(s.replaceAll("内容截图：", "")
                            .replaceAll("简介:", "")
                            .replaceAll("\n\n\n\n\n\n\n\n\n\n", "\n")
                            .replaceAll("\n\n\n\n\n\n\n\n\n", "\n")
                            .replaceAll("\n\n\n\n\n\n\n\n", "\n")
                            .replaceAll("\n\n\n\n\n\n\n", "\n")
                            .replaceAll("\n\n\n\n\n\n", "\n")
                            .replaceAll("\n\n\n\n\n", "\n")
                            .replaceAll("\n\n\n\n", "\n")
                            .replaceAll("\n\n\n", "\n")
                            .replaceAll("\n\n", "\n"));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return detail;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<MinxueDetail>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(MinxueDetail detail) {
                Log.e("detail", detail.toString());
                if (detail.getIcon() != "") {
                    Glide.with(getApplicationContext()).load(detail.getIcon()).skipMemoryCache(true).override(600, 600).fitCenter().into(imgIcon);
                }
                textDetail.setText(detail.getText());
                textDownload.setText(detail.getStringURL());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    public static String br2nl(String html) {
        if (html == null) return html;
        Document document = Jsoup.parse(html);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
        document.select("br").append("\\n");
        document.select("p").prepend("\\n\\n");
        String s = document.html().replaceAll("\\\\n", "\n")
                .replaceAll("&nbsp;", "");
        return Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
                RealmResults<mx> down = realmDownload.where(mx.class).equalTo("article_url", item_url).findAll();
                if (down.size() > 0) {
                    Toast.makeText(this, "链接已载入下载页面！请前往下载页面下载！", Toast.LENGTH_SHORT).show();
                    break;
                }
                realmDownload.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        addMX2Realm(realm, detail);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "链接已载入下载页面！请前往下载页面下载！", Toast.LENGTH_SHORT).show();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(getApplicationContext(), "获取链接出错！", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case R.id.btn_star:
                if (results.size() <= 0) {

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            addMX2Realm(bgRealm, detail);
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            Log.e("Star!", "Realm.Transaction.OnSuccess");
                            btnStar.setText("取消收藏");
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            Log.e("Star!", "Realm.Transaction.OnError");
                            Toast.makeText(getApplicationContext(), "收藏失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            results.deleteAllFromRealm();
                            btnStar.setText("收藏");
                        }
                    });
                }
                break;
            case R.id.btn_original:
                WebUtils.openInternal(this, item_name, item_url);
                break;

        }
    }

    public void addMX2Realm(Realm realm, MinxueDetail detail) {
        URL url;
        RealmList<URL> urls = new RealmList<>();
        mx m = realm.createObject(mx.class);
        int i = 0;
        for (Map.Entry<String, String> vo : detail.getUrl().entrySet()) {
            url = realm.createObject(URL.class);
            url.setId(i++);
            url.setFileName(vo.getKey());
            url.setFileURL(vo.getValue());
            urls.add(url);

        }
        m.setArticle_download_urls(urls);
        m.setArticle_title(item_name);
        m.setArticle_img(detail.getIcon());
        m.setArticle_updateTime(item_updateTime);
        m.setArticle_url(item_url);
        m.setArticle_content(detail.getText());
        Log.e("mx", m.toString());

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
