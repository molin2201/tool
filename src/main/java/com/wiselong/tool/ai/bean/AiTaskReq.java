package com.wiselong.tool.ai.bean;

import java.util.List;

public class AiTaskReq {

    private String   batchName;//	String	是	批次名称
    private Integer   batchStatus;//	Int	是	批次状态 (1:启用、2:暂停)
    private String  sceneUid;//	String	是	场景ID 获取所有场景
    private List<TaskData> taskDataList;//	List<TaskData>	是	任务数据
    private Integer  priority;//	Int	是	优先级 (1:一般、4:中级、8:高级)
    private String  startDate;//	Date	是	外呼开始日期 格式：yyyy/MM/dd HH:mm:ss
    private String   endDate;//	Date	是	外呼结束日期 格式：yyyy/MM/dd HH:mm:ss
    private Integer   festivalBan;//	Int	是	节日禁呼 (0:否、1:是)
    private Integer   ruleType;//	Int	是	外呼规则类型(1:自定义、2:使用现有规则)
    private  RuleParam ruleParams ;//	RuleParams	否	外呼规则，外呼规则类型为1时 必传
    private String  ruleUid;//	String	否	现有外呼规则ID， 外呼规则类型为2时 必传 获取现有外呼规则

   public String getBatchName() {
      return batchName;
   }

   public void setBatchName(String batchName) {
      this.batchName = batchName;
   }

   public Integer getBatchStatus() {
      return batchStatus;
   }

   public void setBatchStatus(Integer batchStatus) {
      this.batchStatus = batchStatus;
   }

   public String getSceneUid() {
      return sceneUid;
   }

   public void setSceneUid(String sceneUid) {
      this.sceneUid = sceneUid;
   }

   public List<TaskData> getTaskDataList() {
      return taskDataList;
   }

   public void setTaskDataList(List<TaskData> taskDataList) {
      this.taskDataList = taskDataList;
   }

   public Integer getPriority() {
      return priority;
   }

   public void setPriority(Integer priority) {
      this.priority = priority;
   }

   public String getStartDate() {
      return startDate;
   }

   public void setStartDate(String startDate) {
      this.startDate = startDate;
   }

   public String getEndDate() {
      return endDate;
   }

   public void setEndDate(String endDate) {
      this.endDate = endDate;
   }

   public Integer getFestivalBan() {
      return festivalBan;
   }

   public void setFestivalBan(Integer festivalBan) {
      this.festivalBan = festivalBan;
   }

   public Integer getRuleType() {
      return ruleType;
   }

   public void setRuleType(Integer ruleType) {
      this.ruleType = ruleType;
   }

   public RuleParam getRuleParams() {
      return ruleParams;
   }

   public void setRuleParams(RuleParam ruleParams) {
      this.ruleParams = ruleParams;
   }

   public String getRuleUid() {
      return ruleUid;
   }

   public void setRuleUid(String ruleUid) {
      this.ruleUid = ruleUid;
   }
}
