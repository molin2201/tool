package com.wiselong.tool.ai.bean;

import java.util.List;

public class TaskData {

   private String  calledNumber;//String	是	被叫号码
   private List<Variable> variableList;//	List< Variable >	否	场景变量 其数量需要与所传场景ID的任务变量数量一致 获取场景变量

   public String getCalledNumber() {
      return calledNumber;
   }

   public void setCalledNumber(String calledNumber) {
      this.calledNumber = calledNumber;
   }

   public List<Variable> getVariableList() {
      return variableList;
   }

   public void setVariableList(List<Variable> variableList) {
      this.variableList = variableList;
   }
}
