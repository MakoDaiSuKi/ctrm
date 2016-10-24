package com.smm.ctrm.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.FlushModeType;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateTemplate;

import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.Queryable;
import com.smm.ctrm.util.RefUtil;

/**
 * Created by hao.zheng on 2016/4/26.
 *
 */
public  interface  HibernateRepository<T extends HibernateEntity> {

	/** 强制同步模式
	 * @param flushMode
	 */
	void setFlushMode(FlushMode flushMode);

	/** 强制同步模式
	 * @return
	 */
	FlushMode getFlushMode();

    /**
     * 强制同步
     */
    void Flush();

    /**
     * 清空一级缓存
     */
    void Clear();

    /** 清除指定对象的以及缓存
     * @param obj
     */
    void Evict(T obj);

    /**
     * 开始一个事务
     */
    void BeginTransaction();

    /**
     * 开始一个事务，指定隔离级别
     * @param isolationLevel
     */
    void BeginTransaction(Long isolationLevel);


    /**
     * 提交事务
     */
    void CommitTransaction();


    /**
     * 回滚事务
     */
    void RollbackTransaction();

    /**
     * 查询所有
     * @return
     */
    List<T> GetList(Class<T> clazz);
    
    /**
     * 查询所有(带Criteria)
     * @param criteria
     * @return
     */
    List<T> GetList(Criteria criteria);

    /**
     * 保存
     * @param obj
     */
    void Save(T obj);


    /**
     * 更新或者新增
     * @param obj
     * @return
     */
    String SaveOrUpdateRetrunId(T obj);


    /**
     * 更新或者新增
     * @param obj
     * @throws Exception 
     */
    void SaveOrUpdate(T obj);

    void Merge(T obj);


    Queryable<T> GetQueryable(Class<T> clazz);

    /**
     * 物理删除
     * @param id
     */
    void PhysicsDelete(String id, Class<T> clazz);


    /**
     * 获取一个对象
     * @param id
     * @return
     */
    T getOneById(String id, Class<T> clazz);
    
    /**
     * 直接执行sql语句
     * @param sql
     */
    boolean ExecuteNonQuery(String sql);

    HibernateTemplate getHibernateTemplate();

	/** 创建过滤条件
	 * @param clazz
	 * @return
	 */
	Criteria CreateCriteria(Class<T> clazz);


    List<T> ExecuteSql(String sql,Class<T> clazz);

    /**
     * 分页查询
     * @param criteria
     * @param pageSize
     * @param pageIndex
     * @param sortBy        排序字段
     * @param orderBy       asc  或者 desc
     * @param total         记录总数对象
     * @return
     */
    ActionResult<List<T>> GetPage(Criteria criteria, int pageSize, int pageIndex, String sortBy, String orderBy, RefUtil total);

    /** 执行存储过程
     *  返回二维list结构（需要自行映射到对象）
     * @param procName
     * @param parameters
     * @return
     */
    List<Object[]> ExecuteProcedureSql (String procName, Map<String, Object> parameters);

	void PhysicsDelete(T t);
	
	Map<String, Object> ExecuteProcedureSqlOutMap(String procName, Map<String, Object> parameters,Map<String, String> outParame);

	List<T> ExecuteCorrectlySql(String sql, Class<T> clazz);

	void SaveForJob(T obj);

	Session getCurrentSession();

	List<String> ExecuteCorrectlySql2(String sql);

	void ExecuteProcedureSql2(String procName, Map<String, Object> parameters);

	void ExecuteCorrectlySql3(String sql);

	Object ExecuteScalarSql(String sql);

	List<String> ExecuteCorrectlySql4(String sql,String scalar);

	List<T> ExecuteHql(String hql, Map<String, Object> params, Class<T> clazz);
	
	int ExecuteHql2Update(String hql, Map<String, Object> params);

}
