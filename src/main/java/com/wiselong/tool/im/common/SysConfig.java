package com.wiselong.tool.im.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "sysconf")
public class SysConfig {
    private List<String> phoneList;
    private List<String> urlList;
    private Integer  expireTimeMin;
    private Integer  importSize;
    private String  openAsyn;
    public Integer getImportSize() {
        return importSize;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public String getOpenAsyn() {
        return openAsyn;
    }

    public void setOpenAsyn(String openAsyn) {
        this.openAsyn = openAsyn;
    }

    public void setImportSize(Integer importSize) {
        this.importSize = importSize;
    }

    public Integer getExpireTimeMin() {
        return expireTimeMin;
    }

    public void setExpireTimeMin(Integer expireTimeMin) {
        this.expireTimeMin = expireTimeMin;
    }

    public List<String> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(List<String> phoneList) {
        this.phoneList = phoneList;
    }
}
