package com.wiselong.tool.ai.bean;

public class RecallDetail {

  private Integer  timeNo;//	Int	是	次数序号
    private Integer  interval	 ;//Int	是	时间间隔
    private String unit	 ;//String	是	时间单位 （day、hour，minute、second）

    public Integer getTimeNo() {
        return timeNo;
    }

    public void setTimeNo(Integer timeNo) {
        this.timeNo = timeNo;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
