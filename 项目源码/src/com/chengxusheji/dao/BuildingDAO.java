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
import com.chengxusheji.domain.Building;

@Service @Transactional
public class BuildingDAO {

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

    /*添加Building信息*/
    public void AddBuilding(Building building) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(building);
    }

    /*查询Building信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Building> QueryBuildingInfo(int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Building building where 1=1";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List buildingList = q.list();
    	return (ArrayList<Building>) buildingList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Building> QueryBuildingInfo() { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Building building where 1=1";
    	Query q = s.createQuery(hql);
    	List buildingList = q.list();
    	return (ArrayList<Building>) buildingList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Building> QueryAllBuildingInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From Building";
        Query q = s.createQuery(hql);
        List buildingList = q.list();
        return (ArrayList<Building>) buildingList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber() {
        Session s = factory.getCurrentSession();
        String hql = "From Building building where 1=1";
        Query q = s.createQuery(hql);
        List buildingList = q.list();
        recordNumber = buildingList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Building GetBuildingByBuildingId(int buildingId) {
        Session s = factory.getCurrentSession();
        Building building = (Building)s.get(Building.class, buildingId);
        return building;
    }

    /*更新Building信息*/
    public void UpdateBuilding(Building building) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(building);
    }

    /*删除Building信息*/
    public void DeleteBuilding (int buildingId) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object building = s.load(Building.class, buildingId);
        s.delete(building);
    }

}
