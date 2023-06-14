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
import com.chengxusheji.dao.FeeDAO;
import com.chengxusheji.domain.Fee;
import com.chengxusheji.dao.FeeTypeDAO;
import com.chengxusheji.domain.FeeType;
import com.chengxusheji.dao.OwnerDAO;
import com.chengxusheji.domain.Owner;
@Controller @Scope("prototype")
public class FeeAction extends ActionSupport {

    /*界面层需要查询的属性: 费用类别*/
    private FeeType feeTypeObj;
    public void setFeeTypeObj(FeeType feeTypeObj) {
        this.feeTypeObj = feeTypeObj;
    }
    public FeeType getFeeTypeObj() {
        return this.feeTypeObj;
    }

    /*界面层需要查询的属性: 住户信息*/
    private Owner ownerObj;
    public void setOwnerObj(Owner ownerObj) {
        this.ownerObj = ownerObj;
    }
    public Owner getOwnerObj() {
        return this.ownerObj;
    }

    /*界面层需要查询的属性: 收费时间*/
    private String feeDate;
    public void setFeeDate(String feeDate) {
        this.feeDate = feeDate;
    }
    public String getFeeDate() {
        return this.feeDate;
    }

    /*界面层需要查询的属性: 收费内容*/
    private String feeContent;
    public void setFeeContent(String feeContent) {
        this.feeContent = feeContent;
    }
    public String getFeeContent() {
        return this.feeContent;
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

    private int feeId;
    public void setFeeId(int feeId) {
        this.feeId = feeId;
    }
    public int getFeeId() {
        return feeId;
    }

    /*要删除的记录主键集合*/
    private String feeIds;
    public String getFeeIds() {
		return feeIds;
	}
	public void setFeeIds(String feeIds) {
		this.feeIds = feeIds;
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
    @Resource FeeDAO feeDAO;

    @Resource FeeTypeDAO feeTypeDAO;
    @Resource OwnerDAO ownerDAO;
    /*待操作的Fee对象*/
    private Fee fee;
    public void setFee(Fee fee) {
        this.fee = fee;
    }
    public Fee getFee() {
        return this.fee;
    }

    /*ajax添加Fee信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddFee() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try {
            FeeType feeTypeObj = feeTypeDAO.GetFeeTypeByTypeId(fee.getFeeTypeObj().getTypeId());
            fee.setFeeTypeObj(feeTypeObj);
            Owner ownerObj = ownerDAO.GetOwnerByOwnerId(fee.getOwnerObj().getOwnerId());
            fee.setOwnerObj(ownerObj);
            feeDAO.AddFee(fee);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "Fee添加失败!";
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

    /*查询Fee信息*/
    public void ajaxQueryFee() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(feeDate == null) feeDate = "";
        if(feeContent == null) feeContent = "";
        if(opUser == null) opUser = "";
        if(rows != 0) feeDAO.setRows(rows);
        List<Fee> feeList = feeDAO.QueryFeeInfo(feeTypeObj, ownerObj, feeDate, feeContent, opUser, page);
        /*计算总的页数和总的记录数*/
        feeDAO.CalculateTotalPageAndRecordNumber(feeTypeObj, ownerObj, feeDate, feeContent, opUser);
        /*获取到总的页码数目*/
        totalPage = feeDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = feeDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Fee fee:feeList) {
			JSONObject jsonFee = fee.getJsonObject();
			jsonArray.put(jsonFee);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询Fee信息*/
    public void ajaxQueryAllFee() throws IOException, JSONException {
        List<Fee> feeList = feeDAO.QueryAllFeeInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(Fee fee:feeList) {
			JSONObject jsonFee = new JSONObject();
			jsonFee.accumulate("feeId", fee.getFeeId());
			jsonFee.accumulate("feeId", fee.getFeeId());
			jsonArray.put(jsonFee);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }

    /*查询要修改的Fee信息*/
    public void ajaxModifyFeeQuery() throws IOException, JSONException {
        /*根据主键feeId获取Fee对象*/
        Fee fee = feeDAO.GetFeeByFeeId(feeId);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonFee = fee.getJsonObject(); 
		out.println(jsonFee.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改Fee信息*/
    public void ajaxModifyFee() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            FeeType feeTypeObj = feeTypeDAO.GetFeeTypeByTypeId(fee.getFeeTypeObj().getTypeId());
            fee.setFeeTypeObj(feeTypeObj);
            Owner ownerObj = ownerDAO.GetOwnerByOwnerId(fee.getOwnerObj().getOwnerId());
            fee.setOwnerObj(ownerObj);
            feeDAO.UpdateFee(fee);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "Fee修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除Fee信息*/
    public void ajaxDeleteFee() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _feeIds[] = feeIds.split(",");
        	for(String _feeId: _feeIds) {
        		feeDAO.DeleteFee(Integer.parseInt(_feeId));
        	}
        	success = true;
        	message = _feeIds.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询Fee信息*/
    public String FrontQueryFee() {
        if(page == 0) page = 1;
        if(feeDate == null) feeDate = "";
        if(feeContent == null) feeContent = "";
        if(opUser == null) opUser = "";
        List<Fee> feeList = feeDAO.QueryFeeInfo(feeTypeObj, ownerObj, feeDate, feeContent, opUser, page);
        /*计算总的页数和总的记录数*/
        feeDAO.CalculateTotalPageAndRecordNumber(feeTypeObj, ownerObj, feeDate, feeContent, opUser);
        /*获取到总的页码数目*/
        totalPage = feeDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = feeDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("feeList",  feeList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("feeTypeObj", feeTypeObj);
        List<FeeType> feeTypeList = feeTypeDAO.QueryAllFeeTypeInfo();
        ctx.put("feeTypeList", feeTypeList);
        ctx.put("ownerObj", ownerObj);
        List<Owner> ownerList = ownerDAO.QueryAllOwnerInfo();
        ctx.put("ownerList", ownerList);
        ctx.put("feeDate", feeDate);
        ctx.put("feeContent", feeContent);
        ctx.put("opUser", opUser);
        return "front_query_view";
    }

    /*查询要修改的Fee信息*/
    public String FrontShowFeeQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键feeId获取Fee对象*/
        Fee fee = feeDAO.GetFeeByFeeId(feeId);

        List<FeeType> feeTypeList = feeTypeDAO.QueryAllFeeTypeInfo();
        ctx.put("feeTypeList", feeTypeList);
        List<Owner> ownerList = ownerDAO.QueryAllOwnerInfo();
        ctx.put("ownerList", ownerList);
        ctx.put("fee",  fee);
        return "front_show_view";
    }

    /*删除Fee信息*/
    public String DeleteFee() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            feeDAO.DeleteFee(feeId);
            ctx.put("message", "Fee删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "Fee删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String queryFeeOutputToExcel() { 
        if(feeDate == null) feeDate = "";
        if(feeContent == null) feeContent = "";
        if(opUser == null) opUser = "";
        List<Fee> feeList = feeDAO.QueryFeeInfo(feeTypeObj,ownerObj,feeDate,feeContent,opUser);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Fee信息记录"; 
        String[] headers = { "费用id","费用类别","住户信息","收费时间","收费金额","收费内容","操作员"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<feeList.size();i++) {
        	Fee fee = feeList.get(i); 
        	dataset.add(new String[]{fee.getFeeId() + "",fee.getFeeTypeObj().getTypeName(),
fee.getOwnerObj().getOwnerName(),
fee.getFeeDate(),fee.getFeeMoney() + "",fee.getFeeContent(),fee.getOpUser()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Fee.xls");//filename是下载的xls的名，建议最好用英文 
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
