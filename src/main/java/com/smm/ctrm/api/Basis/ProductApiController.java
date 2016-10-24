
package com.smm.ctrm.api.Basis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.ProductService;
import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.domain.Basis.Product;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.hibernate.TableNameConst;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Basis/Product/")
public class ProductApiController {

	@Autowired
	private ProductService productService;

	@Resource
	private CheckService checkService;

	/**
	 * 返回指定品种的产品列表
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("ProductsByCommodityId")
	@ResponseBody
	public ActionResult<List<Product>> ProductsByCommodityId(HttpServletRequest request, @RequestBody String id) {
		try {
			return productService.ProductsByCommodityId(id);
		} catch (Exception ex) {
			ActionResult<List<Product>> tempVar = new ActionResult<List<Product>>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 不含分页的列表，包括全部产品的列表
	 * 
	 * @return
	 */
	@RequestMapping("Products")
	@ResponseBody
	public ActionResult<List<Product>> Products(HttpServletRequest request) {
		return productService.Products();
	}

	/**
	 * 不含分页的列表(显示/隐藏)
	 * 
	 * @return
	 */
	@RequestMapping("BackProducts")
	@ResponseBody
	public ActionResult<List<Product>> BackProducts(HttpServletRequest request) {
		return productService.BackProducts();
	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return productService.GetById(id);
		} catch (Exception ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 根据Id删除单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String id) {
		try {

			List<String> tableList = new ArrayList<>();
			tableList.add(TableNameConst.Contract);
			tableList.add(TableNameConst.Storage);
			Product product = productService.GetById(id).getData();
			ActionResult<String> checkResult = checkService.deletable(product.getName(), "Product", tableList);
			if (!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}

			return productService.Delete(id);
		} catch (Exception ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param product
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Product> Save(HttpServletRequest request, @RequestBody Product product) {
		if (product.getId() != null) {
			product.setUpdatedAt(new Date());
			product.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
		} else {
			product.setCreatedAt(new Date());
			product.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
		}

		return productService.Save(product);
	}

	/**
	 * 指定控件向上移动
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("MoveUp")
	@ResponseBody
	public ActionResult<String> MoveUp(HttpServletRequest request, @RequestBody String id) {
		if (StringUtils.isBlank(id)) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("is can not be empty");
			return tempVar;
		}

		try {
			productService.MoveUp(id);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setStatus(ActionStatus.SUCCESS);
			return tempVar2;
		} catch (Exception ex) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setStatus(ActionStatus.ERROR);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}

	/**
	 * 指定控件向下移动
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("MoveDown")
	@ResponseBody
	public ActionResult<String> MoveDown(HttpServletRequest request, @RequestBody String id) {
		if (StringUtils.isBlank(id)) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("is can not be empty");
			return tempVar;
		}

		try {
			productService.MoveUp(id);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setStatus(ActionStatus.SUCCESS);
			return tempVar2;
		} catch (Exception ex) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setStatus(ActionStatus.ERROR);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}
}