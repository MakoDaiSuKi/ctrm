package com.smm.ctrm.bo.impl.Finance;

import com.smm.ctrm.bo.Finance.BankBalanceService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Account;
import com.smm.ctrm.domain.Basis.CustomerBank;
import com.smm.ctrm.domain.Physical.BankBalance;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class BankBalanceServiceImpl implements BankBalanceService {

    private static final Logger logger=Logger.getLogger(BankBalanceServiceImpl.class);

    @Autowired
    private HibernateRepository<BankBalance> repository;

	@Override
	public ActionResult<BankBalance> Save(List<BankBalance> bankBalances) {

        ActionResult<BankBalance> result= new ActionResult<>();

        try{

            if(bankBalances==null || bankBalances.size()==0) throw new Exception("没有任何数据");

            for (BankBalance balance : bankBalances) {
				this.repository.SaveOrUpdate(balance);
			}
            result.setSuccess(true);
            result.setMessage(MessageCtrm.SaveSuccess);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
	}

	@Override
	public ActionResult<String> Delete(String id) {

        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.PhysicsDelete(id,BankBalance.class);

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
	public ActionResult<BankBalance> GetById(String id) {

        ActionResult<BankBalance> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id,BankBalance.class));

        return result;
	}

	/**
	 * /// 根据指定日期，返回该天的银行余额的集合
	 */
	@Override
	public ActionResult<List<BankBalance>> GetBankBalancesByTradeDate(Date dt) {

        DetachedCriteria where=DetachedCriteria.forClass(BankBalance.class);
        where.add(Restrictions.ge("TradeDate", dt));
        where.add(Restrictions.le("TradeDate", dt));

        List<BankBalance> list=this.repository.GetQueryable(BankBalance.class).where(where).toList();

        ActionResult<List<BankBalance>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
	}

	@Override
	public List<BankBalance> BankBalances() {

        return repository.GetList(BankBalance.class);
	}

	@Override
	public Criteria GetCriteria() {
		return this.repository.CreateCriteria(BankBalance.class);
	}

	@Override
	public List<BankBalance> BankBalances(Criteria param, Integer pageSize, Integer pageIndex, RefUtil total,
			String orderBy, String orderSort) {
		return this.repository.GetPage(param, pageSize, pageIndex, orderBy, orderSort, total).getData();
	}

}
