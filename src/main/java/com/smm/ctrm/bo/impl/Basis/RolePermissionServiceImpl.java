package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.RolePermissionService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.RolePermission;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

@Service
public class RolePermissionServiceImpl implements RolePermissionService{

	@Autowired
	private HibernateRepository<RolePermission> repository; 
	
	
	@Override
	public ActionResult<String> Save(List<RolePermission> rolePermission) {
		
		for (RolePermission item : rolePermission) {
			DetachedCriteria dc=DetachedCriteria.forClass(RolePermission.class);
			dc.add(Restrictions.eq("RoleId", item.getRoleId()));
			dc.add(Restrictions.eq("SubRoleId", item.getSubRoleId()));
			List<RolePermission> list=this.repository.GetQueryable(RolePermission.class).where(dc).toList();
			if(list!=null&&list.size()>0){
				RolePermission p=list.get(0);
				p.setIsQuery(item.getIsQuery());
				p.setIsUpdate(item.getIsUpdate());
			}else{
				this.repository.SaveOrUpdate(item);
			}
		}
		return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess);
	}

	/**
	 * 根据角色ID获取下级的角色列表
	 */
	@Override
	public ActionResult<List<RolePermission>> GetByRoleId(String id) {
		DetachedCriteria dc=DetachedCriteria.forClass(RolePermission.class);
		dc.add(Restrictions.eq("RoleId", id));
		List<RolePermission> rolePermission = this.repository.GetQueryable(RolePermission.class).where(dc).toList();
        return new ActionResult<List<RolePermission>>(Boolean.TRUE,"查询成功",rolePermission);
	}
}
