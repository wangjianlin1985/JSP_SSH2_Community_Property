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

@Service @Transactional
public class EmployeeDAO {

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

    /*添加Employee信息*/
    public void AddEmployee(Employee employee) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(employee);
    }

    /*查询Employee信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Employee> QueryEmployeeInfo(String employeeNo,String name,String positionName,String telephone,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Employee employee where 1=1";
    	if(!employeeNo.equals("")) hql = hql + " and employee.employeeNo like '%" + employeeNo + "%'";
    	if(!name.equals("")) hql = hql + " and employee.name like '%" + name + "%'";
    	if(!positionName.equals("")) hql = hql + " and employee.positionName like '%" + positionName + "%'";
    	if(!telephone.equals("")) hql = hql + " and employee.telephone like '%" + telephone + "%'";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List employeeList = q.list();
    	return (ArrayList<Employee>) employeeList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Employee> QueryEmployeeInfo(String employeeNo,String name,String positionName,String telephone) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Employee employee where 1=1";
    	if(!employeeNo.equals("")) hql = hql + " and employee.employeeNo like '%" + employeeNo + "%'";
    	if(!name.equals("")) hql = hql + " and employee.name like '%" + name + "%'";
    	if(!positionName.equals("")) hql = hql + " and employee.positionName like '%" + positionName + "%'";
    	if(!telephone.equals("")) hql = hql + " and employee.telephone like '%" + telephone + "%'";
    	Query q = s.createQuery(hql);
    	List employeeList = q.list();
    	return (ArrayList<Employee>) employeeList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Employee> QueryAllEmployeeInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From Employee";
        Query q = s.createQuery(hql);
        List employeeList = q.list();
        return (ArrayList<Employee>) employeeList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String employeeNo,String name,String positionName,String telephone) {
        Session s = factory.getCurrentSession();
        String hql = "From Employee employee where 1=1";
        if(!employeeNo.equals("")) hql = hql + " and employee.employeeNo like '%" + employeeNo + "%'";
        if(!name.equals("")) hql = hql + " and employee.name like '%" + name + "%'";
        if(!positionName.equals("")) hql = hql + " and employee.positionName like '%" + positionName + "%'";
        if(!telephone.equals("")) hql = hql + " and employee.telephone like '%" + telephone + "%'";
        Query q = s.createQuery(hql);
        List employeeList = q.list();
        recordNumber = employeeList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Employee GetEmployeeByEmployeeNo(String employeeNo) {
        Session s = factory.getCurrentSession();
        Employee employee = (Employee)s.get(Employee.class, employeeNo);
        return employee;
    }

    /*更新Employee信息*/
    public void UpdateEmployee(Employee employee) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(employee);
    }

    /*删除Employee信息*/
    public void DeleteEmployee (String employeeNo) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object employee = s.load(Employee.class, employeeNo);
        s.delete(employee);
    }

}
