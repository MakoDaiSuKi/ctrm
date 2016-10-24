package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.ProductService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Product;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger=Logger.getLogger(ProductServiceImpl.class);

    @Autowired
    private HibernateRepository<Product> repository;

    @Override
    public ActionResult<List<Product>> ProductsByCommodityId(String commodityId) {

        DetachedCriteria where=DetachedCriteria.forClass(Product.class);
        where.add(Restrictions.eq("IsHidden", false));
        where.add(Restrictions.eq("CommodityId", commodityId));

        List<Product> list=this.repository.GetQueryable(Product.class).where(where).OrderBy(Order.desc("OrderIndex")).toList();

        ActionResult<List<Product>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<List<Product>> Products() {

        DetachedCriteria where=DetachedCriteria.forClass(Product.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<Product> list=this.repository.GetQueryable(Product.class).where(where).OrderBy(Order.desc("OrderIndex")).toCacheList();

        ActionResult<List<Product>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<List<Product>> BackProducts() {

        List<Product> list=this.repository.GetQueryable(Product.class).OrderBy(Order.desc("OrderIndex")).toList();

        ActionResult<List<Product>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<Product> Save(Product product) {
    	
        try{
        	DetachedCriteria dc = DetachedCriteria.forClass(Product.class);
        	if(StringUtils.isNotBlank(product.getId())) {
        		dc.add(Restrictions.ne("Id", product.getId()));
        	}
        	dc.add(Restrictions.eq("CommodityId", product.getCommodityId()));
        	dc.add(Restrictions.or(Restrictions.eq("Name", product.getName()),
        			Restrictions.eq("Code", product.getCode())));
        	
        	
        	Product p = repository.GetQueryable(Product.class).where(dc).firstOrDefault();
        	if(p != null) return new ActionResult<>(Boolean.FALSE, MessageCtrm.DuplicatedName);

            this.repository.SaveOrUpdate(product);

            return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess);
        }catch (Exception e){
            logger.error(e);
            return new ActionResult<>(Boolean.FALSE, e.getMessage());
        }
    }

    @Override
    public ActionResult<String> Delete(String id) {

        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.PhysicsDelete(id, Product.class);

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
    public ActionResult<Product> GetById(String id) {

        ActionResult<Product> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id, Product.class));

        return result;
    }

	@Override
	public void MoveUp(String id) {

	}

	@Override
	public void MoveDown(String id) {

	}

	@Override
	public void AddProduct(Product product) {

        DetachedCriteria where=DetachedCriteria.forClass(Product.class);
        where.add(Restrictions.eq("Id", product.getId()));
        where.add(Restrictions.eq("CommodityId", product.getCommodityId()));
        where.add(Restrictions.or(Restrictions.eq("Name",product.getName()),Restrictions.eq("Code",product.getCode())));

        List<Product> list=this.repository.GetQueryable(Product.class).where(where).toList();

        Product prod=null;

        if(list!=null && list.size()>0) prod=list.get(0);

        if(prod!=null) this.repository.SaveOrUpdate(prod);
		
	}

}
