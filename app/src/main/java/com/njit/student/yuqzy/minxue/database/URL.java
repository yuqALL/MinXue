package com.njit.student.yuqzy.minxue.database;

import io.realm.RealmObject;


public class URL extends RealmObject {
    private long id;
    private String fileName;
    private String fileURL;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }
}
