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
import com.chengxusheji.domain.FeeType;

@Service @Transactional
public class FeeTypeDAO {

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

    /*添加FeeType信息*/
    public void AddFeeType(FeeType feeType) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(feeType);
    }

    /*查询FeeType信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<FeeType> QueryFeeTypeInfo(int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From FeeType feeType where 1=1";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List feeTypeList = q.list();
    	return (ArrayList<FeeType>) feeTypeList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<FeeType> QueryFeeTypeInfo() { 
    	Session s = factory.getCurrentSession();
    	String hql = "From FeeType feeType where 1=1";
    	Query q = s.createQuery(hql);
    	List feeTypeList = q.list();
    	return (ArrayList<FeeType>) feeTypeList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<FeeType> QueryAllFeeTypeInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From FeeType";
        Query q = s.createQuery(hql);
        List feeTypeList = q.list();
        return (ArrayList<FeeType>) feeTypeList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber() {
        Session s = factory.getCurrentSession();
        String hql = "From FeeType feeType where 1=1";
        Query q = s.createQuery(hql);
        List feeTypeList = q.list();
        recordNumber = feeTypeList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public FeeType GetFeeTypeByTypeId(int typeId) {
        Session s = factory.getCurrentSession();
        FeeType feeType = (FeeType)s.get(FeeType.class, typeId);
        return feeType;
    }

    /*更新FeeType信息*/
    public void UpdateFeeType(FeeType feeType) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(feeType);
    }

    /*删除FeeType信息*/
    public void DeleteFeeType (int typeId) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object feeType = s.load(FeeType.class, typeId);
        s.delete(feeType);
    }

}
