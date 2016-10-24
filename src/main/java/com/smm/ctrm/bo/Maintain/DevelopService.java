package com.smm.ctrm.bo.Maintain;

import java.util.List;

import com.smm.ctrm.domain.Basis.Button;
import com.smm.ctrm.domain.Basis.Menu;
import com.smm.ctrm.domain.Basis.Resource;
import com.smm.ctrm.dto.res.ActionResult;
public interface DevelopService {
	ActionResult<Menu> MenuById(String id);
	Button ButtonById(String id);
	ActionResult<List<Menu>> Menus();
	Button SaveButton(Button button);
	ActionResult<Menu> SaveMenu(Menu menu);
	ActionResult<String> DeleteMenu(String id);
	ActionResult<String> DeleteResources(List<Resource> resources);
	List<Resource> Resources();

}