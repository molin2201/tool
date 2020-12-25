package com.wiselong.tool.ai.bean;

public class CallTime {

    private String startTime	;//String	是	开始时间 格式：HH:mm
    private String  endTime	;//String	是	结束时间 格式：HH:mm

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
