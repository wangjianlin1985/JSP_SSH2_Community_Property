package com.chengxusheji.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.chengxusheji.domain.Employee;
import com.chengxusheji.domain.Salary;

@Service @Transactional
public class SalaryDAO {

	@Resource SessionFactory factory;
    /*每页显示记录数目*/
    private int rows = 5;;
    public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加Salary信息*/
    public void AddSalary(Salary salary) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(salary);
    }

    /*查询Salary信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Salary> QuerySalaryInfo(Employee employeeObj,String year,String month,String fafang,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Salary salary where 1=1";
    	if(null != employeeObj && !employeeObj.getEmployeeNo().equals("")) hql += " and salary.employeeObj.employeeNo='" + employeeObj.getEmployeeNo() + "'";
    	if(!year.equals("")) hql = hql + " and salary.year like '%" + year + "%'";
    	if(!month.equals("")) hql = hql + " and salary.month like '%" + month + "%'";
    	if(!fafang.equals("")) hql = hql + " and salary.fafang like '%" + fafang + "%'";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List salaryList = q.list();
    	return (ArrayList<Salary>) salaryList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Salary> QuerySalaryInfo(Employee employeeObj,String year,String month,String fafang) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Salary salary where 1=1";
    	if(null != employeeObj && !employeeObj.getEmployeeNo().equals("")) hql += " and salary.employeeObj.employeeNo='" + employeeObj.getEmployeeNo() + "'";
    	if(!year.equals("")) hql = hql + " and salary.year like '%" + year + "%'";
    	if(!month.equals("")) hql = hql + " and salary.month like '%" + month + "%'";
    	if(!fafang.equals("")) hql = hql + " and salary.fafang like '%" + fafang + "%'";
    	Query q = s.createQuery(hql);
    	List salaryList = q.list();
    	return (ArrayList<Salary>) salaryList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Salary> QueryAllSalaryInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From Salary";
        Query q = s.createQuery(hql);
        List salaryList = q.list();
        return (ArrayList<Salary>) salaryList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(Employee employeeObj,String year,String month,String fafang) {
        Session s = factory.getCurrentSession();
        String hql = "From Salary salary where 1=1";
        if(null != employeeObj && !employeeObj.getEmployeeNo().equals("")) hql += " and salary.employeeObj.employeeNo='" + employeeObj.getEmployeeNo() + "'";
        if(!year.equals("")) hql = hql + " and salary.year like '%" + year + "%'";
        if(!month.equals("")) hql = hql + " and salary.month like '%" + month + "%'";
        if(!fafang.equals("")) hql = hql + " and salary.fafang like '%" + fafang + "%'";
        Query q = s.createQuery(hql);
        List salaryList = q.list();
        recordNumber = salaryList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Salary GetSalaryBySalaryId(int salaryId) {
        Session s = factory.getCurrentSession();
        Salary salary = (Salary)s.get(Salary.class, salaryId);
        return salary;
    }

    /*更新Salary信息*/
    public void UpdateSalary(Salary salary) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(salary);
    }

    /*删除Salary信息*/
    public void DeleteSalary (int salaryId) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object salary = s.load(Salary.class, salaryId);
        s.delete(salary);
    }

}
