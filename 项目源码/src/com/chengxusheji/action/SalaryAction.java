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
import com.chengxusheji.dao.SalaryDAO;
import com.chengxusheji.domain.Salary;
import com.chengxusheji.dao.EmployeeDAO;
import com.chengxusheji.domain.Employee;
@Controller @Scope("prototype")
public class SalaryAction extends ActionSupport {

    /*界面层需要查询的属性: 员工*/
    private Employee employeeObj;
    public void setEmployeeObj(Employee employeeObj) {
        this.employeeObj = employeeObj;
    }
    public Employee getEmployeeObj() {
        return this.employeeObj;
    }

    /*界面层需要查询的属性: 工资年份*/
    private String year;
    public void setYear(String year) {
        this.year = year;
    }
    public String getYear() {
        return this.year;
    }

    /*界面层需要查询的属性: 工资月份*/
    private String month;
    public void setMonth(String month) {
        this.month = month;
    }
    public String getMonth() {
        return this.month;
    }

    /*界面层需要查询的属性: 是否发放*/
    private String fafang;
    public void setFafang(String fafang) {
        this.fafang = fafang;
    }
    public String getFafang() {
        return this.fafang;
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

    private int salaryId;
    public void setSalaryId(int salaryId) {
        this.salaryId = salaryId;
    }
    public int getSalaryId() {
        return salaryId;
    }

    /*要删除的记录主键集合*/
    private String salaryIds;
    public String getSalaryIds() {
		return salaryIds;
	}
	public void setSalaryIds(String salaryIds) {
		this.salaryIds = salaryIds;
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
    @Resource SalaryDAO salaryDAO;

    @Resource EmployeeDAO employeeDAO;
    /*待操作的Salary对象*/
    private Salary salary;
    public void setSalary(Salary salary) {
        this.salary = salary;
    }
    public Salary getSalary() {
        return this.salary;
    }

    /*ajax添加Salary信息*/
    @SuppressWarnings("deprecation")
    public void ajaxAddSalary() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try {
            Employee employeeObj = employeeDAO.GetEmployeeByEmployeeNo(salary.getEmployeeObj().getEmployeeNo());
            salary.setEmployeeObj(employeeObj);
            salaryDAO.AddSalary(salary);
            success = true;
            writeJsonResponse(success, message); 
        } catch (Exception e) {
            e.printStackTrace();
            message = "Salary添加失败!";
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

    /*查询Salary信息*/
    public void ajaxQuerySalary() throws IOException, JSONException {
        if(page == 0) page = 1;
        if(year == null) year = "";
        if(month == null) month = "";
        if(fafang == null) fafang = "";
        if(rows != 0) salaryDAO.setRows(rows);
        List<Salary> salaryList = salaryDAO.QuerySalaryInfo(employeeObj, year, month, fafang, page);
        /*计算总的页数和总的记录数*/
        salaryDAO.CalculateTotalPageAndRecordNumber(employeeObj, year, month, fafang);
        /*获取到总的页码数目*/
        totalPage = salaryDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = salaryDAO.getRecordNumber();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Salary salary:salaryList) {
			JSONObject jsonSalary = salary.getJsonObject();
			jsonArray.put(jsonSalary);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
    }

    /*查询Salary信息*/
    public void ajaxQueryAllSalary() throws IOException, JSONException {
        List<Salary> salaryList = salaryDAO.QueryAllSalaryInfo();        HttpServletResponse response=ServletActionContext.getResponse();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONArray jsonArray = new JSONArray();
		for(Salary salary:salaryList) {
			JSONObject jsonSalary = new JSONObject();
			jsonSalary.accumulate("salaryId", salary.getSalaryId());
			jsonSalary.accumulate("salaryId", salary.getSalaryId());
			jsonArray.put(jsonSalary);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
    }

    /*查询要修改的Salary信息*/
    public void ajaxModifySalaryQuery() throws IOException, JSONException {
        /*根据主键salaryId获取Salary对象*/
        Salary salary = salaryDAO.GetSalaryBySalaryId(salaryId);

        HttpServletResponse response=ServletActionContext.getResponse(); 
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		//将要被返回到客户端的对象 
		JSONObject jsonSalary = salary.getJsonObject(); 
		out.println(jsonSalary.toString()); 
		out.flush();
		out.close();
    };

    /*更新修改Salary信息*/
    public void ajaxModifySalary() throws IOException, JSONException{
    	String message = "";
    	boolean success = false;
        try {
            Employee employeeObj = employeeDAO.GetEmployeeByEmployeeNo(salary.getEmployeeObj().getEmployeeNo());
            salary.setEmployeeObj(employeeObj);
            salaryDAO.UpdateSalary(salary);
            success = true;
            writeJsonResponse(success, message);
        } catch (Exception e) {
            message = "Salary修改失败!"; 
            writeJsonResponse(success, message);
       }
   }

    /*删除Salary信息*/
    public void ajaxDeleteSalary() throws IOException, JSONException {
    	String message = "";
    	boolean success = false;
        try { 
        	String _salaryIds[] = salaryIds.split(",");
        	for(String _salaryId: _salaryIds) {
        		salaryDAO.DeleteSalary(Integer.parseInt(_salaryId));
        	}
        	success = true;
        	message = _salaryIds.length + "条记录删除成功";
        	writeJsonResponse(success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(success, message);
        }
    }

    /*前台查询Salary信息*/
    public String FrontQuerySalary() {
        if(page == 0) page = 1;
        if(year == null) year = "";
        if(month == null) month = "";
        if(fafang == null) fafang = "";
        List<Salary> salaryList = salaryDAO.QuerySalaryInfo(employeeObj, year, month, fafang, page);
        /*计算总的页数和总的记录数*/
        salaryDAO.CalculateTotalPageAndRecordNumber(employeeObj, year, month, fafang);
        /*获取到总的页码数目*/
        totalPage = salaryDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = salaryDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("salaryList",  salaryList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("page", page);
        ctx.put("employeeObj", employeeObj);
        List<Employee> employeeList = employeeDAO.QueryAllEmployeeInfo();
        ctx.put("employeeList", employeeList);
        ctx.put("year", year);
        ctx.put("month", month);
        ctx.put("fafang", fafang);
        return "front_query_view";
    }

    /*查询要修改的Salary信息*/
    public String FrontShowSalaryQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键salaryId获取Salary对象*/
        Salary salary = salaryDAO.GetSalaryBySalaryId(salaryId);

        List<Employee> employeeList = employeeDAO.QueryAllEmployeeInfo();
        ctx.put("employeeList", employeeList);
        ctx.put("salary",  salary);
        return "front_show_view";
    }

    /*删除Salary信息*/
    public String DeleteSalary() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            salaryDAO.DeleteSalary(salaryId);
            ctx.put("message", "Salary删除成功!");
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error", "Salary删除失败!");
            return "error";
        }
    }

    /*后台导出到excel*/
    public String querySalaryOutputToExcel() { 
        if(year == null) year = "";
        if(month == null) month = "";
        if(fafang == null) fafang = "";
        List<Salary> salaryList = salaryDAO.QuerySalaryInfo(employeeObj,year,month,fafang);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Salary信息记录"; 
        String[] headers = { "工资id","员工","工资年份","工资月份","工资金额","是否发放"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<salaryList.size();i++) {
        	Salary salary = salaryList.get(i); 
        	dataset.add(new String[]{salary.getSalaryId() + "",salary.getEmployeeObj().getName(),
salary.getYear(),salary.getMonth(),salary.getSalaryMoney() + "",salary.getFafang()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Salary.xls");//filename是下载的xls的名，建议最好用英文 
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
