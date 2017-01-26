package com.njit.student.yuqzy.minxue.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/23.
 */

public class MinxueSearchItem implements Serializable{
    private String title="";
    private String url="";
    private String img="";
    private String itemAbstract="";
    private String showUrl="";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getItemAbstract() {
        return itemAbstract;
    }

    public void setItemAbstract(String itemAbstract) {
        this.itemAbstract = itemAbstract;
    }

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }

    @Override
    public String toString() {
        return "MinxueSearchItem{\n" +
                "title='" + title + "\'\n" +
                "url='" + url + "\'\n" +
                "img='" + img + "\'\n" +
                "itemAbstract='" + itemAbstract + "\'\n" +
                "showUrl='" + showUrl + "\'\n" +
                '}';
    }
}
