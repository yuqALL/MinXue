package com.njit.student.yuqzy.minxue.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MinxueDetail implements Serializable {
    private String title = "error";

    public String getTitle() {
        return title;
    }

    public MinxueDetail() {

    }

    public MinxueDetail(String title, String text, String Icon, Map<String, String> url) {
        this.title = title;
        this.text = text;
        this.Icon = Icon;
        this.url = url;
    }

    @Override
    public String toString() {
        return "MinxueDetail{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", Icon='" + Icon + '\'' +
                ", url=" + url +
                '}';
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String text = "获取详情页出错！请重试！";
    private String Icon = "";
    private Map<String, String> url;

    public String getIcon() {
        return Icon;
    }

    public Map<String, String> getUrl() {
        return url;
    }

    public void setUrl(Map<String, String> url) {
        this.url = url;
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getStringURL() {
        StringBuilder string = new StringBuilder();
        for (Map.Entry<String, String> vo : url.entrySet()) {
            if (vo.getValue() != "http://www.minxue.net/channel-name-help.html") {
                string.append(vo.getKey() + " : \n\n 链接地址 ： " + vo.getValue() + "\n\n\n");
            }
        }
        return string.toString();
    }

    public static ArrayList<MinxueDetail> getTestingList() {
        ArrayList<MinxueDetail> items = new ArrayList<>();
        Map<String,String> testUrl=new HashMap<String, String>();
        testUrl.put("url","ed2k:\\uuuuuuu");
        testUrl.put("url2","ed2k:\\uuuuuuu");
        testUrl.put("url3","ed2k:\\uuuuuuu");
        items.add(new MinxueDetail("test1", "hello word", "http://i9.baidu.com/it/u=1688599925,1709166842&fm=85&s=4550CD3A19DF6449485CC1DB0300E0B3",testUrl));
        items.add(new MinxueDetail("test2", "hello word", "http://i9.baidu.com/it/u=1688599925,1709166842&fm=85&s=4550CD3A19DF6449485CC1DB0300E0B3",testUrl));
        items.add(new MinxueDetail("test3", "hello word", "http://i9.baidu.com/it/u=1688599925,1709166842&fm=85&s=4550CD3A19DF6449485CC1DB0300E0B3",testUrl));
        items.add(new MinxueDetail("test4", "hello word", "http://i9.baidu.com/it/u=1688599925,1709166842&fm=85&s=4550CD3A19DF6449485CC1DB0300E0B3",testUrl));
        items.add(new MinxueDetail("test5", "hello word", "http://i9.baidu.com/it/u=1688599925,1709166842&fm=85&s=4550CD3A19DF6449485CC1DB0300E0B3",testUrl));
        items.add(new MinxueDetail("test6", "hello word", "http://i9.baidu.com/it/u=1688599925,1709166842&fm=85&s=4550CD3A19DF6449485CC1DB0300E0B3",testUrl));
        items.add(new MinxueDetail("test7", "hello word", "http://i9.baidu.com/it/u=1688599925,1709166842&fm=85&s=4550CD3A19DF6449485CC1DB0300E0B3",testUrl));
        items.add(new MinxueDetail("test8", "hello word", "http://i9.baidu.com/it/u=1688599925,1709166842&fm=85&s=4550CD3A19DF6449485CC1DB0300E0B3",testUrl));
        items.add(new MinxueDetail("test9", "hello word", "http://i9.baidu.com/it/u=1688599925,1709166842&fm=85&s=4550CD3A19DF6449485CC1DB0300E0B3",testUrl));
        items.add(new MinxueDetail("test10", "hello word", "http://i9.baidu.com/it/u=1688599925,1709166842&fm=85&s=4550CD3A19DF6449485CC1DB0300E0B3",testUrl));
        items.add(new MinxueDetail("test11", "hello word", "http://i9.baidu.com/it/u=1688599925,1709166842&fm=85&s=4550CD3A19DF6449485CC1DB0300E0B3",testUrl));
        return items;
    }
}
