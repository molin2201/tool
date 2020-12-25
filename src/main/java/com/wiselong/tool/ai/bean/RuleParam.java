package com.wiselong.tool.ai.bean;

import java.util.List;

public class RuleParam {

    private String  name;//	String	是	规则名称
    private Integer  type;//	Int	是	规则类型 (1:自定义(和批次绑定的，私有)、 2:手工维护)
    private List<Integer> weekList;//	List< Int >	是	周 (1:周一、2:周二、3:周三、4:周四、5:周五、6:周六、7:周日)
    private String gatewayUid;//	String	是	网关ID 获取网关
    private Integer num	;//Int	是	线路数量 取值范围 (1~ 最大值) 通过网关获取线路数量最大值
    private String  callNumber;//	String	是	主叫号码 通过网关获取选择其一
    private Integer  ringTime;//	Int	是	响铃时长 / 秒
    private String backSoundUid	;//String	否	背景音ID 获取背景音
    private String advanceRecall;//	String	是	提前重呼 (0:否、1:是)
    private Integer recallNum;//	Int	是	重呼次数
    private List<RecallDetail> recallDetailList;//	List< RecallDetail >	是	重呼明细（每次重呼的时间间隔） 集合大小需要与重呼次数一致
    private List<CallTime> callTimeList;//	List< CallTime >	是	呼叫时间范围 可设置多个

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Integer> getWeekList() {
        return weekList;
    }

    public void setWeekList(List<Integer> weekList) {
        this.weekList = weekList;
    }

    public String getGatewayUid() {
        return gatewayUid;
    }

    public void setGatewayUid(String gatewayUid) {
        this.gatewayUid = gatewayUid;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public Integer getRingTime() {
        return ringTime;
    }

    public void setRingTime(Integer ringTime) {
        this.ringTime = ringTime;
    }

    public String getBackSoundUid() {
        return backSoundUid;
    }

    public void setBackSoundUid(String backSoundUid) {
        this.backSoundUid = backSoundUid;
    }

    public String getAdvanceRecall() {
        return advanceRecall;
    }

    public void setAdvanceRecall(String advanceRecall) {
        this.advanceRecall = advanceRecall;
    }

    public Integer getRecallNum() {
        return recallNum;
    }

    public void setRecallNum(Integer recallNum) {
        this.recallNum = recallNum;
    }

    public List<RecallDetail> getRecallDetailList() {
        return recallDetailList;
    }

    public void setRecallDetailList(List<RecallDetail> recallDetailList) {
        this.recallDetailList = recallDetailList;
    }

    public List<CallTime> getCallTimeList() {
        return callTimeList;
    }

    public void setCallTimeList(List<CallTime> callTimeList) {
        this.callTimeList = callTimeList;
    }
}
