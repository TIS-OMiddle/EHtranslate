package com.ljh.dragoon.ehtranslate;


/**
 * Created by dragoon on 2018/7/27.
 */

public class MyTranslateBean {
    private String en;
    private String cn;
    private String url;
    private String type;

    public MyTranslateBean() {
    }

    public MyTranslateBean(String en, String cn, String url, String type) {
        this.en = en;
        this.cn = cn;
        this.url = url;
        this.type = type;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
