package com.smm.ctrm.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Instrument;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.hibernate.DataSource.DataSourceContextHolder;

@org.springframework.stereotype.Controller
@RequestMapping("api")
public class UserController {
	
	@Resource
	HibernateRepository<User> userRepo;
	
	@RequestMapping("myTest")
	@ResponseBody
	public ActionResult<String> test() {
		String userId = "2EEE9FDC-293F-4599-B3B1-A3890184EC57";
		
		DataSourceContextHolder.setDataSourceType("baiyin");
		
//		User u1 = userRepo.getCurrentSession().get(User.class, userId);
		User u1 = userRepo.getOneById(userId, User.class);
		System.out.println(u1 + u1.getName());
		userRepo.getCurrentSession().clear();
		DataSourceContextHolder.setDataSourceType("baiyin4Test");
//		User u2 = userRepo.getCurrentSession().get(User.class, userId);
		User u2 = userRepo.getOneById(userId, User.class);
		System.out.println(u2 + u2.getName());
		return new ActionResult<>(true, "成功了");
	}
	
}
