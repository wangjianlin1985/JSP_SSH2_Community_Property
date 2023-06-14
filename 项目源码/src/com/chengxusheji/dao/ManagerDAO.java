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

import com.chengxusheji.domain.Admin;
import com.chengxusheji.domain.Manager;
import com.chengxusheji.domain.Owner;

@Service @Transactional
public class ManagerDAO {

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
    
    
    /*保存业务逻辑错误信息字段*/
	private String errMessage;
	public String getErrMessage() { return this.errMessage; }
	
	 
	
	/*验证用户登录*/
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public boolean CheckLogin(Admin admin,String managerType) { 
		Session s = factory.getCurrentSession();  
		Manager db_manager = (Manager)s.get(Manager.class, admin.getUsername());
		if(db_manager == null) { 
			this.errMessage = " 账号不存在 ";
			System.out.print(this.errMessage);
			return false;
		} else if( !db_manager.getPassword().equals(admin.getPassword())) {
			this.errMessage = " 密码不正确! ";
			System.out.print(this.errMessage);
			return false;
		} else if( !db_manager.getManageType().equals(managerType)) {
			this.errMessage = " 你不是" + managerType + "! ";
			System.out.print(this.errMessage);
			return false;
		}
		
		return true;
	}
	
	 
	

    /*添加Manager信息*/
    public void AddManager(Manager manager) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(manager);
    }

    /*查询Manager信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Manager> QueryManagerInfo(String manageUserName,String manageType,String name,String telephone,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Manager manager where 1=1";
    	if(!manageUserName.equals("")) hql = hql + " and manager.manageUserName like '%" + manageUserName + "%'";
    	if(!manageType.equals("")) hql = hql + " and manager.manageType like '%" + manageType + "%'";
    	if(!name.equals("")) hql = hql + " and manager.name like '%" + name + "%'";
    	if(!telephone.equals("")) hql = hql + " and manager.telephone like '%" + telephone + "%'";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List managerList = q.list();
    	return (ArrayList<Manager>) managerList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Manager> QueryManagerInfo(String manageUserName,String manageType,String name,String telephone) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Manager manager where 1=1";
    	if(!manageUserName.equals("")) hql = hql + " and manager.manageUserName like '%" + manageUserName + "%'";
    	if(!manageType.equals("")) hql = hql + " and manager.manageType like '%" + manageType + "%'";
    	if(!name.equals("")) hql = hql + " and manager.name like '%" + name + "%'";
    	if(!telephone.equals("")) hql = hql + " and manager.telephone like '%" + telephone + "%'";
    	Query q = s.createQuery(hql);
    	List managerList = q.list();
    	return (ArrayList<Manager>) managerList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Manager> QueryAllManagerInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From Manager";
        Query q = s.createQuery(hql);
        List managerList = q.list();
        return (ArrayList<Manager>) managerList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String manageUserName,String manageType,String name,String telephone) {
        Session s = factory.getCurrentSession();
        String hql = "From Manager manager where 1=1";
        if(!manageUserName.equals("")) hql = hql + " and manager.manageUserName like '%" + manageUserName + "%'";
        if(!manageType.equals("")) hql = hql + " and manager.manageType like '%" + manageType + "%'";
        if(!name.equals("")) hql = hql + " and manager.name like '%" + name + "%'";
        if(!telephone.equals("")) hql = hql + " and manager.telephone like '%" + telephone + "%'";
        Query q = s.createQuery(hql);
        List managerList = q.list();
        recordNumber = managerList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Manager GetManagerByManageUserName(String manageUserName) {
        Session s = factory.getCurrentSession();
        Manager manager = (Manager)s.get(Manager.class, manageUserName);
        return manager;
    }

    /*更新Manager信息*/
    public void UpdateManager(Manager manager) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(manager);
    }

    /*删除Manager信息*/
    public void DeleteManager (String manageUserName) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object manager = s.load(Manager.class, manageUserName);
        s.delete(manager);
    }

}
