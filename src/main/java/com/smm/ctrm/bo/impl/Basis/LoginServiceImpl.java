package com.smm.ctrm.bo.impl.Basis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.LoginService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Button;
import com.smm.ctrm.domain.Basis.Menu;
import com.smm.ctrm.domain.Basis.Resource;
import com.smm.ctrm.domain.Basis.RoleMenu;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Basis.UserRole;
import com.smm.ctrm.domain.Basis.UserSet;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.BeanUtils;
import com.smm.ctrm.util.MD5Util;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class LoginServiceImpl implements LoginService {

	private static final Logger logger = Logger.getLogger(LoginServiceImpl.class);

	@Autowired
	private HibernateRepository<UserSet> repository;

	@Autowired
	private HibernateRepository<User> userRepository;

	@Autowired
	private HibernateRepository<UserRole> userRoleHibernateRepository;

	@Autowired
	private HibernateRepository<RoleMenu> roleMenuHibernateRepository;

	@Autowired
	private HibernateRepository<Menu> menuHibernateRepository;

	@Override
	public ActionResult<UserSet> GetUserSettings(String userId) {

		DetachedCriteria where = DetachedCriteria.forClass(UserSet.class);
		where.add(Restrictions.eq("UserId", userId));

		List<UserSet> list = this.repository.GetQueryable(UserSet.class).where(where).toCacheList();

		UserSet set = null;

		if (list != null && list.size() > 0)
			set = list.get(0);

		ActionResult<UserSet> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(set);

		return result;
	}

	@Override
	public ActionResult<User> Login(String act, String pwd) {
		// 检查参数
		if (StringUtils.isBlank(act) || StringUtils.isBlank(pwd)) {
			return new ActionResult<>(false, "帐号密码不能为空");
		}
		// 检查用户是否存在
		User user = getUserByAccountAndPassword(act, pwd);
		if (user == null) {
			return new ActionResult<>(false, "帐号或密码错误");
		}
		// 检查帐号是否被锁定
		if (user.getIsLocked()) {
			return new ActionResult<>(false, "该帐号已被锁定");
		}
		return new ActionResult<>(true, "", user);
	}

	private User getUserByAccountAndPassword(String act, String pwd) {
		DetachedCriteria where = DetachedCriteria.forClass(User.class);
		where.add(Restrictions.eq("Account", act.toLowerCase()));
		where.add(Restrictions.eq("Password", MD5Util.MD5(pwd)));
		return this.userRepository.GetQueryable(User.class).where(where).firstOrDefault();
	}

	@Override
	public void RemoveSubMenus(List<Menu> baseMenus, List<Resource> resources, Menu menu) {

		if (menu.getChildren() == null || menu.getChildren().size() == 0) {
			boolean b = resources.stream()
					.allMatch(x -> !menu.getId().equals(x.getParentId()) && !x.getId().equals(menu.getId()));
			if (b) {
				baseMenus.remove(menu);
			}
			return;
		}

		for (int i = menu.getChildren().size() - 1; i >= 0; i--) {
			RemoveSubMenus(menu.getChildren(), resources, menu.getChildren().get(i));
		}

		if (menu.getChildren() == null || menu.getChildren().size() == 0) {
			boolean b = resources.stream()
					.allMatch(x -> !menu.getId().equals(x.getParentId()) && !x.getId().equals(menu.getId()));
			if (b) {
				baseMenus.remove(menu);
			}
			return;
		}

	}

	@Override
	public Menu SimplifyData(Menu menu) {
		if (menu != null) {
			BeanUtils.simple(menu);
		}
		return menu;
	}

	@Override
	public Button SimplifyData(Button button) {
		if (button != null) {
			BeanUtils.simple(button);
		}
		return button;
	}

	@Override
	public List<Menu> GetMenusByUserId(String userId, String account) {

		List<Resource> resources = new ArrayList<>();

		List<UserRole> userRoles = this.userRoleHibernateRepository.GetQueryable(UserRole.class)
				.where(DetachedCriteria.forClass(UserRole.class).add(Restrictions.eq("UserId", userId))).toCacheList();

		for (UserRole userRole : userRoles) {
			if (userRole == null || userRole.getRoleId() == null)
				continue;
			List<RoleMenu> roleMenus = this.roleMenuHibernateRepository.GetQueryable(RoleMenu.class).where(
					DetachedCriteria.forClass(RoleMenu.class).add(Restrictions.eq("RoleId", userRole.getRoleId())))
					.toCacheList();
			for (RoleMenu roleMenu : roleMenus) {
				if (roleMenu.getResourceId() == null)
					continue;
				boolean notInResource = resources.stream().anyMatch(r -> r.getId().equals(roleMenu.getResourceId()));
				if (notInResource)
					continue;
				if (StringUtils.isNotBlank(roleMenu.getMenuId())) {
					if (roleMenu.getMenu() != null) {
						resources.add(roleMenu.getMenu());
					}
				} else if (StringUtils.isNotBlank(roleMenu.getButtonId())) {
					if (roleMenu.getButton() != null) {
						resources.add(roleMenu.getButton());
					}
				}

			}
		}
		String lang = "zh-CN";
		List<Menu> wholeMenus = this.menuHibernateRepository.GetQueryable(Menu.class).where(DetachedCriteria
				.forClass(Menu.class).add(Restrictions.eq("Lang", lang)).add(Restrictions.eq("IsForbidden", false)))
				.OrderBy(Order.asc("OrderIndex")).toCacheList();

		if (wholeMenus == null || wholeMenus.size() == 0)
			return null;

		List<Menu> topMenus = wholeMenus.stream().filter(menu -> StringUtils.isEmpty(menu.getParentId()))
				.collect(Collectors.toList());

		// 获取最全的菜单
		for (Menu top : topMenus) {
			GetSubMenus(wholeMenus, top);
		}

		for (int i = topMenus.size() - 1; i >= 0; i--) {
			RemoveSubMenus(topMenus, resources, topMenus.get(i));
		}
		List<Menu> menus = topMenus.stream().filter(menu -> menu.getChildren() != null && menu.getChildren().size() > 0)
				.collect(Collectors.toList());
		return menus;
	}

	private void GetSubMenus(List<Menu> menus, Menu menu) {

		if (menu == null || menus == null || menus.size() == 0)
			return;

		menu.setChildren(menus.stream().filter(p -> menu.getId().equals(p.getParentId())).collect(Collectors.toList()));
		if (menu.getChildren() == null || menu.getChildren().size() == 0)
			return;
		for (Menu child : menu.getChildren()) {
			GetSubMenus(menus, child);
		}
	}

	@Override
	public List<Button> GetButtonsByUserId(String userId, String account) {

		List<Button> buttons = new ArrayList<>();
		List<UserRole> userRoles = this.userRoleHibernateRepository.GetQueryable(UserRole.class)
				.where(DetachedCriteria.forClass(UserRole.class).add(Restrictions.eq("UserId", userId))).toCacheList();

		for (UserRole userRole : userRoles) {
			if (StringUtils.isEmpty(userRole.getRoleId()))
				continue;
			List<RoleMenu> roleMenus = this.roleMenuHibernateRepository.GetQueryable(RoleMenu.class).where(
					DetachedCriteria.forClass(RoleMenu.class).add(Restrictions.eq("RoleId", userRole.getRoleId())))
					.toCacheList();
			for (RoleMenu roleMenu : roleMenus) {
				if (StringUtils.isEmpty(roleMenu.getButtonId()))
					continue;
				if (buttons.stream().anyMatch(button -> roleMenu.getButtonId().equalsIgnoreCase(button.getId())))
					continue;
				if (roleMenu.getButton() == null)
					continue;
				if (roleMenu.getButton().getIsForbidden())
					continue;
				buttons.add(roleMenu.getButton());
			}
		}
		return buttons;
	}
}
