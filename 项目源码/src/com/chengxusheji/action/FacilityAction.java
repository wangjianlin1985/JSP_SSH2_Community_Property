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
import com.chengxusheji.dao.FacilityDAO;
import com.chengxusheji.domain.Facility;
@Controller @Scope("prototype")
public class FacilityAction extends ActionSupport {

    /*界面层需要查询的属性: 设施名称*/
    private String name;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    /*界面层需要查询的属性: 开始使用时间*/
    private String startTime;
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getStartTime() {
        return this.startTime;
    }

    /*界面层需要查询的属性: 设施状态*/
    private String facilityState;
    public void setFacilityState(String facilityState) {
        this.facilityState = facilityState;
    }
    public String getFacilityState() {
        return this.facilityState;
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

    private int facilityId;
    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }
    public int getFacilityId() {
        return facilityId;
    }

    /*要删除的记录主键集合*/
    private String facilityIds;
    public String getFacilityIds() {
		return facilityIds;
	}
	public void setFacilityIds(String facilityIds) {
		this.facilityIds = facilityIds;
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
    @Resource FacilityDAO facilityDAO;

    /*待操作的Facility对象*/
    private Facility facility;
    public void setFacility(Facility facility) {
        this.facility = facility;
    }
    public Facility getFacility() {
        return this.facility;
    }

    /*ajax添加Facility信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddFacility() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try {
            facilityDAO.AddFacility(facility);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "Facility添加失败!";
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

    /*查询Facility信息*/
    public void ajaxQueryFacility() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(name == null) name = "";
        if(startTime == null) startTime = "";
        if(facilityState == null) facilityState = "";
        if(rows != 0) facilityDAO.setRows(rows);
        List<Facility> facilityList = facilityDAO.QueryFacilityInfo(name, startTime, facilityState, page);
        /*计算总的页数和总的记录数*/
        facilityDAO.CalculateTotalPageAndRecordNumber(name, startTime, facilityState);
        /*获取到总的页码数目*/
        totalPage = facilityDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = facilityDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Facility facility:facilityList) {
			JSONObject jsonFacility = facility.getJsonObject();
			jsonArray.put(jsonFacility);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询Facility信息*/
    public void ajaxQueryAllFacility() throws IOException, JSONException {
        List<Facility> facilityList = facilityDAO.QueryAllFacilityInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(Facility facility:facilityList) {
			JSONObject jsonFacility = new JSONObject();
			jsonFacility.accumulate("facilityId", facility.getFacilityId());
			jsonFacility.accumulate("name", facility.getName());
			jsonArray.put(jsonFacility);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }

    /*查询要修改的Facility信息*/
    public void ajaxModifyFacilityQuery() throws IOException, JSONException {
        /*根据主键facilityId获取Facility对象*/
        Facility facility = facilityDAO.GetFacilityByFacilityId(facilityId);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonFacility = facility.getJsonObject(); 
		out.println(jsonFacility.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改Facility信息*/
    public void ajaxModifyFacility() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            facilityDAO.UpdateFacility(facility);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "Facility修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除Facility信息*/
    public void ajaxDeleteFacility() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _facilityIds[] = facilityIds.split(",");
        	for(String _facilityId: _facilityIds) {
        		facilityDAO.DeleteFacility(Integer.parseInt(_facilityId));
        	}
        	success = true;
        	message = _facilityIds.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询Facility信息*/
    public String FrontQueryFacility() {
        if(page == 0) page = 1;
        if(name == null) name = "";
        if(startTime == null) startTime = "";
        if(facilityState == null) facilityState = "";
        List<Facility> facilityList = facilityDAO.QueryFacilityInfo(name, startTime, facilityState, page);
        /*计算总的页数和总的记录数*/
        facilityDAO.CalculateTotalPageAndRecordNumber(name, startTime, facilityState);
        /*获取到总的页码数目*/
        totalPage = facilityDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = facilityDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("facilityList",  facilityList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("name", name);
        ctx.put("startTime", startTime);
        ctx.put("facilityState", facilityState);
        return "front_query_view";
    }

    /*查询要修改的Facility信息*/
    public String FrontShowFacilityQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键facilityId获取Facility对象*/
        Facility facility = facilityDAO.GetFacilityByFacilityId(facilityId);

        ctx.put("facility",  facility);
        return "front_show_view";
    }

    /*删除Facility信息*/
    public String DeleteFacility() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            facilityDAO.DeleteFacility(facilityId);
            ctx.put("message", "Facility删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "Facility删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryFacilityOutputToExcel() { 
        if(name == null) name = "";
        if(startTime == null) startTime = "";
        if(facilityState == null) facilityState = "";
        List<Facility> facilityList = facilityDAO.QueryFacilityInfo(name,startTime,facilityState);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Facility信息记录"; 
        String[] headers = { "设施id","设施名称","数量","开始使用时间","设施状态"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<facilityList.size();i++) {
        	Facility facility = facilityList.get(i); 
        	dataset.add(new String[]{facility.getFacilityId() + "",facility.getName(),facility.getCount() + "",facility.getStartTime(),facility.getFacilityState()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Facility.xls");//filename是下载的xls的名，建议最好用英文 
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
