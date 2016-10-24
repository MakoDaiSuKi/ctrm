package com.smm.ctrm.bo.Basis;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.hibernate.Criteria;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.domain.Basis.*;
import com.smm.ctrm.domain.Maintain.*;
import com.smm.ctrm.domain.Physical.*;
import com.smm.ctrm.domain.Report.*;
public interface MenuService {
	ActionResult<Menu> GetById(String id);
	ActionResult<List<Menu>> Menus(String account);
	ActionResult<Menu> Save(Menu menu);
	ActionResult<String> Delete(String id);
	ActionResult<String> DeleteMenus(List<Menu> menus);

}