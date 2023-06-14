package com.chengxusheji.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class LeaveWord {
    /*记录id*/
    private int leaveWordId;
    public int getLeaveWordId() {
        return leaveWordId;
    }
    public void setLeaveWordId(int leaveWordId) {
        this.leaveWordId = leaveWordId;
    }

    /*标题*/
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    /*内容*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*发布时间*/
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    /*提交住户*/
    private Owner ownerObj;
    public Owner getOwnerObj() {
        return ownerObj;
    }
    public void setOwnerObj(Owner ownerObj) {
        this.ownerObj = ownerObj;
    }

    /*回复内容*/
    private String replyContent;
    public String getReplyContent() {
        return replyContent;
    }
    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    /*回复时间*/
    private String replyTime;
    public String getReplyTime() {
        return replyTime;
    }
    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    /*回复人*/
    private String opUser;
    public String getOpUser() {
        return opUser;
    }
    public void setOpUser(String opUser) {
        this.opUser = opUser;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonLeaveWord=new JSONObject(); 
		jsonLeaveWord.accumulate("leaveWordId", this.getLeaveWordId());
		jsonLeaveWord.accumulate("title", this.getTitle());
		jsonLeaveWord.accumulate("content", this.getContent());
		jsonLeaveWord.accumulate("addTime", this.getAddTime());
		jsonLeaveWord.accumulate("ownerObj", this.getOwnerObj().getOwnerName());
		jsonLeaveWord.accumulate("ownerObjPri", this.getOwnerObj().getOwnerId());
		jsonLeaveWord.accumulate("replyContent", this.getReplyContent());
		jsonLeaveWord.accumulate("replyTime", this.getReplyTime());
		jsonLeaveWord.accumulate("opUser", this.getOpUser());
		return jsonLeaveWord;
    }}