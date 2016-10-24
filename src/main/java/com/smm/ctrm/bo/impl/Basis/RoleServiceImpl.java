package com.smm.ctrm.bo.impl.Basis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smm.ctrm.bo.Basis.RoleService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Resource;
import com.smm.ctrm.domain.Basis.Role;
import com.smm.ctrm.domain.Basis.RoleMenu;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Basis.UserRole;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class RoleServiceImpl implements RoleService {


    private static final Logger logger=Logger.getLogger(RoleServiceImpl.class);

    @Autowired
    private HibernateRepository<Role> repository;

    @Autowired
    private HibernateRepository<UserRole> userRoleRepository;

    @Autowired
    private HibernateRepository<RoleMenu> roleMenuRepository;

    @Autowired
    private HibernateRepository<Resource> resourceHibernateRepository;


    @Override
    public ActionResult<List<Role>> Roles() {

        List<Role> list=this.repository.GetQueryable(Role.class).OrderBy(Order.asc("Name")).toList();

        ActionResult<List<Role>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<Role> Save(Role role) {

        ActionResult<Role> result= new ActionResult<>();

        try{

            //判断重复
            DetachedCriteria where=DetachedCriteria.forClass(Role.class);
            where.add(Restrictions.eq("Name", role.getName()));
            where.add(Restrictions.ne("Id",role.getId()));

            List<Role> list=this.repository.GetQueryable(Role.class).where(where).toList();

            if(list!=null && list.size()>0) throw new Exception(MessageCtrm.DuplicatedName);

            this.repository.SaveOrUpdate(role);

            result.setSuccess(true);
            result.setData(role);
            result.setMessage(MessageCtrm.SaveSuccess);
        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }

    @Override
    public ActionResult<String> Delete(String roleId) {

        ActionResult<String> result= new ActionResult<>();

        try{

            //1. 删除user - role的记录
            String sql = String.format("Delete from [Basis].UserRole where RoleId = '%s'", roleId);
            repository.ExecuteNonQuery(sql);
            //2. 删除role - menu的记录
            sql = String.format("Delete from [Basis].RoleMenu where RoleId = '%s'", roleId);
            repository.ExecuteNonQuery(sql);

            //删除自身
            repository.PhysicsDelete(roleId, Role.class);

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
    public ActionResult<Role> GetById(String roleId) {

        ActionResult<Role> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(roleId, Role.class));

        return result;
    }

    @Override
    public ActionResult<Role> GetByIdDetail(String roleId) {

        Role role=this.repository.getOneById(roleId, Role.class);

        if(role!=null){

            DetachedCriteria where=DetachedCriteria.forClass(UserRole.class);
            where.add(Restrictions.eq("RoleId", roleId));

            List<UserRole> userRoles=this.userRoleRepository.GetQueryable(UserRole.class).where(where).toList();

            if(userRoles!=null) role.setUsers(userRoles.stream().map(UserRole::getUser).collect(Collectors.toList()));

            if(role.getResources()==null) role.setResources(new ArrayList<>());

            where=DetachedCriteria.forClass(RoleMenu.class);
            where.add(Restrictions.eq("RoleId", roleId));
            List<RoleMenu> roleMenus=this.roleMenuRepository.GetQueryable(RoleMenu.class).where(where).toList();

            if(roleMenus!=null){

                roleMenus.forEach(roleMenu -> {

                    Resource resource=this.resourceHibernateRepository.getOneById(roleMenu.getResourceId(),Resource.class);

                    if(resource!=null){

                    	if(role.getResources().stream().allMatch(r-> !r.getId().equals(resource.getId()))){
                    		role.getResources().add(resource);
                    	}
                    }
                });
            }

        }

        return new ActionResult<>(true,null,role);
    }

    @Override
    public ActionResult<Role> GetByIdIncUsers(String roleId) {

        Role role=this.repository.getOneById(roleId, Role.class);

        if(role==null) return new ActionResult<>(true,null,null);

        DetachedCriteria where=DetachedCriteria.forClass(UserRole.class);
        where.add(Restrictions.eq("RoleId", roleId));

        List<UserRole> userRoleList=this.userRoleRepository.GetQueryable(UserRole.class).where(where).toList();

        List<User> users=userRoleList.stream().map(UserRole::getUser).collect(Collectors.toList());

        role.setUsers(users);

        return new ActionResult<>(true,null,role);
    }

    @Override
    public ActionResult<Role> GetByIdIncMenus(String roleId) {

        Role role=this.repository.getOneById(roleId, Role.class);

        if(role==null) return new ActionResult<>(true,null,null);

        if(role.getResources()==null) role.setResources(new ArrayList<>());


        DetachedCriteria where=DetachedCriteria.forClass(RoleMenu.class);
        where.add(Restrictions.eq("RoleId", roleId));
        List<RoleMenu>  roleMenus=this.roleMenuRepository.GetQueryable(RoleMenu.class).where(where).toList();

        if(roleMenus!=null && roleMenus.size()>0){

            roleMenus.forEach(roleMenu -> {

                Resource resource=this.resourceHibernateRepository.getOneById(roleMenu.getResourceId(),Resource.class);

                if(resource!=null && role.getResources().stream().allMatch(r-> !r.getId().equals(resource.getId()))){

                    role.getResources().add(resource);

                }

            });
        }

        return new ActionResult<>(true,null,role);
    }

	@Override
	public ActionResult<Role> AllocateUsers2Role(Role role) {

        ActionResult<Role> result= new ActionResult<>();

        try{

            String sql = String.format("Delete from [Basis].UserRole where RoleId = '%s'", role.getId());
            repository.ExecuteNonQuery(sql);

            if(role.getUsers()!=null){

                role.getUsers().forEach(user -> {

                    UserRole userRole=new UserRole();

                    userRole.setCreatedAt(new Date());
                    userRole.setRoleId(role.getId());
                    userRole.setUserId(user.getId());

                    this.userRoleRepository.SaveOrUpdate(userRole);
                });
            }

            result.setSuccess(true);
            result.setData(role);
            result.setMessage(MessageCtrm.SaveSuccess);
        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
	}

	@Override
	public ActionResult<Role> AllocateMenus2Role(Role role) {


        ActionResult<Role> result= new ActionResult<>();

        try{

            //先删除以前的role - menu的对应关系
            String sql = String.format("Delete from [Basis].RoleMenu where RoleId = '%s'", role.getId());
            repository.ExecuteNonQuery(sql);

            //保存新的role - menu的对应关系
            if(role.getResources()!=null){

            	for (Resource resource : role.getResources()) {
            		
            		RoleMenu menu=new RoleMenu();
                    menu.setCreatedAt(new Date());
                    menu.setRoleId(role.getId());
                    menu.setResourceId(resource.getId());

                    if(Resource.TYPE_Menu.equals(resource.getResType())) 
                    	menu.setMenuId(resource.getId());

                    if(Resource.TYPE_Button.equals(resource.getResType())) 
                    	menu.setButtonId(resource.getId());

                    this.roleMenuRepository.SaveOrUpdate(menu);
				}
            }

            result.setSuccess(true);
            result.setMessage(MessageCtrm.SaveSuccess);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
	}
}
