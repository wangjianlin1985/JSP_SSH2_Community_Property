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
import com.chengxusheji.domain.Owner;
import com.chengxusheji.domain.Parking;

@Service @Transactional
public class ParkingDAO {

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

    /*添加Parking信息*/
    public void AddParking(Parking parking) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(parking);
    }

    /*查询Parking信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Parking> QueryParkingInfo(String parkingName,String plateNumber,Owner ownerObj,String opUser,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Parking parking where 1=1";
    	if(!parkingName.equals("")) hql = hql + " and parking.parkingName like '%" + parkingName + "%'";
    	if(!plateNumber.equals("")) hql = hql + " and parking.plateNumber like '%" + plateNumber + "%'";
    	if(null != ownerObj && ownerObj.getOwnerId()!=0) hql += " and parking.ownerObj.ownerId=" + ownerObj.getOwnerId();
    	if(!opUser.equals("")) hql = hql + " and parking.opUser like '%" + opUser + "%'";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List parkingList = q.list();
    	return (ArrayList<Parking>) parkingList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Parking> QueryParkingInfo(String parkingName,String plateNumber,Owner ownerObj,String opUser) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Parking parking where 1=1";
    	if(!parkingName.equals("")) hql = hql + " and parking.parkingName like '%" + parkingName + "%'";
    	if(!plateNumber.equals("")) hql = hql + " and parking.plateNumber like '%" + plateNumber + "%'";
    	if(null != ownerObj && ownerObj.getOwnerId()!=0) hql += " and parking.ownerObj.ownerId=" + ownerObj.getOwnerId();
    	if(!opUser.equals("")) hql = hql + " and parking.opUser like '%" + opUser + "%'";
    	Query q = s.createQuery(hql);
    	List parkingList = q.list();
    	return (ArrayList<Parking>) parkingList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Parking> QueryAllParkingInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From Parking";
        Query q = s.createQuery(hql);
        List parkingList = q.list();
        return (ArrayList<Parking>) parkingList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String parkingName,String plateNumber,Owner ownerObj,String opUser) {
        Session s = factory.getCurrentSession();
        String hql = "From Parking parking where 1=1";
        if(!parkingName.equals("")) hql = hql + " and parking.parkingName like '%" + parkingName + "%'";
        if(!plateNumber.equals("")) hql = hql + " and parking.plateNumber like '%" + plateNumber + "%'";
        if(null != ownerObj && ownerObj.getOwnerId()!=0) hql += " and parking.ownerObj.ownerId=" + ownerObj.getOwnerId();
        if(!opUser.equals("")) hql = hql + " and parking.opUser like '%" + opUser + "%'";
        Query q = s.createQuery(hql);
        List parkingList = q.list();
        recordNumber = parkingList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Parking GetParkingByParkingId(int parkingId) {
        Session s = factory.getCurrentSession();
        Parking parking = (Parking)s.get(Parking.class, parkingId);
        return parking;
    }

    /*更新Parking信息*/
    public void UpdateParking(Parking parking) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(parking);
    }

    /*删除Parking信息*/
    public void DeleteParking (int parkingId) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object parking = s.load(Parking.class, parkingId);
        s.delete(parking);
    }

}
