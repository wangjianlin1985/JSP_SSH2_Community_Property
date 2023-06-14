package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class Parking {
    /*车位id*/
    private int parkingId;
    public int getParkingId() {
        return parkingId;
    }
    public void setParkingId(int parkingId) {
        this.parkingId = parkingId;
    }

    /*车位名称*/
    private String parkingName;
    public String getParkingName() {
        return parkingName;
    }
    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    /*车牌号*/
    private String plateNumber;
    public String getPlateNumber() {
        return plateNumber;
    }
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    /*车主*/
    private Owner ownerObj;
    public Owner getOwnerObj() {
        return ownerObj;
    }
    public void setOwnerObj(Owner ownerObj) {
        this.ownerObj = ownerObj;
    }

    /*操作员*/
    private String opUser;
    public String getOpUser() {
        return opUser;
    }
    public void setOpUser(String opUser) {
        this.opUser = opUser;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonParking=new JSONObject(); 
		jsonParking.accumulate("parkingId", this.getParkingId());
		jsonParking.accumulate("parkingName", this.getParkingName());
		jsonParking.accumulate("plateNumber", this.getPlateNumber());
		jsonParking.accumulate("ownerObj", this.getOwnerObj().getOwnerName());
		jsonParking.accumulate("ownerObjPri", this.getOwnerObj().getOwnerId());
		jsonParking.accumulate("opUser", this.getOpUser());
		return jsonParking;
    }}