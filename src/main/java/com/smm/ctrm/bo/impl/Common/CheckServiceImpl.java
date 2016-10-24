package com.smm.ctrm.bo.impl.Common;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.hibernate.TableNameConst;

@Service
public class CheckServiceImpl implements CheckService {
	
	@Autowired
	private HibernateRepository<User> repository;
	
	@Override
	public ActionResult<String> deletable(String value, String column, List<String> tableList) {
		String msg = "";
		boolean isSuccess = Boolean.TRUE;
		for(String table : tableList) {
			String sql = "select count(1) from " + table + " where " + column + " = '" + value +"'";
			Integer count = (Integer) repository.getCurrentSession().createNativeQuery(sql).getSingleResult();
			if(count > 0) {
				if(StringUtils.isBlank(msg)) {
					isSuccess = Boolean.FALSE;
					msg += "存在关联数据：\r\n";
				}
				msg += "\r\n" + TableNameConst.getTableNameCustomer(table, column, value, repository.getCurrentSession());
			}
		}
		return new ActionResult<>(isSuccess, msg);
		
	}
}
