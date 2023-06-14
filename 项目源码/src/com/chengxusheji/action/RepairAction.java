package com.chengxusheji.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.chengxusheji.utils.ExportExcelUtil;
import com.chengxusheji.dao.RepairDAO;
import com.chengxusheji.domain.Repair;
import com.chengxusheji.dao.OwnerDAO;
import com.chengxusheji.domain.Owner;
@Controller @Scope("prototype")
public class RepairAction extends ActionSupport {

    /*界面层需要查询的属性: 报修用户*/
    private Owner ownerObj;
    public void setOwnerObj(Owner ownerObj) {
        this.ownerObj = ownerObj;
    }
    public Owner getOwnerObj() {
        return this.ownerObj;
    }

    /*界面层需要查询的属性: 报修日期*/
    private String repairDate;
    public void setRepairDate(String repairDate) {
        this.repairDate = repairDate;
    }
    public String getRepairDate() {
        return this.repairDate;
    }

    /*界面层需要查询的属性: 问题描述*/
    private String questionDesc;
    public void setQuestionDesc(String questionDesc) {
        this.questionDesc = questionDesc;
    }
    public String getQuestionDesc() {
        return this.questionDesc;
    }

    /*界面层需要查询的属性: 报修状态*/
    private String repairState;
    public void setRepairState(String repairState) {
        this.repairState = repairState;
    }
    public String getRepairState() {
        return this.repairState;
    }

    /*当前第几页*/
    private int page;
    public void setPage(int page) {
        this.page = page;
    }
    public int getPage() {
        return page;
    }

    /*每页显示多少条数据*/
    private int rows;
    public void setRows(int rows) {
    	this.rows = rows;
    }
    public int getRows() {
    	return this.rows;
    }
    /*一共多少页*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    private int repairId;
    public void setRepairId(int repairId) {
        this.repairId = repairId;
    }
    public int getRepairId() {
        return repairId;
    }

    /*要删除的记录主键集合*/
    private String repairIds;
    public String getRepairIds() {
		return repairIds;
	}
	public void setRepairIds(String repairIds) {
		this.repairIds = repairIds;
	}
    /*当前查询的总记录数目*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*业务层对象*/
    @Resource RepairDAO repairDAO;

    @Resource OwnerDAO ownerDAO;
    /*待操作的Repair对象*/
    private Repair repair;
    public void setRepair(Repair repair) {
        this.repair = repair;
    }
    public Repair getRepair() {
        return this.repair;
    }

    /*ajax添加Repair信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddRepair() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try {
            Owner ownerObj = ownerDAO.GetOwnerByOwnerId(repair.getOwnerObj().getOwnerId());
            repair.setOwnerObj(ownerObj);
            repairDAO.AddRepair(repair);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "Repair添加失败!";
            writeJsonResponse(success, message);
        }
    }

    /*向客户端输出操作成功或失败信息*/
	private void writeJsonResponse(boolean success,String message)
			throws IOException, JSONException {
		HttpServletResponse response=ServletActionContext.getResponse(); 
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject json=new JSONObject();
		json.accumulate("success", success);
		json.accumulate("message", message);
		out.println(json.toString());
		out.flush(); 
		out.close();
	}

    /*查询Repair信息*/
    public void ajaxQueryRepair() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(repairDate == null) repairDate = "";
        if(questionDesc == null) questionDesc = "";
        if(repairState == null) repairState = "";
        if(rows != 0) repairDAO.setRows(rows);
        List<Repair> repairList = repairDAO.QueryRepairInfo(ownerObj, repairDate, questionDesc, repairState, page);
        /*计算总的页数和总的记录数*/
        repairDAO.CalculateTotalPageAndRecordNumber(ownerObj, repairDate, questionDesc, repairState);
        /*获取到总的页码数目*/
        totalPage = repairDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = repairDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Repair repair:repairList) {
			JSONObject jsonRepair = repair.getJsonObject();
			jsonArray.put(jsonRepair);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询Repair信息*/
    public void ajaxQueryAllRepair() throws IOException, JSONException {
        List<Repair> repairList = repairDAO.QueryAllRepairInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(Repair repair:repairList) {
			JSONObject jsonRepair = new JSONObject();
			jsonRepair.accumulate("repairId", repair.getRepairId());
			jsonRepair.accumulate("questionDesc", repair.getQuestionDesc());
			jsonArray.put(jsonRepair);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }

    /*查询要修改的Repair信息*/
    public void ajaxModifyRepairQuery() throws IOException, JSONException {
        /*根据主键repairId获取Repair对象*/
        Repair repair = repairDAO.GetRepairByRepairId(repairId);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonRepair = repair.getJsonObject(); 
		out.println(jsonRepair.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改Repair信息*/
    public void ajaxModifyRepair() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            Owner ownerObj = ownerDAO.GetOwnerByOwnerId(repair.getOwnerObj().getOwnerId());
            repair.setOwnerObj(ownerObj);
            repairDAO.UpdateRepair(repair);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "Repair修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除Repair信息*/
    public void ajaxDeleteRepair() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _repairIds[] = repairIds.split(",");
        	for(String _repairId: _repairIds) {
        		repairDAO.DeleteRepair(Integer.parseInt(_repairId));
        	}
        	success = true;
        	message = _repairIds.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询Repair信息*/
    public String FrontQueryRepair() {
        if(page == 0) page = 1;
        if(repairDate == null) repairDate = "";
        if(questionDesc == null) questionDesc = "";
        if(repairState == null) repairState = "";
        List<Repair> repairList = repairDAO.QueryRepairInfo(ownerObj, repairDate, questionDesc, repairState, page);
        /*计算总的页数和总的记录数*/
        repairDAO.CalculateTotalPageAndRecordNumber(ownerObj, repairDate, questionDesc, repairState);
        /*获取到总的页码数目*/
        totalPage = repairDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = repairDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("repairList",  repairList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("ownerObj", ownerObj);
        List<Owner> ownerList = ownerDAO.QueryAllOwnerInfo();
        ctx.put("ownerList", ownerList);
        ctx.put("repairDate", repairDate);
        ctx.put("questionDesc", questionDesc);
        ctx.put("repairState", repairState);
        return "front_query_view";
    }

    /*查询要修改的Repair信息*/
    public String FrontShowRepairQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键repairId获取Repair对象*/
        Repair repair = repairDAO.GetRepairByRepairId(repairId);

        List<Owner> ownerList = ownerDAO.QueryAllOwnerInfo();
        ctx.put("ownerList", ownerList);
        ctx.put("repair",  repair);
        return "front_show_view";
    }

    /*删除Repair信息*/
    public String DeleteRepair() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            repairDAO.DeleteRepair(repairId);
            ctx.put("message", "Repair删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "Repair删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryRepairOutputToExcel() { 
        if(repairDate == null) repairDate = "";
        if(questionDesc == null) questionDesc = "";
        if(repairState == null) repairState = "";
        List<Repair> repairList = repairDAO.QueryRepairInfo(ownerObj,repairDate,questionDesc,repairState);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Repair信息记录"; 
        String[] headers = { "报修id","报修用户","报修日期","问题描述","报修状态","处理结果"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<repairList.size();i++) {
        	Repair repair = repairList.get(i); 
        	dataset.add(new String[]{repair.getRepairId() + "",repair.getOwnerObj().getOwnerName(),
repair.getRepairDate(),repair.getQuestionDesc(),repair.getRepairState(),repair.getHandleResult()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Repair.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = ServletActionContext.getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
		return null;
    }
}
