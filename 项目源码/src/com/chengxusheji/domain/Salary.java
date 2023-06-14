package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class Salary {
    /*工资id*/
    private int salaryId;
    public int getSalaryId() {
        return salaryId;
    }
    public void setSalaryId(int salaryId) {
        this.salaryId = salaryId;
    }

    /*员工*/
    private Employee employeeObj;
    public Employee getEmployeeObj() {
        return employeeObj;
    }
    public void setEmployeeObj(Employee employeeObj) {
        this.employeeObj = employeeObj;
    }

    /*工资年份*/
    private String year;
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }

    /*工资月份*/
    private String month;
    public String getMonth() {
        return month;
    }
    public void setMonth(String month) {
        this.month = month;
    }

    /*工资金额*/
    private float salaryMoney;
    public float getSalaryMoney() {
        return salaryMoney;
    }
    public void setSalaryMoney(float salaryMoney) {
        this.salaryMoney = salaryMoney;
    }

    /*是否发放*/
    private String fafang;
    public String getFafang() {
        return fafang;
    }
    public void setFafang(String fafang) {
        this.fafang = fafang;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonSalary=new JSONObject(); 
		jsonSalary.accumulate("salaryId", this.getSalaryId());
		jsonSalary.accumulate("employeeObj", this.getEmployeeObj().getName());
		jsonSalary.accumulate("employeeObjPri", this.getEmployeeObj().getEmployeeNo());
		jsonSalary.accumulate("year", this.getYear());
		jsonSalary.accumulate("month", this.getMonth());
		jsonSalary.accumulate("salaryMoney", this.getSalaryMoney());
		jsonSalary.accumulate("fafang", this.getFafang());
		return jsonSalary;
    }}