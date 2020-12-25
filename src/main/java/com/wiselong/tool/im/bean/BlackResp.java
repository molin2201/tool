package com.wiselong.tool.im.bean;

/**
 * 黑名单查询返回
 */
public class BlackResp {


   private Integer  callId ;//int M 呼叫唯一标识
    private Integer  forbid ;//Integer O 不设置：允许通话继续  设置：禁止通话继续, 可任意填写数值
    private String  transactionId  ;//String O 通话随路数据，将会在话单中记录该数据

    public Integer getCallId() {
        return callId;
    }

    public void setCallId(Integer callId) {
        this.callId = callId;
    }

    public Integer getForbid() {
        return forbid;
    }

    public void setForbid(Integer forbid) {
        this.forbid = forbid;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
