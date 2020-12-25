package com.wiselong.tool.im.bean;

public class Msg implements java.io.Serializable {
    private String url;

    private Integer  callId;// int M 呼叫唯一标识
    private String  caller;// String M 通话主叫号码
    private String  callee ;// String M 通话被叫号码
    private String  version ;// String O 消息版本号,可用于第三方系统选择合适的
    private String extraData   ;//String O 额外信息

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getCallId() {
        return callId;
    }

    public void setCallId(Integer callId) {
        this.callId = callId;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCallee() {
        return callee;
    }

    public void setCallee(String callee) {
        this.callee = callee;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }
}
