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
import com.chengxusheji.dao.BuildingDAO;
import com.chengxusheji.domain.Building;
@Controller @Scope("prototype")
public class BuildingAction extends ActionSupport {

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

    private int buildingId;
    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }
    public int getBuildingId() {
        return buildingId;
    }

    /*要删除的记录主键集合*/
    private String buildingIds;
    public String getBuildingIds() {
		return buildingIds;
	}
	public void setBuildingIds(String buildingIds) {
		this.buildingIds = buildingIds;
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
    @Resource BuildingDAO buildingDAO;

    /*待操作的Building对象*/
    private Building building;
    public void setBuilding(Building building) {
        this.building = building;
    }
    public Building getBuilding() {
        return this.building;
    }

    /*ajax添加Building信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddBuilding() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try {
            buildingDAO.AddBuilding(building);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "Building添加失败!";
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

    /*查询Building信息*/
    public void ajaxQueryBuilding() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(rows != 0) buildingDAO.setRows(rows);
        List<Building> buildingList = buildingDAO.QueryBuildingInfo(page);
        /*计算总的页数和总的记录数*/
        buildingDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = buildingDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = buildingDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Building building:buildingList) {
			JSONObject jsonBuilding = building.getJsonObject();
			jsonArray.put(jsonBuilding);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询Building信息*/
    public void ajaxQueryAllBuilding() throws IOException, JSONException {
        List<Building> buildingList = buildingDAO.QueryAllBuildingInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(Building building:buildingList) {
			JSONObject jsonBuilding = new JSONObject();
			jsonBuilding.accumulate("buildingId", building.getBuildingId());
			jsonBuilding.accumulate("buildingName", building.getBuildingName());
			jsonArray.put(jsonBuilding);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }

    /*查询要修改的Building信息*/
    public void ajaxModifyBuildingQuery() throws IOException, JSONException {
        /*根据主键buildingId获取Building对象*/
        Building building = buildingDAO.GetBuildingByBuildingId(buildingId);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonBuilding = building.getJsonObject(); 
		out.println(jsonBuilding.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改Building信息*/
    public void ajaxModifyBuilding() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            buildingDAO.UpdateBuilding(building);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "Building修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除Building信息*/
    public void ajaxDeleteBuilding() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _buildingIds[] = buildingIds.split(",");
        	for(String _buildingId: _buildingIds) {
        		buildingDAO.DeleteBuilding(Integer.parseInt(_buildingId));
        	}
        	success = true;
        	message = _buildingIds.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询Building信息*/
    public String FrontQueryBuilding() {
        if(page == 0) page = 1;
        List<Building> buildingList = buildingDAO.QueryBuildingInfo(page);
        /*计算总的页数和总的记录数*/
        buildingDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = buildingDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = buildingDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("buildingList",  buildingList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        return "front_query_view";
    }

    /*查询要修改的Building信息*/
    public String FrontShowBuildingQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键buildingId获取Building对象*/
        Building building = buildingDAO.GetBuildingByBuildingId(buildingId);

        ctx.put("building",  building);
        return "front_show_view";
    }

    /*删除Building信息*/
    public String DeleteBuilding() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            buildingDAO.DeleteBuilding(buildingId);
            ctx.put("message", "Building删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "Building删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryBuildingOutputToExcel() { 
        List<Building> buildingList = buildingDAO.QueryBuildingInfo();
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Building信息记录"; 
        String[] headers = { "楼栋id","楼栋名称","楼栋备注"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<buildingList.size();i++) {
        	Building building = buildingList.get(i); 
        	dataset.add(new String[]{building.getBuildingId() + "",building.getBuildingName(),building.getMemo()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Building.xls");//filename是下载的xls的名，建议最好用英文 
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
