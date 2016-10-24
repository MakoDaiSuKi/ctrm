package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.UserSetService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Basis.UserSet;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class UserSetServiceImpl implements UserSetService {

	@Autowired
	private HibernateRepository<UserSet> repository;

	@Autowired
	private HibernateRepository<User> userRepository;

	@Override
	public ActionResult<UserSet> Save(UserSet userSet) {

		this.repository.SaveOrUpdate(userSet);
		return new ActionResult<>(true, MessageCtrm.SaveSuccess, userSet);
	}

	@Override
	public ActionResult<UserSet> MyUserSet(String userId) {

		DetachedCriteria where = DetachedCriteria.forClass(UserSet.class);
		where.add(Restrictions.eq("UserId", userId));

		List<UserSet> list = this.repository.GetQueryable(UserSet.class).where(where).toCacheList();

		ActionResult<UserSet> result = new ActionResult<>();

		result.setSuccess(true);

		if (list != null && list.size() > 0) {
			UserSet userSet = list.get(0);
			if (userSet.getUserId() != null) {
				userSet.setUser(this.userRepository.getOneById(userSet.getUserId(), User.class));
			}
			result.setData(userSet);
		}

		return result;
	}
}
