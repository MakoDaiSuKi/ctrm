package com.smm.ctrm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.hibernate.DataSource.DataSourceContextHolder;
import com.smm.ctrm.util.RefUtil;

@org.springframework.stereotype.Controller
@RequestMapping("api")
public class UserController {

	@Resource
	HibernateRepository<User> userRepo;

	@Resource
	RestTemplate restTemplate;

	@RequestMapping("myTest")
	@ResponseBody
	public ActionResult<String> test() {
		String userId = "2EEE9FDC-293F-4599-B3B1-A3890184EC57";

		DataSourceContextHolder.setDataSourceType("baiyin");

		// User u1 = userRepo.getCurrentSession().get(User.class, userId);
		User u1 = userRepo.getOneById(userId, User.class);
		System.out.println(u1 + u1.getName());
		userRepo.getCurrentSession().clear();
		DataSourceContextHolder.setDataSourceType("baiyin4Test");
		// User u2 = userRepo.getCurrentSession().get(User.class, userId);
		User u2 = userRepo.getOneById(userId, User.class);
		System.out.println(u2 + u2.getName());
		return new ActionResult<>(true, "成功了");
	}

	@RequestMapping("getWatchesPrice")
	@ResponseBody
	public ActionResult<String> getWatchesPrice() {
		String url = "https://www.amazon.com/Seiko-SNE329-Sport-Solar-Powered-Stainless/dp/B00I1KSQ6Q/ref=sr_1_1?ie=UTF8&qid=1477036014&sr=8-1&keywords=sne329";
		url = "https://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords=sne329";
		ResponseEntity<String> pageContent = restTemplate.getForEntity(url, String.class);
		String content = pageContent.getBody();
		final String priceStartStr = "a-size-base a-color-price s-price a-text-bold\">$";
		List<String> priceList = getListByTag(content, priceStartStr, "<");
		final String itemNameStartStr = "a-link-normal s-access-detail-page  a-text-normal\" title=\"";
		List<String> itemNameList = getListByTag(content, itemNameStartStr, "\"");
		return new ActionResult<>(true, "成功了", priceList.toString() + itemNameList.toString(), new RefUtil());
	}
	private List<String> getListByTag(String content, final String startTag, final String endTag) {
		restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		List<String> priceList = new ArrayList<>();
		int startIndex = 0;
		final int maxItemCount = 5;
		int index = 0;
		while(true) {
			startIndex = content.indexOf(startTag, startIndex);
			if(startIndex == -1) {
				break;
			}
			startIndex += startTag.length();
			int endIndex = content.indexOf(endTag, startIndex);
			String price = content.substring(startIndex, endIndex);
			priceList.add(price);
			if(index >= maxItemCount) {
				break;
			}
			index++;
		}
		return priceList;
	}

}
