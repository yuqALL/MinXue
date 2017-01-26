package com.njit.student.yuqzy.minxue.model;

import java.io.Serializable;


public class MinxueCategory implements Serializable{
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
