package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class FeeType {
    /*类别id*/
    private int typeId;
    public int getTypeId() {
        return typeId;
    }
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    /*类别名称*/
    private String typeName;
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonFeeType=new JSONObject(); 
		jsonFeeType.accumulate("typeId", this.getTypeId());
		jsonFeeType.accumulate("typeName", this.getTypeName());
		return jsonFeeType;
    }}