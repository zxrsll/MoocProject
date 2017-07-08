package com.imooc.project.com.imooc.project.model;

/**
 * Created by Administrator on 2016/11/16.
 */

public class Record {

    private float mTime;
    private String mPath;

    public Record(float time, String path) {
        this.mTime = time;
        this.mPath = path;
    }


    public Record() {
    }

    public void setPath(String path) {
        this.mPath = path;
    }

    public String getPath() {
        return mPath;
    }

    public void setTime(float time) {
        this.mTime = time;
    }

    public float getTime() {
        return mTime;
    }
}
