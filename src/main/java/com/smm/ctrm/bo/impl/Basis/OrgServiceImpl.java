package com.smm.ctrm.bo.impl.Basis;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.OrgService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Division;
import com.smm.ctrm.domain.Basis.Org;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MD5Util;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class OrgServiceImpl implements OrgService {


    private static final Logger logger=Logger.getLogger(OrgServiceImpl.class);

    @Autowired
    private HibernateRepository<Org> repository;


    @Autowired
    private HibernateRepository<User> userHibernateRepository;


    @Autowired
    private HibernateRepository<Division> divisionHibernateRepository;


    @Override
    public ActionResult<List<Org>> Orgs() {

        List<Org> list=this.repository.GetQueryable(Org.class).OrderBy(Order.desc("Code")).toList();

        ActionResult<List<Org>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<Org> Save(Org org) {

        if(org==null) return new ActionResult<>(false,"org is null");

        //检查重复
        DetachedCriteria where=DetachedCriteria.forClass(Org.class);
        where.add(Restrictions.eq("Name", org.getName()));
        where.add(Restrictions.eq("Code", org.getCode()));
        where.add(Restrictions.ne("Id", org.getId()));
        List<Org> orgs=this.repository.GetQueryable(Org.class).where(where).toList();

        if(orgs!=null && orgs.size()>0) return new ActionResult<>(false, MessageCtrm.DuplicatedName);

        try{
        	
        	if(org.getId()!=null){
        		
        		String orgId=this.repository.SaveOrUpdateRetrunId(org);

                //创建默认管理员帐号
                User user=new User();
                user.setOrgId(orgId);
                user.setAccount("admin");
                user.setName("管理员");
                user.setPassword(MD5Util.MD5("61283393"));
                user.setCreatedAt(new Date());
                user.setCreatedBy("shanggu");

                this.userHibernateRepository.SaveOrUpdate(user);

                //创建默认的组织机构树的根节点
                Division division=new Division();
                division.setName(org.getName());
                division.setCode(org.getCode());
                division.setOrgId(org.getId());
                division.setParent(null);

                this.divisionHibernateRepository.SaveOrUpdate(division);
                return new ActionResult<>(true,MessageCtrm.SaveSuccess);
        	}else{
        		this.repository.SaveOrUpdateRetrunId(org);
        		return new ActionResult<>(true,MessageCtrm.SaveSuccess);
        	}
        }catch (Exception e){

        	logger.error(e.getMessage(), e);
        	return new ActionResult<>(false,e.getMessage());
           
        }

    }

    @Override
    public ActionResult<String> Delete(String id) {

        ActionResult<String> result= new ActionResult<>();

        try{

            String sql = String.format("delete from [Basis].Division where OrgId = '%s'", id);
            repository.ExecuteNonQuery(sql);

            sql = String.format("delete from [Basis].Users where OrgId = '%s'", id);
            repository.ExecuteNonQuery(sql);

            //不存在关联数据时才允许删除。如果仍然存在关联数据，则由ex抛出
            repository.PhysicsDelete(id, Org.class);

            result.setSuccess(true);
            result.setMessage(MessageCtrm.DeleteSuccess);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }

    @Override
    public ActionResult<Org> GetById(String id) {

        ActionResult<Org> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id, Org.class));

        return result;
    }

	@Override
	public Criteria GetCriteria() {

        return this.repository.CreateCriteria(Org.class);
		
	}

	@Override
	public List<Org> Orgs(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy,
			String orderBy) {
		
		return this.repository.GetPage(criteria, pageSize, pageIndex, sortBy, orderBy, total).getData();
	}
}
