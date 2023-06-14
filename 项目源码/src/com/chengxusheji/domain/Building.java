package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class Building {
    /*楼栋id*/
    private int buildingId;
    public int getBuildingId() {
        return buildingId;
    }
    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    /*楼栋名称*/
    private String buildingName;
    public String getBuildingName() {
        return buildingName;
    }
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    /*楼栋备注*/
    private String memo;
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonBuilding=new JSONObject(); 
		jsonBuilding.accumulate("buildingId", this.getBuildingId());
		jsonBuilding.accumulate("buildingName", this.getBuildingName());
		jsonBuilding.accumulate("memo", this.getMemo());
		return jsonBuilding;
    }}