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
import com.chengxusheji.dao.FeeTypeDAO;
import com.chengxusheji.domain.FeeType;
@Controller @Scope("prototype")
public class FeeTypeAction extends ActionSupport {

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

    private int typeId;
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
    public int getTypeId() {
        return typeId;
    }

    /*要删除的记录主键集合*/
    private String typeIds;
    public String getTypeIds() {
		return typeIds;
	}
	public void setTypeIds(String typeIds) {
		this.typeIds = typeIds;
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
    @Resource FeeTypeDAO feeTypeDAO;

    /*待操作的FeeType对象*/
    private FeeType feeType;
    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
    }
    public FeeType getFeeType() {
        return this.feeType;
    }

    /*ajax添加FeeType信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddFeeType() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try {
            feeTypeDAO.AddFeeType(feeType);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "FeeType添加失败!";
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

    /*查询FeeType信息*/
    public void ajaxQueryFeeType() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(rows != 0) feeTypeDAO.setRows(rows);
        List<FeeType> feeTypeList = feeTypeDAO.QueryFeeTypeInfo(page);
        /*计算总的页数和总的记录数*/
        feeTypeDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = feeTypeDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = feeTypeDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(FeeType feeType:feeTypeList) {
			JSONObject jsonFeeType = feeType.getJsonObject();
			jsonArray.put(jsonFeeType);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询FeeType信息*/
    public void ajaxQueryAllFeeType() throws IOException, JSONException {
        List<FeeType> feeTypeList = feeTypeDAO.QueryAllFeeTypeInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(FeeType feeType:feeTypeList) {
			JSONObject jsonFeeType = new JSONObject();
			jsonFeeType.accumulate("typeId", feeType.getTypeId());
			jsonFeeType.accumulate("typeName", feeType.getTypeName());
			jsonArray.put(jsonFeeType);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }

    /*查询要修改的FeeType信息*/
    public void ajaxModifyFeeTypeQuery() throws IOException, JSONException {
        /*根据主键typeId获取FeeType对象*/
        FeeType feeType = feeTypeDAO.GetFeeTypeByTypeId(typeId);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonFeeType = feeType.getJsonObject(); 
		out.println(jsonFeeType.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改FeeType信息*/
    public void ajaxModifyFeeType() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            feeTypeDAO.UpdateFeeType(feeType);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "FeeType修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除FeeType信息*/
    public void ajaxDeleteFeeType() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _typeIds[] = typeIds.split(",");
        	for(String _typeId: _typeIds) {
        		feeTypeDAO.DeleteFeeType(Integer.parseInt(_typeId));
        	}
        	success = true;
        	message = _typeIds.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询FeeType信息*/
    public String FrontQueryFeeType() {
        if(page == 0) page = 1;
        List<FeeType> feeTypeList = feeTypeDAO.QueryFeeTypeInfo(page);
        /*计算总的页数和总的记录数*/
        feeTypeDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = feeTypeDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = feeTypeDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("feeTypeList",  feeTypeList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        return "front_query_view";
    }

    /*查询要修改的FeeType信息*/
    public String FrontShowFeeTypeQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键typeId获取FeeType对象*/
        FeeType feeType = feeTypeDAO.GetFeeTypeByTypeId(typeId);

        ctx.put("feeType",  feeType);
        return "front_show_view";
    }

    /*删除FeeType信息*/
    public String DeleteFeeType() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            feeTypeDAO.DeleteFeeType(typeId);
            ctx.put("message", "FeeType删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "FeeType删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryFeeTypeOutputToExcel() { 
        List<FeeType> feeTypeList = feeTypeDAO.QueryFeeTypeInfo();
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "FeeType信息记录"; 
        String[] headers = { "类别id","类别名称"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<feeTypeList.size();i++) {
        	FeeType feeType = feeTypeList.get(i); 
        	dataset.add(new String[]{feeType.getTypeId() + "",feeType.getTypeName()});
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
			response.setHeader("Content-disposition","attachment; filename="+"FeeType.xls");//filename是下载的xls的名，建议最好用英文 
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
