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
import com.chengxusheji.dao.ParkingDAO;
import com.chengxusheji.domain.Parking;
import com.chengxusheji.dao.OwnerDAO;
import com.chengxusheji.domain.Owner;
@Controller @Scope("prototype")
public class ParkingAction extends ActionSupport {

    /*界面层需要查询的属性: 车位名称*/
    private String parkingName;
    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }
    public String getParkingName() {
        return this.parkingName;
    }

    /*界面层需要查询的属性: 车牌号*/
    private String plateNumber;
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
    public String getPlateNumber() {
        return this.plateNumber;
    }

    /*界面层需要查询的属性: 车主*/
    private Owner ownerObj;
    public void setOwnerObj(Owner ownerObj) {
        this.ownerObj = ownerObj;
    }
    public Owner getOwnerObj() {
        return this.ownerObj;
    }

    /*界面层需要查询的属性: 操作员*/
    private String opUser;
    public void setOpUser(String opUser) {
        this.opUser = opUser;
    }
    public String getOpUser() {
        return this.opUser;
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

    private int parkingId;
    public void setParkingId(int parkingId) {
        this.parkingId = parkingId;
    }
    public int getParkingId() {
        return parkingId;
    }

    /*要删除的记录主键集合*/
    private String parkingIds;
    public String getParkingIds() {
		return parkingIds;
	}
	public void setParkingIds(String parkingIds) {
		this.parkingIds = parkingIds;
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
    @Resource ParkingDAO parkingDAO;

    @Resource OwnerDAO ownerDAO;
    /*待操作的Parking对象*/
    private Parking parking;
    public void setParking(Parking parking) {
        this.parking = parking;
    }
    public Parking getParking() {
        return this.parking;
    }

    /*ajax添加Parking信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddParking() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try {
            Owner ownerObj = ownerDAO.GetOwnerByOwnerId(parking.getOwnerObj().getOwnerId());
            parking.setOwnerObj(ownerObj);
            parkingDAO.AddParking(parking);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "Parking添加失败!";
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

    /*查询Parking信息*/
    public void ajaxQueryParking() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(parkingName == null) parkingName = "";
        if(plateNumber == null) plateNumber = "";
        if(opUser == null) opUser = "";
        if(rows != 0) parkingDAO.setRows(rows);
        List<Parking> parkingList = parkingDAO.QueryParkingInfo(parkingName, plateNumber, ownerObj, opUser, page);
        /*计算总的页数和总的记录数*/
        parkingDAO.CalculateTotalPageAndRecordNumber(parkingName, plateNumber, ownerObj, opUser);
        /*获取到总的页码数目*/
        totalPage = parkingDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = parkingDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Parking parking:parkingList) {
			JSONObject jsonParking = parking.getJsonObject();
			jsonArray.put(jsonParking);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询Parking信息*/
    public void ajaxQueryAllParking() throws IOException, JSONException {
        List<Parking> parkingList = parkingDAO.QueryAllParkingInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(Parking parking:parkingList) {
			JSONObject jsonParking = new JSONObject();
			jsonParking.accumulate("parkingId", parking.getParkingId());
			jsonParking.accumulate("parkingName", parking.getParkingName());
			jsonArray.put(jsonParking);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }

    /*查询要修改的Parking信息*/
    public void ajaxModifyParkingQuery() throws IOException, JSONException {
        /*根据主键parkingId获取Parking对象*/
        Parking parking = parkingDAO.GetParkingByParkingId(parkingId);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonParking = parking.getJsonObject(); 
		out.println(jsonParking.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改Parking信息*/
    public void ajaxModifyParking() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            Owner ownerObj = ownerDAO.GetOwnerByOwnerId(parking.getOwnerObj().getOwnerId());
            parking.setOwnerObj(ownerObj);
            parkingDAO.UpdateParking(parking);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "Parking修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除Parking信息*/
    public void ajaxDeleteParking() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _parkingIds[] = parkingIds.split(",");
        	for(String _parkingId: _parkingIds) {
        		parkingDAO.DeleteParking(Integer.parseInt(_parkingId));
        	}
        	success = true;
        	message = _parkingIds.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询Parking信息*/
    public String FrontQueryParking() {
        if(page == 0) page = 1;
        if(parkingName == null) parkingName = "";
        if(plateNumber == null) plateNumber = "";
        if(opUser == null) opUser = "";
        List<Parking> parkingList = parkingDAO.QueryParkingInfo(parkingName, plateNumber, ownerObj, opUser, page);
        /*计算总的页数和总的记录数*/
        parkingDAO.CalculateTotalPageAndRecordNumber(parkingName, plateNumber, ownerObj, opUser);
        /*获取到总的页码数目*/
        totalPage = parkingDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = parkingDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("parkingList",  parkingList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("parkingName", parkingName);
        ctx.put("plateNumber", plateNumber);
        ctx.put("ownerObj", ownerObj);
        List<Owner> ownerList = ownerDAO.QueryAllOwnerInfo();
        ctx.put("ownerList", ownerList);
        ctx.put("opUser", opUser);
        return "front_query_view";
    }

    /*查询要修改的Parking信息*/
    public String FrontShowParkingQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键parkingId获取Parking对象*/
        Parking parking = parkingDAO.GetParkingByParkingId(parkingId);

        List<Owner> ownerList = ownerDAO.QueryAllOwnerInfo();
        ctx.put("ownerList", ownerList);
        ctx.put("parking",  parking);
        return "front_show_view";
    }

    /*删除Parking信息*/
    public String DeleteParking() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            parkingDAO.DeleteParking(parkingId);
            ctx.put("message", "Parking删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "Parking删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryParkingOutputToExcel() { 
        if(parkingName == null) parkingName = "";
        if(plateNumber == null) plateNumber = "";
        if(opUser == null) opUser = "";
        List<Parking> parkingList = parkingDAO.QueryParkingInfo(parkingName,plateNumber,ownerObj,opUser);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Parking信息记录"; 
        String[] headers = { "车位id","车位名称","车牌号","车主","操作员"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<parkingList.size();i++) {
        	Parking parking = parkingList.get(i); 
        	dataset.add(new String[]{parking.getParkingId() + "",parking.getParkingName(),parking.getPlateNumber(),parking.getOwnerObj().getOwnerName(),
parking.getOpUser()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Parking.xls");//filename是下载的xls的名，建议最好用英文 
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
