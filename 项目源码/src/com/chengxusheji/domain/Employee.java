package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class Employee {
    /*员工编号*/
    private String employeeNo;
    public String getEmployeeNo() {
        return employeeNo;
    }
    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    /*姓名*/
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /*性别*/
    private String sex;
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    /*职位*/
    private String positionName;
    public String getPositionName() {
        return positionName;
    }
    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    /*联系电话*/
    private String telephone;
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /*地址*/
    private String address;
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    /*员工介绍*/
    private String employeeDesc;
    public String getEmployeeDesc() {
        return employeeDesc;
    }
    public void setEmployeeDesc(String employeeDesc) {
        this.employeeDesc = employeeDesc;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonEmployee=new JSONObject(); 
		jsonEmployee.accumulate("employeeNo", this.getEmployeeNo());
		jsonEmployee.accumulate("name", this.getName());
		jsonEmployee.accumulate("sex", this.getSex());
		jsonEmployee.accumulate("positionName", this.getPositionName());
		jsonEmployee.accumulate("telephone", this.getTelephone());
		jsonEmployee.accumulate("address", this.getAddress());
		jsonEmployee.accumulate("employeeDesc", this.getEmployeeDesc());
		return jsonEmployee;
    }}