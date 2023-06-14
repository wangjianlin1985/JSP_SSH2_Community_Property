package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class Facility {
    /*设施id*/
    private int facilityId;
    public int getFacilityId() {
        return facilityId;
    }
    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    /*设施名称*/
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /*数量*/
    private int count;
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    /*开始使用时间*/
    private String startTime;
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /*设施状态*/
    private String facilityState;
    public String getFacilityState() {
        return facilityState;
    }
    public void setFacilityState(String facilityState) {
        this.facilityState = facilityState;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonFacility=new JSONObject(); 
		jsonFacility.accumulate("facilityId", this.getFacilityId());
		jsonFacility.accumulate("name", this.getName());
		jsonFacility.accumulate("count", this.getCount());
		jsonFacility.accumulate("startTime", this.getStartTime().length()>19?this.getStartTime().substring(0,19):this.getStartTime());
		jsonFacility.accumulate("facilityState", this.getFacilityState());
		return jsonFacility;
    }}