package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class Manager {
    /*用户名*/
    private String manageUserName;
    public String getManageUserName() {
        return manageUserName;
    }
    public void setManageUserName(String manageUserName) {
        this.manageUserName = manageUserName;
    }

    /*登录密码*/
    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /*管理员类别*/
    private String manageType;
    public String getManageType() {
        return manageType;
    }
    public void setManageType(String manageType) {
        this.manageType = manageType;
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

    /*联系电话*/
    private String telephone;
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonManager=new JSONObject(); 
		jsonManager.accumulate("manageUserName", this.getManageUserName());
		jsonManager.accumulate("password", this.getPassword());
		jsonManager.accumulate("manageType", this.getManageType());
		jsonManager.accumulate("name", this.getName());
		jsonManager.accumulate("sex", this.getSex());
		jsonManager.accumulate("telephone", this.getTelephone());
		return jsonManager;
    }}