package com.smm.ctrm.bo.Basis;

import java.util.List;

import com.smm.ctrm.domain.Basis.Dictionary;
import com.smm.ctrm.dto.res.ActionResult;
public interface DictionaryService {
	ActionResult<List<Dictionary>> Dictionaries();
	ActionResult<List<Dictionary>> BackDictionaries();
	ActionResult<Dictionary> GetById(String id);
    Dictionary MoveUp(String id);
    Dictionary MoveDown(String id);
	ActionResult<String> Delete(String id);
	ActionResult<Dictionary> Save(Dictionary dictionary);
	ActionResult<Dictionary> SaveCat(Dictionary dictionary);
	ActionResult<Dictionary> SaveItem(Dictionary dictionary);
	ActionResult<String> DeleteCat(String id);
	ActionResult<String> DeleteItem(String id);
	ActionResult<List<Dictionary>> Cats();

    List<Dictionary> Items(String parentId);

    List<Dictionary> ItemByCatId(String catId);
	List<Dictionary> ItemsVisibleOnly();
}