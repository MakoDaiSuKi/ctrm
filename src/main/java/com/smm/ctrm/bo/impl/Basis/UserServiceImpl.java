package com.smm.ctrm.bo.impl.Basis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.UserService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Button;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.Division;
import com.smm.ctrm.domain.Basis.Menu;
import com.smm.ctrm.domain.Basis.Org;
import com.smm.ctrm.domain.Basis.Resource;
import com.smm.ctrm.domain.Basis.Role;
import com.smm.ctrm.domain.Basis.RoleMenu;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Basis.UserRole;
import com.smm.ctrm.domain.Basis.UserSet;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MD5Util;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class UserServiceImpl implements UserService {


    private static final Logger logger=Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private HibernateRepository<RoleMenu> roleMenuRepository;

    @Autowired
    private HibernateRepository<Resource> resourceRepository;

    @Autowired
    private HibernateRepository<UserRole> repository;

    @Autowired
    private HibernateRepository<User> userRepository;

    @Autowired
    private HibernateRepository<Role> roleRepository;

    @Autowired
    private HibernateRepository<UserSet> userSetHibernateRepository;
    
    @Autowired
    private HibernateRepository<Division> divisionRepo;
    
    @Autowired
    private HibernateRepository<Org> orgHibernateRepository;
    
    @Autowired
    private HibernateRepository<Menu> menuHibernateRepository;
    
    @Autowired
    private HibernateRepository<Button> buttonHibernateRepository;
    

    @Override
    public ActionResult<List<Role>> GetRolesByUserId(String userId) {

        DetachedCriteria where=DetachedCriteria.forClass(UserRole.class);
        where.add(Restrictions.eq("UserId", userId));

        List<UserRole> list=this.repository.GetQueryable(UserRole.class).where(where).toList();

        List<Role> roleList=new ArrayList<>();

        list.forEach(userRole -> roleList.add(userRole.getRole()));

        ActionResult<List<Role>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(roleList);

        return result;
    }

    @Override
    public ActionResult<List<User>> GetUsersByRoleId(String roleId) {

        DetachedCriteria where=DetachedCriteria.forClass(UserRole.class);
        where.add(Restrictions.eq("RoleId", roleId));

        List<UserRole> list=this.repository.GetQueryable(UserRole.class).where(where).toList();

        List<User> userList=new ArrayList<>();

        list.forEach(userRole -> userList.add(userRole.getUser()));

        ActionResult<List<User>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(assemblingBeanList(userList));

        return result;
    }

    @Override
    public ActionResult<List<Resource>> GetMenusByUserId(String userId) {

        ActionResult<List<Resource>> result=new ActionResult<>();

        List<Resource> resourceList=new ArrayList<>();

        try{

            DetachedCriteria where=DetachedCriteria.forClass(UserRole.class);
            where.add(Restrictions.eq("UserId", userId));

            List<UserRole> list=this.repository.GetQueryable(UserRole.class).where(where).toList();

            list.forEach(userRole -> {

                if(!StringUtils.isEmpty(userRole.getRoleId())){

                    DetachedCriteria temp_where=DetachedCriteria.forClass(RoleMenu.class);
                    temp_where.add(Restrictions.eq("RoleId", userRole.getRoleId()));

                    List<RoleMenu> menuList=this.roleMenuRepository.GetQueryable(RoleMenu.class).where(temp_where).toList();

                    menuList.forEach(roleMenu -> {

                        Resource resource=this.resourceRepository.getOneById(roleMenu.getResourceId(), Resource.class);

                        if(resource!=null && !resourceList.contains(resource)){
                        	resourceList.add(resource);
                        }
                    });
                }
            });


            result.setSuccess(true);
            result.setData(resourceList);

        }catch (Exception e){

        	logger.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }
    
    @Override
	public List<User> Pager(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy,
			String orderBy) {
    	return this.userRepository.GetPage(criteria, pageSize, pageIndex, sortBy, orderBy, total).getData();
	}

    @Override
    public ActionResult<List<User>> Users() {
    	
        List<User> list=this.userRepository.GetQueryable(User.class).OrderBy(Order.desc("Account")).toCacheList();

        ActionResult<List<User>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(assemblingBeanList(list));

        return result;
    }

    @Override
    public ActionResult<List<User>> Traders() {

        DetachedCriteria where=DetachedCriteria.forClass(User.class);
        where.add(Restrictions.eq("IsTrader", true));

        List<User> list=this.userRepository.GetQueryable(User.class).where(where).OrderBy(Order.desc("Account")).toCacheList();

        ActionResult<List<User>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(assemblingBeanList(list));

        return result;
    }

    @Override
    public ActionResult<List<User>> ApproversOfContract() {

        ActionResult<List<User>> result=new ActionResult<>();

        DetachedCriteria where=DetachedCriteria.forClass(Role.class);
        where.add(Restrictions.eq("Name", "订单审核组"));

        List<Role> list=this.roleRepository.GetQueryable(Role.class).where(where).toCacheList();

        if(list==null || list.size()==0) {

            result.setSuccess(false);
            result.setMessage("找不到指定的角色");

            return result;
        }

        return this.GetUsersByRoleId(list.get(0).getId());

    }

    @Override
    public ActionResult<List<User>> ApproversOfPayment() {


        ActionResult<List<User>> result=new ActionResult<>();

        DetachedCriteria where=DetachedCriteria.forClass(Role.class);
        where.add(Restrictions.eq("Name", "付款审核组"));

        List<Role> list=this.roleRepository.GetQueryable(Role.class).where(where).toCacheList();

        if(list==null || list.size()==0) {

            result.setSuccess(false);
            result.setMessage("找不到指定的角色");

            return result;
        }

        return this.GetUsersByRoleId(list.get(0).getId());
    }

    @Override
    public ActionResult<List<User>> ApproversOfCustomer() {

        ActionResult<List<User>> result=new ActionResult<>();

        DetachedCriteria where=DetachedCriteria.forClass(Role.class);
        where.add(Restrictions.eq("Name", "客户审核组"));

        List<Role> list=this.roleRepository.GetQueryable(Role.class).where(where).toCacheList();

        if(list==null || list.size()==0) {

            result.setSuccess(false);
            result.setMessage("找不到指定的角色");

            return result;
        }

        return this.GetUsersByRoleId(list.get(0).getId());
    }

    @Override
    public ActionResult<User> GetById(String userId) {

        User user=this.userRepository.getOneById(userId, User.class);

        ActionResult<User> result=new ActionResult<>();

        if(user==null){

            result.setSuccess(false);
            result.setMessage("用户不存在");

            return result;
        }
        
        //组装部门和机构
        
        assemblingBean(user);
        //重新加载role

        if(user.getRoles()==null) user.setRoles(new ArrayList<>());

        DetachedCriteria where=DetachedCriteria.forClass(UserRole.class);
        where.add(Restrictions.eq("UserId", user.getId()));

        List<UserRole> list=this.repository.GetQueryable(UserRole.class).where(where).toList();

        list.forEach(userRole -> user.getRoles().add(this.roleRepository.getOneById(userRole.getRoleId(), Role.class)));

        //返回
        result.setSuccess(true);
        result.setData(user);

        return result;
    }

    @Override
    public ActionResult<User> Save(User user) {

        try{

            if(user==null) throw new Exception("user is null");

            //检查重复
            DetachedCriteria where=DetachedCriteria.forClass(User.class);
            if(user.getId()!=null){
            	 where.add(Restrictions.ne("Id", user.getId()));
            }else{
            	where.add(Restrictions.isNotNull("Id"));
            }
            where.add(Restrictions.or(
                    Restrictions.eq("Name",user.getName()),
                    Restrictions.eq("Account",user.getAccount())
            ));

            List<User> users=this.userRepository.GetQueryable(User.class).where(where).toList();

            if(users!=null && users.size()>0) throw new Exception("同名用户已存在");

            //设置密码
            if(StringUtils.isEmpty(user.getId())){
                user.setPassword(MD5Util.MD5("123456"));

            }else{
            	User oldUser=this.userRepository.getOneById(user.getId(),User.class);
                user.setPassword(oldUser.getPassword());
            }

            //保存并返回id
            String userId=this.userRepository.SaveOrUpdateRetrunId(user);

            //先删除以前的user - role的对应关系
            where=DetachedCriteria.forClass(UserRole.class);
            where.add(Restrictions.eq("UserId", userId));

            List<UserRole> userRoles=this.repository.GetQueryable(UserRole.class).where(where).toList();

            if(userRoles!=null) userRoles.forEach(userRole -> this.repository.PhysicsDelete(userRole.getId(),UserRole.class));


            //保存新的user - role的对应关系
            if(user.getRoles()!=null){

                user.getRoles().forEach(role -> {
                    UserRole userRole=new UserRole();
                    userRole.setCreatedAt(new Date());
                    userRole.setUserId(userId);
                    userRole.setRoleId(role.getId());
                    this.repository.SaveOrUpdate(userRole);
                });
            }


            //检查是否保存了用户的个性化配置
            UserSet userSet=this.userSetHibernateRepository.getOneById(userId,UserSet.class);

            if(userSet==null){

                userSet=new UserSet();

                userSet.setUserId(userId);
                userSet.setCreatedAt(new Date());
                userSet.setCreatedBy(user.getCreatedBy());

                userSet.setGridRows(20);
                this.userSetHibernateRepository.SaveOrUpdate(userSet);
            }

            return new ActionResult<>(true,MessageCtrm.SaveSuccess,user);

        }catch (Exception e){
        	logger.error(e.getMessage(), e);
        	return new ActionResult<>(false,e.getMessage());
        }

    }

    @Override
    public ActionResult<String> Delete(String userId) {

        ActionResult<String> result= new ActionResult<>();

        try{

            //1. 删除UserRole的记录
            String sql = String.format("Delete from [Basis].UserRole where UserId = '%s'", userId);
            repository.ExecuteNonQuery(sql);

            //2. 删除UserSet的记录
            sql = String.format("Delete from [Basis].UserSet where UserId = '%s'", userId);
            repository.ExecuteNonQuery(sql);

            //3. 删除自身
            sql = String.format("Delete from [Basis].Users where Id = '%s'", userId);
            repository.ExecuteNonQuery(sql);


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
    public ActionResult<String> ChangePassword(String userId, String oldPwd, String newPwd) {

        ActionResult<String> result= new ActionResult<>();

        try{
        	
        	User user=this.userRepository.getOneById(userId, User.class);
        	
            if(user==null) throw new Exception("用户不存在");

            if(!user.getPassword().equals(MD5Util.MD5(oldPwd))) throw new Exception("原密码不正确");

            user.setPassword(MD5Util.MD5(newPwd));

            this.userRepository.SaveOrUpdate(user);

            result.setSuccess(true);
            result.setMessage(MessageCtrm.UpdateSuccess);


        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }


        return result;
    }

    @Override
    public ActionResult<String> ResetPassword(String account,String userId, String newpwd) {


        ActionResult<String> result= new ActionResult<>();

        try{

        	DetachedCriteria dc=DetachedCriteria.forClass(User.class);
        	dc.add(Restrictions.eq("Account", account));
        	
        	User admin=this.userRepository.GetQueryable(User.class).where(dc).firstOrDefault();
        	if(admin != null && admin.getIsSysAdmin()){
        		//系统管理员
        	}else{
        		//非系统管理员判断账号
        		if(!account.toLowerCase().equals("admin") && !account.toLowerCase().equals("supervisor")){

                    throw new Exception("没有权限重置密码。");
                }
        	}
            User user=this.userRepository.getOneById(userId, User.class);

            if(user==null) throw new Exception("用户不存在！");

            user.setPassword(MD5Util.MD5(newpwd));

            this.userRepository.SaveOrUpdate(user);

            result.setSuccess(true);
            result.setMessage("密码已经重置。");


        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }


        return result;
    }

	@Override
	public ActionResult<List<User>> AccountsToBeAudit4App() {

        DetachedCriteria where=DetachedCriteria.forClass(User.class);
        where.add(Restrictions.eq("IsAudited", false));

        List<User> list=this.userRepository.GetQueryable(User.class).where(where).OrderBy(Order.desc("Account")).toList();

        ActionResult<List<User>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
	}

	@Override
	public ActionResult<String> AuditAccount4App(String accountId, String isAudited) {

        ActionResult<String> result= new ActionResult<>();

        try{

            User user=this.userRepository.getOneById(accountId, User.class);

            if(user==null) throw new Exception("用户不存在");

            user.setIsAudited(Boolean.parseBoolean(isAudited));

            //更新用户
            this.userRepository.SaveOrUpdate(user);

            result.setSuccess(true);
            result.setMessage("Audit succesfully.");


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
    public List<User> assemblingBeanList(List<User> ct){
    	if(ct.size()==0)return null;
    	for (User user : ct) {
    		assemblingBean(user);
		}
    	return ct;
	}
	
	public void assemblingBean(User ct){
		if(ct!=null){
			/**
			 * 组装Parent
			 */
			Division parentDiv=null;
			if(ct.getDivisionId()!=null){
				parentDiv=this.divisionRepo.getOneById(ct.getDivisionId(),Division.class);
				ct.setDivision(parentDiv);
			}
			/**
			 * 组装Org
			 */
			if(ct.getOrgId()!=null){
				Org org=this.orgHibernateRepository.getOneById(ct.getOrgId(), Org.class);
				ct.setOrg(org);
			}
		}
	}
	public void assemblingBean(RoleMenu ct){
		if(ct!=null){
			/**
			 * 组装Menu
			 */
			if(ct.getMenuId()!=null){
				Menu mueu=this.menuHibernateRepository.getOneById(ct.getMenuId(),Menu.class);
				ct.setMenu(mueu);
			}
			/**
			 * 组装Button
			 */
			if(ct.getButtonId()!=null){
				Button bt=this.buttonHibernateRepository.getOneById(ct.getButtonId(), Button.class);
				ct.setButton(bt);
			}
			
			/**
			 * 组装Role
			 */
			if(ct.getRoleId()!=null){
				Role rl=this.roleRepository.getOneById(ct.getRoleId(), Role.class);
				ct.setRole(rl);
			}
		}
	}

	@Override
	public ActionResult<List<User>> ApproversOfShip() {
		DetachedCriteria dc=DetachedCriteria.forClass(Role.class);
		dc.add(Restrictions.eq("Name", "发货单审核组"));
		Role role = this.roleRepository.GetQueryable(Role.class).where(dc).firstOrDefault();
        if (role == null)
            return new ActionResult<List<User>>(Boolean.FALSE,"找不到指定的角色");
        return GetUsersByRoleId(role.getId());
	}

	@Override
	public ActionResult<List<User>> ApproversOfInvoice() {
		
		DetachedCriteria dc=DetachedCriteria.forClass(Role.class);
		dc.add(Restrictions.eq("Name", "发票审核组"));
		Role role = this.roleRepository.GetQueryable(Role.class).where(dc).firstOrDefault();
        if (role == null)
            return new ActionResult<List<User>>(Boolean.FALSE,"找不到指定的角色");
        return GetUsersByRoleId(role.getId());
	}

	@Override
	public Criteria GetCriteria() {
		return this.userRepository.CreateCriteria(User.class);
	}
}
