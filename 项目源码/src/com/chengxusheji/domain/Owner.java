package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class Owner {
    /*业主id*/
    private int ownerId;
    public int getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    /*登录密码*/
    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /*楼栋名称*/
    private Building buildingObj;
    public Building getBuildingObj() {
        return buildingObj;
    }
    public void setBuildingObj(Building buildingObj) {
        this.buildingObj = buildingObj;
    }

    /*房间号*/
    private String roomNo;
    public String getRoomNo() {
        return roomNo;
    }
    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    /*户主*/
    private String ownerName;
    public String getOwnerName() {
        return ownerName;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /*房屋面积*/
    private String area;
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }

    /*联系方式*/
    private String telephone;
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /*备注信息*/
    private String memo;
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonOwner=new JSONObject(); 
		jsonOwner.accumulate("ownerId", this.getOwnerId());
		jsonOwner.accumulate("password", this.getPassword());
		jsonOwner.accumulate("buildingObj", this.getBuildingObj().getBuildingName());
		jsonOwner.accumulate("buildingObjPri", this.getBuildingObj().getBuildingId());
		jsonOwner.accumulate("roomNo", this.getRoomNo());
		jsonOwner.accumulate("ownerName", this.getOwnerName());
		jsonOwner.accumulate("area", this.getArea());
		jsonOwner.accumulate("telephone", this.getTelephone());
		jsonOwner.accumulate("memo", this.getMemo());
		return jsonOwner;
    }}