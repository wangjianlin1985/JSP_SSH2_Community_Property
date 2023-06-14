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
import com.chengxusheji.dao.ManagerDAO;
import com.chengxusheji.domain.Manager;
@Controller @Scope("prototype")
public class ManagerAction extends ActionSupport {

    /*界面层需要查询的属性: 用户名*/
    private String manageUserName;
    public void setManageUserName(String manageUserName) {
        this.manageUserName = manageUserName;
    }
    public String getManageUserName() {
        return this.manageUserName;
    }

    /*界面层需要查询的属性: 管理员类别*/
    private String manageType;
    public void setManageType(String manageType) {
        this.manageType = manageType;
    }
    public String getManageType() {
        return this.manageType;
    }

    /*界面层需要查询的属性: 姓名*/
    private String name;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    /*界面层需要查询的属性: 联系电话*/
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

    /*要删除的记录主键集合*/
    private String manageUserNames;
    public String getManageUserNames() {
		return manageUserNames;
	}
	public void setManageUserNames(String manageUserNames) {
		this.manageUserNames = manageUserNames;
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
    @Resource ManagerDAO managerDAO;

    /*待操作的Manager对象*/
    private Manager manager;
    public void setManager(Manager manager) {
        this.manager = manager;
    }
    public Manager getManager() {
        return this.manager;
    }

    /*ajax添加Manager信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddManager() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        /*验证用户名是否已经存在*/
        String manageUserName = manager.getManageUserName();
        Manager db_manager = managerDAO.GetManagerByManageUserName(manageUserName);
        if(null != db_manager) {
        	message = "该用户名已经存在!";
        	writeJsonResponse(success, message);
        	return ;
        }
        try {
            managerDAO.AddManager(manager);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "Manager添加失败!";
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

    /*查询Manager信息*/
    public void ajaxQueryManager() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(manageUserName == null) manageUserName = "";
        if(manageType == null) manageType = "";
        if(name == null) name = "";
        if(telephone == null) telephone = "";
        if(rows != 0) managerDAO.setRows(rows);
        List<Manager> managerList = managerDAO.QueryManagerInfo(manageUserName, manageType, name, telephone, page);
        /*计算总的页数和总的记录数*/
        managerDAO.CalculateTotalPageAndRecordNumber(manageUserName, manageType, name, telephone);
        /*获取到总的页码数目*/
        totalPage = managerDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = managerDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Manager manager:managerList) {
			JSONObject jsonManager = manager.getJsonObject();
			jsonArray.put(jsonManager);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询Manager信息*/
    public void ajaxQueryAllManager() throws IOException, JSONException {
        List<Manager> managerList = managerDAO.QueryAllManagerInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(Manager manager:managerList) {
			JSONObject jsonManager = new JSONObject();
			jsonManager.accumulate("manageUserName", manager.getManageUserName());
			jsonManager.accumulate("name", manager.getName());
			jsonArray.put(jsonManager);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }

    /*查询要修改的Manager信息*/
    public void ajaxModifyManagerQuery() throws IOException, JSONException {
        /*根据主键manageUserName获取Manager对象*/
        Manager manager = managerDAO.GetManagerByManageUserName(manageUserName);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonManager = manager.getJsonObject(); 
		out.println(jsonManager.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改Manager信息*/
    public void ajaxModifyManager() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            managerDAO.UpdateManager(manager);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "Manager修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除Manager信息*/
    public void ajaxDeleteManager() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _manageUserNames[] = manageUserNames.split(",");
        	for(String _manageUserName: _manageUserNames) {
        		managerDAO.DeleteManager(_manageUserName);
        	}
        	success = true;
        	message = _manageUserNames.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询Manager信息*/
    public String FrontQueryManager() {
        if(page == 0) page = 1;
        if(manageUserName == null) manageUserName = "";
        if(manageType == null) manageType = "";
        if(name == null) name = "";
        if(telephone == null) telephone = "";
        List<Manager> managerList = managerDAO.QueryManagerInfo(manageUserName, manageType, name, telephone, page);
        /*计算总的页数和总的记录数*/
        managerDAO.CalculateTotalPageAndRecordNumber(manageUserName, manageType, name, telephone);
        /*获取到总的页码数目*/
        totalPage = managerDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = managerDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("managerList",  managerList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("manageUserName", manageUserName);
        ctx.put("manageType", manageType);
        ctx.put("name", name);
        ctx.put("telephone", telephone);
        return "front_query_view";
    }

    /*查询要修改的Manager信息*/
    public String FrontShowManagerQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键manageUserName获取Manager对象*/
        Manager manager = managerDAO.GetManagerByManageUserName(manageUserName);

        ctx.put("manager",  manager);
        return "front_show_view";
    }

    /*删除Manager信息*/
    public String DeleteManager() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            managerDAO.DeleteManager(manageUserName);
            ctx.put("message", "Manager删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "Manager删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryManagerOutputToExcel() { 
        if(manageUserName == null) manageUserName = "";
        if(manageType == null) manageType = "";
        if(name == null) name = "";
        if(telephone == null) telephone = "";
        List<Manager> managerList = managerDAO.QueryManagerInfo(manageUserName,manageType,name,telephone);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Manager信息记录"; 
        String[] headers = { "用户名","管理员类别","姓名","性别","联系电话"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<managerList.size();i++) {
        	Manager manager = managerList.get(i); 
        	dataset.add(new String[]{manager.getManageUserName(),manager.getManageType(),manager.getName(),manager.getSex(),manager.getTelephone()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Manager.xls");//filename是下载的xls的名，建议最好用英文 
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
