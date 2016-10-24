package com.smm.ctrm.bo.impl.SchedulerJobs;

import com.smm.ctrm.bo.SchedulerJobs.CommonScheduleService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Physical.Position;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class CommonScheduleServiceImpl implements CommonScheduleService {

    private static final Logger logger=Logger.getLogger(CommonScheduleServiceImpl.class);

    @Autowired
    private HibernateRepository<Position> repository;

	@Override
	public void UpdateInventory() {

        try
        {
            this.repository.BeginTransaction();

            Position storage = this.repository.getOneById("83114AB2-21DB-4CD6-ABA3-A42100EF1D46",Position.class);

            if (storage == null) return;

            storage.setQuantity(storage.getQuantity().add(new BigDecimal(5)));

            this.repository.SaveOrUpdate(storage);

            this.repository.CommitTransaction();
        }
        catch (Exception e)
        {

            logger.error(e);

            this.repository.RollbackTransaction();
        }
		
	}

}
