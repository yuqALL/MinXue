package com.njit.student.yuqzy.minxue.database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2017/1/22.
 */

public class mx extends RealmObject {

    private String article_title;
    private String article_url;
    private String article_img;
    private String article_updateTime;
    private String article_content;
    private RealmList<URL> article_download_urls;

    @Override
    public String toString() {
        return "mx{" +

                ", article_title='" + article_title + '\'' +
                ", article_url='" + article_url + '\'' +
                ", article_img='" + article_img + '\'' +
                ", article_updateTime='" + article_updateTime + '\'' +
                ", article_content='" + article_content + '\'' +
                ", article_download_urls=" + article_download_urls +
                '}';
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getArticle_url() {
        return article_url;
    }

    public void setArticle_url(String article_url) {
        this.article_url = article_url;
    }

    public String getArticle_img() {
        return article_img;
    }

    public void setArticle_img(String article_img) {
        this.article_img = article_img;
    }

    public String getArticle_updateTime() {
        return article_updateTime;
    }

    public void setArticle_updateTime(String article_updateTime) {
        this.article_updateTime = article_updateTime;
    }

    public String getArticle_content() {
        return article_content;
    }

    public void setArticle_content(String article_content) {
        this.article_content = article_content;
    }

    public RealmList<URL> getArticle_download_urls() {
        return article_download_urls;
    }

    public void setArticle_download_urls(RealmList<URL> article_download_urls) {
        this.article_download_urls = article_download_urls;
    }

}
