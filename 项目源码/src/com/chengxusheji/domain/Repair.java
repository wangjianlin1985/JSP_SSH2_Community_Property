﻿package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class Repair {
    /*报修id*/
    private int repairId;
    public int getRepairId() {
        return repairId;
    }
    public void setRepairId(int repairId) {
        this.repairId = repairId;
    }

    /*报修用户*/
    private Owner ownerObj;
    public Owner getOwnerObj() {
        return ownerObj;
    }
    public void setOwnerObj(Owner ownerObj) {
        this.ownerObj = ownerObj;
    }

    /*报修日期*/
    private String repairDate;
    public String getRepairDate() {
        return repairDate;
    }
    public void setRepairDate(String repairDate) {
        this.repairDate = repairDate;
    }

    /*问题描述*/
    private String questionDesc;
    public String getQuestionDesc() {
        return questionDesc;
    }
    public void setQuestionDesc(String questionDesc) {
        this.questionDesc = questionDesc;
    }

    /*报修状态*/
    private String repairState;
    public String getRepairState() {
        return repairState;
    }
    public void setRepairState(String repairState) {
        this.repairState = repairState;
    }

    /*处理结果*/
    private String handleResult;
    public String getHandleResult() {
        return handleResult;
    }
    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonRepair=new JSONObject(); 
		jsonRepair.accumulate("repairId", this.getRepairId());
		jsonRepair.accumulate("ownerObj", this.getOwnerObj().getOwnerName());
		jsonRepair.accumulate("ownerObjPri", this.getOwnerObj().getOwnerId());
		jsonRepair.accumulate("repairDate", this.getRepairDate().length()>19?this.getRepairDate().substring(0,19):this.getRepairDate());
		jsonRepair.accumulate("questionDesc", this.getQuestionDesc());
		jsonRepair.accumulate("repairState", this.getRepairState());
		jsonRepair.accumulate("handleResult", this.getHandleResult());
		return jsonRepair;
    }}