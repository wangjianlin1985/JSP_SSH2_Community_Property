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
import com.chengxusheji.domain.Facility;

@Service @Transactional
public class FacilityDAO {

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

    /*添加Facility信息*/
    public void AddFacility(Facility facility) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(facility);
    }

    /*查询Facility信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Facility> QueryFacilityInfo(String name,String startTime,String facilityState,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Facility facility where 1=1";
    	if(!name.equals("")) hql = hql + " and facility.name like '%" + name + "%'";
    	if(!startTime.equals("")) hql = hql + " and facility.startTime like '%" + startTime + "%'";
    	if(!facilityState.equals("")) hql = hql + " and facility.facilityState like '%" + facilityState + "%'";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List facilityList = q.list();
    	return (ArrayList<Facility>) facilityList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Facility> QueryFacilityInfo(String name,String startTime,String facilityState) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Facility facility where 1=1";
    	if(!name.equals("")) hql = hql + " and facility.name like '%" + name + "%'";
    	if(!startTime.equals("")) hql = hql + " and facility.startTime like '%" + startTime + "%'";
    	if(!facilityState.equals("")) hql = hql + " and facility.facilityState like '%" + facilityState + "%'";
    	Query q = s.createQuery(hql);
    	List facilityList = q.list();
    	return (ArrayList<Facility>) facilityList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Facility> QueryAllFacilityInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From Facility";
        Query q = s.createQuery(hql);
        List facilityList = q.list();
        return (ArrayList<Facility>) facilityList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String name,String startTime,String facilityState) {
        Session s = factory.getCurrentSession();
        String hql = "From Facility facility where 1=1";
        if(!name.equals("")) hql = hql + " and facility.name like '%" + name + "%'";
        if(!startTime.equals("")) hql = hql + " and facility.startTime like '%" + startTime + "%'";
        if(!facilityState.equals("")) hql = hql + " and facility.facilityState like '%" + facilityState + "%'";
        Query q = s.createQuery(hql);
        List facilityList = q.list();
        recordNumber = facilityList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Facility GetFacilityByFacilityId(int facilityId) {
        Session s = factory.getCurrentSession();
        Facility facility = (Facility)s.get(Facility.class, facilityId);
        return facility;
    }

    /*更新Facility信息*/
    public void UpdateFacility(Facility facility) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(facility);
    }

    /*删除Facility信息*/
    public void DeleteFacility (int facilityId) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object facility = s.load(Facility.class, facilityId);
        s.delete(facility);
    }

}
