package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.PresetService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Preset;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class PresetServiceImpl implements PresetService {


    private static final Logger logger=Logger.getLogger(PresetServiceImpl.class);

    @Autowired
    private HibernateRepository<Preset> repository;

    @Override
    public ActionResult<List<Preset>> Presets() {

        DetachedCriteria where=DetachedCriteria.forClass(Preset.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<Preset> list=this.repository.GetQueryable(Preset.class).where(where).toList();

        ActionResult<List<Preset>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<List<Preset>> BackPresets() {

        List<Preset> list=this.repository.GetQueryable(Preset.class).toList();

        ActionResult<List<Preset>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<Preset> SavePreset(Preset preset) {

        ActionResult<Preset> result= new ActionResult<>();

        try{

            this.repository.SaveOrUpdate(preset);

            result.setSuccess(true);
            //result.setData(preset);
            result.setMessage(MessageCtrm.SaveSuccess);
        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }

    @Override
    public ActionResult<Preset> DeletePreset(String id) {

        ActionResult<Preset> result= new ActionResult<>();

        try{

            this.repository.PhysicsDelete(id, Preset.class);

            result.setSuccess(true);
            result.setMessage(MessageCtrm.DeleteSuccess);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }
}
