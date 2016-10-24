package com.smm.probase.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smm.ctrm.util.URLUtil;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.smm.ctrm.dao.ResourceDao;
import com.smm.ctrm.domain.Basis.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单元测试 idea
 * 
 * @author zengshihua
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:dispatcher-servlet.xml", "classpath:spring-hibernate.xml" })
public class JunitTest {


    public static final  String host="http://localhost:8080/ctrm/";


	// @Autowired
	// UserService userService;

	@Autowired
	ResourceDao resourceDao;

	@javax.annotation.Resource
	SessionFactory sf;

	@Test
	public void queryUserInfo() {
		try {
			// AcctUser user=userService.get("123");
			/*
			 * Set<AcctRole> role=user.getAcctRoles(); Iterator<AcctRole> it =
			 * role.iterator(); while(it.hasNext()){ AcctRole a=it.next();
			 * System.out.println(a.getName()); } System.out.println(role);
			 */

			/*
			 * long s=System.currentTimeMillis();
			 * System.out.println(System.currentTimeMillis()); List<AcctUser>
			 * listAcctUser=userService.findAll(); long
			 * e=System.currentTimeMillis(); System.out.println(e-s);
			 */
			System.out.println(sf);
			Resource r = resourceDao.get("7540377d-5495-4265-8aab-014ddb288ecc");
			System.out.println(r.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}




    //获取授权码
    public static String getToken() {

        String url=host+"/api/Auth/Login/Login4Winform";

        Map<String,String> user = new HashMap<>();

        user.put("Account","zhenghao");
        user.put("Password","123456");
        user.put("orgName","baiyin");

        String result = URLUtil.post(url,user,null);

        //json转换
        JSONObject object = JSON.parseObject(result);

        JSONObject data = (JSONObject) object.get("Data");

        System.out.println(data);

        String token = data.getString("AuthToken");

//        String orgName = data.getString("OrgName");

//        System.out.println("token:"+token);

        return token;

    }
}
