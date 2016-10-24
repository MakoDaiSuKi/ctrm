package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.MenuService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Division;
import com.smm.ctrm.domain.Basis.Menu;
import com.smm.ctrm.domain.Basis.Org;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger logger=Logger.getLogger(MenuServiceImpl.class);

    @Autowired
    private HibernateRepository<Menu> repository;


    @Override
    public ActionResult<Menu> GetById(String id) {

        ActionResult<Menu> result= new ActionResult<>();
        Menu menu=this.repository.getOneById(id, Menu.class);
        assemblingBean(menu);
        result.setSuccess(true);
        result.setData(menu);

        return result;
    }

    @Override
    public ActionResult<List<Menu>> Menus(String account) {

        //系统语言类别
        String Lang="zh-cn";

        DetachedCriteria where=DetachedCriteria.forClass(Menu.class);
        where.add(Restrictions.eq("Lang", Lang));
        where.add(Restrictions.eq("IsDevOnly", false));

        List<Menu> list=this.repository.GetQueryable(Menu.class).where(where).OrderBy(Order.desc("OrderIndex")).toList();

        ActionResult<List<Menu>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(assemblingBeanList(list));

        return result;
    }

    @Override
    public ActionResult<Menu> Save(Menu menu) {


        ActionResult<Menu> result= new ActionResult<>();

        try{

            //检查 menu id
            if(StringUtils.isEmpty(menu.getId()) && menu.getOrderIndex() ==0){


                DetachedCriteria where=DetachedCriteria.forClass(Menu.class);
                where.add(Restrictions.eq("ParentId", menu.getParentId()));

                List<Menu> list=this.repository.GetQueryable(Menu.class).where(where).OrderBy(Order.desc("OrderIndex")).toList();

                if(list==null || list.size()==0) throw new Exception("菜单id为空 ，对应数据记录也为空");

                //设置菜单 index
                menu.setOrderIndex(list.get(0).getOrderIndex()+1);
            }


            this.repository.SaveOrUpdate(menu);

            result.setSuccess(true);
            result.setData(menu);
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

            this.repository.PhysicsDelete(id, Menu.class);

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
    public ActionResult<String> DeleteMenus(List<Menu> menus) {

        ActionResult<String> result= new ActionResult<>();

        try{

            menus.forEach(menu -> this.Delete(menu.getId()));

            result.setSuccess(true);
            result.setMessage(MessageCtrm.DeleteSuccess);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }
    
    
    /**
     * 以下是打掉关系
     */
    public List<Menu> assemblingBeanList(List<Menu> ct){
    	if(ct.size()==0)return null;
    	for (Menu menu : ct) {
    		assemblingBean(menu);
		}
    	return ct;
	}
	
	public void assemblingBean(Menu ct){
		if(ct!=null){
			/**
			 * 组装Parent
			 */
			Menu pMenu=null;
			if(ct.getParentId()!=null){
				pMenu=this.repository.getOneById(ct.getParentId(),Menu.class);
				ct.setParent(pMenu);
			}
			/**
			 * 组装Children
			 */
			DetachedCriteria dc=DetachedCriteria.forClass(Menu.class);
			dc.add(Restrictions.eq("ParentId", ct.getId()));
			List<Menu> childrenDiv=this.repository.GetQueryable(Menu.class).where(dc).toList();
			ct.setChildren(childrenDiv);
			
			if(pMenu!=null){
				assemblingBean(pMenu);
			}
		}
	}
}
