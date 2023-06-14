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
import com.chengxusheji.dao.OwnerDAO;
import com.chengxusheji.domain.Owner;
import com.chengxusheji.dao.BuildingDAO;
import com.chengxusheji.domain.Building;
@Controller @Scope("prototype")
public class OwnerAction extends ActionSupport {

    /*界面层需要查询的属性: 楼栋名称*/
    private Building buildingObj;
    public void setBuildingObj(Building buildingObj) {
        this.buildingObj = buildingObj;
    }
    public Building getBuildingObj() {
        return this.buildingObj;
    }

    /*界面层需要查询的属性: 房间号*/
    private String roomNo;
    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }
    public String getRoomNo() {
        return this.roomNo;
    }

    /*界面层需要查询的属性: 户主*/
    private String ownerName;
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public String getOwnerName() {
        return this.ownerName;
    }

    /*界面层需要查询的属性: 联系方式*/
    private String telephone;
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getTelephone() {
        return this.telephone;
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

    private int ownerId;
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
    public int getOwnerId() {
        return ownerId;
    }

    /*要删除的记录主键集合*/
    private String ownerIds;
    public String getOwnerIds() {
		return ownerIds;
	}
	public void setOwnerIds(String ownerIds) {
		this.ownerIds = ownerIds;
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
    @Resource OwnerDAO ownerDAO;

    @Resource BuildingDAO buildingDAO;
    /*待操作的Owner对象*/
    private Owner owner;
    public void setOwner(Owner owner) {
        this.owner = owner;
    }
    public Owner getOwner() {
        return this.owner;
    }

    /*ajax添加Owner信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddOwner() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try {
            Building buildingObj = buildingDAO.GetBuildingByBuildingId(owner.getBuildingObj().getBuildingId());
            owner.setBuildingObj(buildingObj);
            ownerDAO.AddOwner(owner);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "Owner添加失败!";
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

    /*查询Owner信息*/
    public void ajaxQueryOwner() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(roomNo == null) roomNo = "";
        if(ownerName == null) ownerName = "";
        if(telephone == null) telephone = "";
        if(rows != 0) ownerDAO.setRows(rows);
        List<Owner> ownerList = ownerDAO.QueryOwnerInfo(buildingObj, roomNo, ownerName, telephone, page);
        /*计算总的页数和总的记录数*/
        ownerDAO.CalculateTotalPageAndRecordNumber(buildingObj, roomNo, ownerName, telephone);
        /*获取到总的页码数目*/
        totalPage = ownerDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = ownerDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Owner owner:ownerList) {
			JSONObject jsonOwner = owner.getJsonObject();
			jsonArray.put(jsonOwner);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询Owner信息*/
    public void ajaxQueryAllOwner() throws IOException, JSONException {
        List<Owner> ownerList = ownerDAO.QueryAllOwnerInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(Owner owner:ownerList) {
			JSONObject jsonOwner = new JSONObject();
			jsonOwner.accumulate("ownerId", owner.getOwnerId());
			jsonOwner.accumulate("ownerName", owner.getBuildingObj().getBuildingName() + "-" + owner.getRoomNo() +  ": " + owner.getOwnerName());
			jsonArray.put(jsonOwner);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }

    /*查询要修改的Owner信息*/
    public void ajaxModifyOwnerQuery() throws IOException, JSONException {
        /*根据主键ownerId获取Owner对象*/
        Owner owner = ownerDAO.GetOwnerByOwnerId(ownerId);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonOwner = owner.getJsonObject(); 
		out.println(jsonOwner.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改Owner信息*/
    public void ajaxModifyOwner() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            Building buildingObj = buildingDAO.GetBuildingByBuildingId(owner.getBuildingObj().getBuildingId());
            owner.setBuildingObj(buildingObj);
            ownerDAO.UpdateOwner(owner);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "Owner修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除Owner信息*/
    public void ajaxDeleteOwner() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _ownerIds[] = ownerIds.split(",");
        	for(String _ownerId: _ownerIds) {
        		ownerDAO.DeleteOwner(Integer.parseInt(_ownerId));
        	}
        	success = true;
        	message = _ownerIds.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询Owner信息*/
    public String FrontQueryOwner() {
        if(page == 0) page = 1;
        if(roomNo == null) roomNo = "";
        if(ownerName == null) ownerName = "";
        if(telephone == null) telephone = "";
        List<Owner> ownerList = ownerDAO.QueryOwnerInfo(buildingObj, roomNo, ownerName, telephone, page);
        /*计算总的页数和总的记录数*/
        ownerDAO.CalculateTotalPageAndRecordNumber(buildingObj, roomNo, ownerName, telephone);
        /*获取到总的页码数目*/
        totalPage = ownerDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = ownerDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("ownerList",  ownerList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("buildingObj", buildingObj);
        List<Building> buildingList = buildingDAO.QueryAllBuildingInfo();
        ctx.put("buildingList", buildingList);
        ctx.put("roomNo", roomNo);
        ctx.put("ownerName", ownerName);
        ctx.put("telephone", telephone);
        return "front_query_view";
    }

    /*查询要修改的Owner信息*/
    public String FrontShowOwnerQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键ownerId获取Owner对象*/
        Owner owner = ownerDAO.GetOwnerByOwnerId(ownerId);

        List<Building> buildingList = buildingDAO.QueryAllBuildingInfo();
        ctx.put("buildingList", buildingList);
        ctx.put("owner",  owner);
        return "front_show_view";
    }

    /*删除Owner信息*/
    public String DeleteOwner() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            ownerDAO.DeleteOwner(ownerId);
            ctx.put("message", "Owner删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "Owner删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryOwnerOutputToExcel() { 
        if(roomNo == null) roomNo = "";
        if(ownerName == null) ownerName = "";
        if(telephone == null) telephone = "";
        List<Owner> ownerList = ownerDAO.QueryOwnerInfo(buildingObj,roomNo,ownerName,telephone);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Owner信息记录"; 
        String[] headers = { "业主id","楼栋名称","房间号","户主","房屋面积","联系方式"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<ownerList.size();i++) {
        	Owner owner = ownerList.get(i); 
        	dataset.add(new String[]{owner.getOwnerId() + "",owner.getBuildingObj().getBuildingName(),
owner.getRoomNo(),owner.getOwnerName(),owner.getArea(),owner.getTelephone()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Owner.xls");//filename是下载的xls的名，建议最好用英文 
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
