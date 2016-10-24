package com.smm.ctrm.bo.impl.Maintain;

import com.smm.ctrm.bo.Maintain.DevelopService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.*;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class DevelopServiceImpl implements DevelopService {

    private static final Logger logger=Logger.getLogger(DevelopServiceImpl.class);

    @Autowired
    private HibernateRepository<Menu> mRepository;

    @Autowired
    private HibernateRepository<Button> bRepository;

    @Autowired
    private HibernateRepository<Resource> rRepository;

    public Menu assembly(Menu menu) {
    	if(menu!=null&&org.apache.commons.lang3.StringUtils.isNoneBlank(menu.getParentId())){
    		Menu parentMenu=this.mRepository.getOneById(menu.getParentId(), Menu.class);
    		if(parentMenu!=null){
    			menu.setParent(parentMenu);
    			assembly(parentMenu);
    		}
    	}
		return menu;
	}

    @Override
    public ActionResult<Menu> MenuById(String id) {

        ActionResult<Menu> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(assembly(this.mRepository.getOneById(id,Menu.class)));

        return result;
    }

    @Override
    public Button ButtonById(String id) {

        return this.bRepository.getOneById(id,Button.class);
    }

    @Override
    public ActionResult<List<Menu>> Menus() {

        String Lang="zh-CN";

        DetachedCriteria where=DetachedCriteria.forClass(Menu.class);
        where.add(Restrictions.eq("Lang", Lang));

        List<Menu> list=this.mRepository.GetQueryable(Menu.class).where(where).OrderBy(Order.desc("OrderIndex")).toList();

        ActionResult<List<Menu>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public Button SaveButton(Button button) {

        if(StringUtils.isEmpty(button.getId()) && button.getOrderIndex()==0){

            DetachedCriteria where=DetachedCriteria.forClass(Button.class);
            where.add(Restrictions.eq("ParentId", button.getParentId()));

            List<Button> list=this.bRepository.GetQueryable(Button.class).where(where).OrderBy(Order.desc("OrderIndex")).toList();

            if(list!=null && list.size()>0){

                button.setOrderIndex(list.get(0).getOrderIndex()+1);

            }else{

                return null;
            }

        }

        this.bRepository.SaveOrUpdate(button);

        return button;
    }

    @Override
    public ActionResult<Menu> SaveMenu(Menu menu) {


        ActionResult<Menu> result= new ActionResult<>();

        try{

            if(StringUtils.isEmpty(menu.getId()) && menu.getOrderIndex()==0 ){

                DetachedCriteria where=DetachedCriteria.forClass(Menu.class);
                where.add(Restrictions.eq("ParentId", menu.getParentId()));

                List<Menu> list=this.mRepository.GetQueryable(Menu.class).where(where).OrderBy(Order.desc("OrderIndex")).toList();

                if(list!=null && list.size()>0){

                    menu.setOrderIndex(list.get(0).getOrderIndex()+1);

                }else{

                    throw new Exception("数据不存在");
                }

            }


            this.mRepository.SaveOrUpdate(menu);

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
    public ActionResult<String> DeleteMenu(String id) {

        ActionResult<String> result= new ActionResult<>();

        try{

            this.mRepository.PhysicsDelete(id,Menu.class);

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
    public ActionResult<String> DeleteResources(List<Resource> resources) {

        ActionResult<String> result= new ActionResult<>();

        try{

            if(resources!=null && resources.size()>0){

                resources.forEach(r-> this.rRepository.PhysicsDelete(r.getId(),Resource.class));
            }

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
    public List<Resource> Resources() {


        List<Resource> resourceList=new ArrayList<>();

        String Lang="zh-CN";

        DetachedCriteria where=DetachedCriteria.forClass(Menu.class);
        where.add(Restrictions.eq("Lang", Lang));

        List<Menu> menuList=this.mRepository.GetQueryable(Menu.class).where(where).OrderBy(Order.asc("OrderIndex")).toList();


        menuList.forEach(m->{

            Resource r=new Resource();

            try {
            	ConvertUtils.register(new SqlDateConverter(null), java.util.Date.class);
            	BeanUtils.copyProperties(r, m);
            } catch (Exception e) {
                e.printStackTrace();
            }

            r.setResType(Resource.TYPE_Menu);

            resourceList.add(r);
        });


        where=DetachedCriteria.forClass(Button.class);
        where.add(Restrictions.eq("Lang", Lang));

        List<Button> buttonList=this.bRepository.GetQueryable(Button.class).where(where).OrderBy(Order.asc("OrderIndex")).toList();


        buttonList.forEach(b->{

            Resource r=new Resource();

            try {
            	ConvertUtils.register(new SqlDateConverter(null), java.util.Date.class);
            	BeanUtils.copyProperties(r, b);
            } catch (Exception e) {
                e.printStackTrace();
            }

            r.setResType(Resource.TYPE_Button);

            resourceList.add(r);
        });



        return resourceList;
    }
}
