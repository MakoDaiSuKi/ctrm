package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.AreaService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Area;
import com.smm.ctrm.domain.Basis.Division;
import com.smm.ctrm.domain.Basis.Org;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class AreaServiceImpl implements AreaService {

    private static final Logger logger=Logger.getLogger(AreaServiceImpl.class);

    @Autowired
    private HibernateRepository<Area> repository;
    
    @Autowired
    private HibernateRepository<User> userRepository;


    @Override
    public ActionResult<List<Area>> Areas() {

        DetachedCriteria where=DetachedCriteria.forClass(Area.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<Area> list=this.repository.GetQueryable(Area.class).where(where).OrderBy(Order.desc("OrderIndex")).toList();

        ActionResult<List<Area>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(assemblingBeanList(list));

        return result;

    }

    @Override
    public ActionResult<List<Area>> BackAreas() {


        DetachedCriteria where=DetachedCriteria.forClass(Area.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<Area> list=this.repository.GetQueryable(Area.class).where(where).toList();

        ActionResult<List<Area>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(assemblingBeanList(list));

        return result;

    }

    @Override
    public ActionResult<Area> Save(Area area) {

        ActionResult<Area> result= new ActionResult<>();

        try{

        	DetachedCriteria dc=DetachedCriteria.forClass(Area.class);
        	dc.add(Restrictions.eq("Name", area.getName()));
        	if(area.getParentId()!=null){
        		dc.add(Restrictions.eq("ParentId", area.getParentId()));
        	}else{
        		dc.add(Restrictions.isNull("ParentId"));
        	}
        	if(area.getId()!=null){
        		dc.add(Restrictions.ne("Id", area.getId()));
        	}else{
        		dc.add(Restrictions.isNotNull("Id"));
        	}
        	
        	List<Area> areas=this.repository.GetQueryable(Area.class).where(dc).toList();
        	if(areas!=null&&areas.size()>0){
        		return new ActionResult<>(false, MessageCtrm.DuplicatedName);
        	}
            this.repository.SaveOrUpdate(area);

            result.setSuccess(true);
            result.setMessage(MessageCtrm.SaveSuccess);
        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;

    }

    @Override
    public ActionResult<String> Delete(String id) {


        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.PhysicsDelete(id, Area.class);

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
    public ActionResult<Area> GetById(String id) {

        ActionResult<Area> result= new ActionResult<>();

        try{

            Area area=this.repository.getOneById(id, Area.class);
            assemblingBean(area);
            result.setSuccess(true);
            result.setData(area);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }

	@Override
	public void MoveUp(String id) {

	}

	@Override
	public void MoveDown(String id) {

	}
	
	/**
     * 以下是打掉关系
     */
    public List<Area> assemblingBeanList(List<Area> ct){
    	if(ct.size()==0)return null;
    	for (Area area : ct) {
    		assemblingBean(area);
		}
    	return ct;
	}
	
	public void assemblingBean(Area ct){
		if(ct!=null){
			/**
			 * 组装Parent
			 */
			Area parentArea=null;
			if(ct.getParentId()!=null){
				parentArea=this.repository.getOneById(ct.getParentId(),Area.class);
				ct.setParent(parentArea);
			}
			/**
			 * 组装User
			 */
			if(ct.getCreatedId()!=null){
				User created=this.userRepository.getOneById(ct.getCreatedId(), User.class);
				ct.setCreated(created);
			}
			/**
			 * 组装User
			 */
			if(ct.getUpdatedId()!=null){
				User updated=this.userRepository.getOneById(ct.getUpdatedId(), User.class);
				ct.setUpdated(updated);
			}
			
			/**
			 * 不进行递归只取一级，注销这三行代码即可
			 */
			if(parentArea!=null){
				assemblingBean(parentArea);
			}
		}
	}

}
