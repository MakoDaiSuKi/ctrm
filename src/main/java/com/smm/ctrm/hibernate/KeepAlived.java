package com.smm.ctrm.hibernate;

import org.springframework.beans.factory.annotation.Autowired;

import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.User;

public class KeepAlived implements KeepAlivedService{

	@Autowired
	private HibernateRepository<User> userRepo;

	@Override
	public void keepAlivedMethod() {
		User user = null;
		try{
//			user = userRepo.getOneById("52FE236D-6B08-4007-8820-2E5FFDDC20FC", User.class);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("调度" + user);
	}

}
