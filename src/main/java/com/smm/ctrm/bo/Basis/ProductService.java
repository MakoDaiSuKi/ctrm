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
public interface ProductService {
	ActionResult<List<Product>> ProductsByCommodityId(String commodityId);
	ActionResult<List<Product>> Products();
	ActionResult<List<Product>> BackProducts();
	ActionResult<Product> Save(Product product);
	ActionResult<String> Delete(String id);
	ActionResult<Product> GetById(String id);
	void MoveUp(String id);
	void MoveDown(String id);
	void AddProduct(Product product);

}