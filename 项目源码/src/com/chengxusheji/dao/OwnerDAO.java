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
import com.chengxusheji.domain.Building;
import com.chengxusheji.domain.Owner;

@Service @Transactional
public class OwnerDAO {

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
	public boolean CheckLogin(Admin admin) { 
		Session s = factory.getCurrentSession(); 

		Owner db_owner = (Owner)s.get(Owner.class, Integer.parseInt(admin.getUsername()));
		if(db_owner == null) { 
			this.errMessage = " 账号不存在 ";
			System.out.print(this.errMessage);
			return false;
		} else if( !db_owner.getPassword().equals(admin.getPassword())) {
			this.errMessage = " 密码不正确! ";
			System.out.print(this.errMessage);
			return false;
		}
		
		return true;
	}
	

    /*添加Owner信息*/
    public void AddOwner(Owner owner) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(owner);
    }

    /*查询Owner信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Owner> QueryOwnerInfo(Building buildingObj,String roomNo,String ownerName,String telephone,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Owner owner where 1=1";
    	if(null != buildingObj && buildingObj.getBuildingId()!=0) hql += " and owner.buildingObj.buildingId=" + buildingObj.getBuildingId();
    	if(!roomNo.equals("")) hql = hql + " and owner.roomNo like '%" + roomNo + "%'";
    	if(!ownerName.equals("")) hql = hql + " and owner.ownerName like '%" + ownerName + "%'";
    	if(!telephone.equals("")) hql = hql + " and owner.telephone like '%" + telephone + "%'";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List ownerList = q.list();
    	return (ArrayList<Owner>) ownerList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Owner> QueryOwnerInfo(Building buildingObj,String roomNo,String ownerName,String telephone) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Owner owner where 1=1";
    	if(null != buildingObj && buildingObj.getBuildingId()!=0) hql += " and owner.buildingObj.buildingId=" + buildingObj.getBuildingId();
    	if(!roomNo.equals("")) hql = hql + " and owner.roomNo like '%" + roomNo + "%'";
    	if(!ownerName.equals("")) hql = hql + " and owner.ownerName like '%" + ownerName + "%'";
    	if(!telephone.equals("")) hql = hql + " and owner.telephone like '%" + telephone + "%'";
    	Query q = s.createQuery(hql);
    	List ownerList = q.list();
    	return (ArrayList<Owner>) ownerList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Owner> QueryAllOwnerInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From Owner";
        Query q = s.createQuery(hql);
        List ownerList = q.list();
        return (ArrayList<Owner>) ownerList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(Building buildingObj,String roomNo,String ownerName,String telephone) {
        Session s = factory.getCurrentSession();
        String hql = "From Owner owner where 1=1";
        if(null != buildingObj && buildingObj.getBuildingId()!=0) hql += " and owner.buildingObj.buildingId=" + buildingObj.getBuildingId();
        if(!roomNo.equals("")) hql = hql + " and owner.roomNo like '%" + roomNo + "%'";
        if(!ownerName.equals("")) hql = hql + " and owner.ownerName like '%" + ownerName + "%'";
        if(!telephone.equals("")) hql = hql + " and owner.telephone like '%" + telephone + "%'";
        Query q = s.createQuery(hql);
        List ownerList = q.list();
        recordNumber = ownerList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Owner GetOwnerByOwnerId(int ownerId) {
        Session s = factory.getCurrentSession();
        Owner owner = (Owner)s.get(Owner.class, ownerId);
        return owner;
    }

    /*更新Owner信息*/
    public void UpdateOwner(Owner owner) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(owner);
    }

    /*删除Owner信息*/
    public void DeleteOwner (int ownerId) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object owner = s.load(Owner.class, ownerId);
        s.delete(owner);
    }

}
