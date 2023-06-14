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
import com.chengxusheji.domain.Owner;
import com.chengxusheji.domain.Fee;

@Service @Transactional
public class FeeDAO {

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

    /*添加Fee信息*/
    public void AddFee(Fee fee) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(fee);
    }

    /*查询Fee信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Fee> QueryFeeInfo(FeeType feeTypeObj,Owner ownerObj,String feeDate,String feeContent,String opUser,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Fee fee where 1=1";
    	if(null != feeTypeObj && feeTypeObj.getTypeId()!=0) hql += " and fee.feeTypeObj.typeId=" + feeTypeObj.getTypeId();
    	if(null != ownerObj && ownerObj.getOwnerId()!=0) hql += " and fee.ownerObj.ownerId=" + ownerObj.getOwnerId();
    	if(!feeDate.equals("")) hql = hql + " and fee.feeDate like '%" + feeDate + "%'";
    	if(!feeContent.equals("")) hql = hql + " and fee.feeContent like '%" + feeContent + "%'";
    	if(!opUser.equals("")) hql = hql + " and fee.opUser like '%" + opUser + "%'";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List feeList = q.list();
    	return (ArrayList<Fee>) feeList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Fee> QueryFeeInfo(FeeType feeTypeObj,Owner ownerObj,String feeDate,String feeContent,String opUser) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Fee fee where 1=1";
    	if(null != feeTypeObj && feeTypeObj.getTypeId()!=0) hql += " and fee.feeTypeObj.typeId=" + feeTypeObj.getTypeId();
    	if(null != ownerObj && ownerObj.getOwnerId()!=0) hql += " and fee.ownerObj.ownerId=" + ownerObj.getOwnerId();
    	if(!feeDate.equals("")) hql = hql + " and fee.feeDate like '%" + feeDate + "%'";
    	if(!feeContent.equals("")) hql = hql + " and fee.feeContent like '%" + feeContent + "%'";
    	if(!opUser.equals("")) hql = hql + " and fee.opUser like '%" + opUser + "%'";
    	Query q = s.createQuery(hql);
    	List feeList = q.list();
    	return (ArrayList<Fee>) feeList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Fee> QueryAllFeeInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From Fee";
        Query q = s.createQuery(hql);
        List feeList = q.list();
        return (ArrayList<Fee>) feeList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(FeeType feeTypeObj,Owner ownerObj,String feeDate,String feeContent,String opUser) {
        Session s = factory.getCurrentSession();
        String hql = "From Fee fee where 1=1";
        if(null != feeTypeObj && feeTypeObj.getTypeId()!=0) hql += " and fee.feeTypeObj.typeId=" + feeTypeObj.getTypeId();
        if(null != ownerObj && ownerObj.getOwnerId()!=0) hql += " and fee.ownerObj.ownerId=" + ownerObj.getOwnerId();
        if(!feeDate.equals("")) hql = hql + " and fee.feeDate like '%" + feeDate + "%'";
        if(!feeContent.equals("")) hql = hql + " and fee.feeContent like '%" + feeContent + "%'";
        if(!opUser.equals("")) hql = hql + " and fee.opUser like '%" + opUser + "%'";
        Query q = s.createQuery(hql);
        List feeList = q.list();
        recordNumber = feeList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Fee GetFeeByFeeId(int feeId) {
        Session s = factory.getCurrentSession();
        Fee fee = (Fee)s.get(Fee.class, feeId);
        return fee;
    }

    /*更新Fee信息*/
    public void UpdateFee(Fee fee) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(fee);
    }

    /*删除Fee信息*/
    public void DeleteFee (int feeId) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object fee = s.load(Fee.class, feeId);
        s.delete(fee);
    }

}
