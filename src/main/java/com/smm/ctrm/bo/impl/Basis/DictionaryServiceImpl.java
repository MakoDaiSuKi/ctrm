package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.DictionaryService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Dictionary;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {

	private static final Logger logger = Logger.getLogger(DictionaryServiceImpl.class);

	@Autowired
	private HibernateRepository<Dictionary> repository;

	@Override
	public ActionResult<List<Dictionary>> Dictionaries() {

		List<Dictionary> list = repository.GetQueryable(Dictionary.class).where(DetachedCriteria
				.forClass(Dictionary.class).add(Restrictions.eq("IsHidden", false)).addOrder(Order.desc("Code")))
				.toCacheList();
		return new ActionResult<>(true, "", assemblyList(list));
	}

	@Override
	public ActionResult<List<Dictionary>> BackDictionaries() {

		DetachedCriteria order = DetachedCriteria.forClass(Dictionary.class);

		List<Dictionary> list = this.repository.GetQueryable(Dictionary.class).OrderBy(Order.desc("Code")).toList();

		ActionResult<List<Dictionary>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(assemblyList(list));

		return result;
	}

	@Override
	public ActionResult<Dictionary> GetById(String id) {

		ActionResult<Dictionary> result = new ActionResult<>();

		try {

			Dictionary obj = this.repository.getOneById(id, Dictionary.class);
			assembly(obj);// 装配
			result.setSuccess(true);
			result.setData(obj);

		} catch (Exception e) {

			logger.error(e);

			result.setSuccess(false);
			result.setMessage(e.getMessage());

		}

		return result;
	}

	@Override
	public ActionResult<String> Delete(String id) {

		ActionResult<String> result = new ActionResult<>();

		try {

			this.repository.PhysicsDelete(id, Dictionary.class);

			result.setSuccess(true);
			result.setMessage(MessageCtrm.DeleteSuccess);

		} catch (Exception e) {

			logger.error(e);

			result.setSuccess(false);
			result.setMessage(e.getMessage());
			logger.error(e.getMessage(), e);

		}

		return result;
	}

	@Override
	public ActionResult<Dictionary> Save(Dictionary dictionary) {

		if (dictionary.getParentId() != null && !dictionary.getParentId().trim().equals("")) {

			return this.SaveItem(dictionary);
		}

		return this.SaveCat(dictionary);
	}

	@Override
	public ActionResult<Dictionary> SaveCat(Dictionary dictionary) {

		ActionResult<Dictionary> result = new ActionResult<>();

		// 检查是否已存在
		DetachedCriteria where = DetachedCriteria.forClass(Dictionary.class);
		where.add(Restrictions.or(Restrictions.eq("Name", dictionary.getName()),
				Restrictions.eq("Code", dictionary.getCode())));
		if (dictionary.getId() != null) {
			where.add(Restrictions.ne("Id", dictionary.getId()));
		}
		where.add(Restrictions.isNull("ParentId"));
		List<Dictionary> list = this.repository.GetQueryable(Dictionary.class).where(where).toList();

		if (list != null && list.size() > 0) {

			result.setSuccess(false);
			result.setMessage(MessageCtrm.DuplicatedName);

		} else {
			this.repository.SaveOrUpdate(dictionary);

			result.setSuccess(true);
			result.setMessage(MessageCtrm.SaveSuccess);
		}

		return result;
	}

	@Override
	public ActionResult<Dictionary> SaveItem(Dictionary dictionary) {

		ActionResult<Dictionary> result = new ActionResult<>();

		// 检查是否已存在
		DetachedCriteria where = DetachedCriteria.forClass(Dictionary.class);
		where.add(Restrictions.and(Restrictions.eq("Name", dictionary.getName()),
				Restrictions.eq("Value", dictionary.getValue())));
		if (dictionary.getId() != null) {
			where.add(Restrictions.ne("Id", dictionary.getId()));
		}
		if (dictionary.getParentId() != null) {
			where.add(Restrictions.eq("ParentId", dictionary.getParentId()));
		}
		List<Dictionary> list = this.repository.GetQueryable(Dictionary.class).where(where).toList();

		if (list != null && list.size() > 0) {

			result.setSuccess(false);
			result.setMessage(MessageCtrm.DuplicatedName);

		} else {

			dictionary.setCode(this.repository.getOneById(dictionary.getParentId(), Dictionary.class).getCode());

			this.repository.SaveOrUpdate(dictionary);

			result.setSuccess(true);
			result.setMessage(MessageCtrm.SaveSuccess);
		}

		return result;
	}

	@Override
	public ActionResult<String> DeleteCat(String id) {

		ActionResult<String> result = new ActionResult<>();

		String sql = String.format("Delete from [Basis].Dictionary where ParentId = '%s'", id);

		try {

			this.repository.ExecuteNonQuery(sql);

			this.repository.PhysicsDelete(id, Dictionary.class);

			result.setSuccess(true);
			result.setMessage(MessageCtrm.DeleteSuccess);

		} catch (Exception e) {

			logger.error(e);

			result.setSuccess(false);
			result.setMessage(e.getMessage());

		}

		return result;
	}

	@Override
	public ActionResult<String> DeleteItem(String id) {

		ActionResult<String> result = new ActionResult<>();

		try {

			this.repository.PhysicsDelete(id, Dictionary.class);

			result.setSuccess(true);
			result.setMessage(MessageCtrm.DeleteSuccess);

		} catch (Exception e) {

			logger.error(e);

			result.setSuccess(false);
			result.setMessage(e.getMessage());

		}

		return result;
	}

	@Override
	public ActionResult<List<Dictionary>> Cats() {

		DetachedCriteria where = DetachedCriteria.forClass(Dictionary.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.isNull("ParentId"));

		List<Dictionary> list = this.repository.GetQueryable(Dictionary.class).where(where).OrderBy(Order.desc("Code"))
				.toList();

		ActionResult<List<Dictionary>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(assemblyList(list));

		return result;

	}

	@Override
	public List<Dictionary> Items(String parentId) {

		DetachedCriteria where = DetachedCriteria.forClass(Dictionary.class);
		where.add(Restrictions.eq("ParentId", parentId));

		List<Dictionary> dicList = this.repository.GetQueryable(Dictionary.class).where(where)
				.OrderBy(Order.desc("Code")).toList();

		return assemblyList(dicList);

	}

	@Override
	public List<Dictionary> ItemByCatId(String catId) {

		DetachedCriteria where = DetachedCriteria.forClass(Dictionary.class);
		where.add(Restrictions.eq("ParentId", catId));

		List<Dictionary> list = this.repository.GetQueryable(Dictionary.class).where(where)
				.OrderBy(Order.desc("OrderIndex")).toList();

		if (list == null || list.size() == 0)
			return null;

		return assemblyList(list);
	}

	@Override
	public List<Dictionary> ItemsVisibleOnly() {

		DetachedCriteria where = DetachedCriteria.forClass(Dictionary.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.isNotNull("ParentId"));

		return repository.GetQueryable(Dictionary.class).where(where).OrderBy(Order.desc("OrderIndex")).toList();
	}

	protected List<Dictionary> assemblyList(List<Dictionary> dicList) {

		if (dicList == null || dicList.size() == 0)
			return null;

		for (int i = 0; i < dicList.size(); i++) {
			Dictionary loadParentDic = dicList.get(i);
			assembly(loadParentDic);
			dicList.set(i, loadParentDic);
		}

		return dicList;
	}

	/**
	 * 递归装配
	 * 
	 * @param dictionary
	 */
	protected void assembly(Dictionary dictionary) {
		if (dictionary.getParentId() != null) {
			Dictionary dic = this.repository.getOneById(dictionary.getParentId(), Dictionary.class);
			if (dic != null) {
				dictionary.setParent(dic);
				assembly(dic);
			}
		}
	}

	@Override
	public Dictionary MoveUp(String id) {

		return this.repository.getOneById(id, Dictionary.class);

	}

	@Override
	public Dictionary MoveDown(String id) {

		return this.repository.getOneById(id, Dictionary.class);
	}
}
