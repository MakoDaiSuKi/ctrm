package com.smm.ctrm.bo.Futures;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;

import com.smm.ctrm.domain.Maintain.Reuter;
import com.smm.ctrm.domain.Physical.CpSplitPosition;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.apiClient.PositionParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;
public interface PositionService {
	void ScheduledUpdateAveragePosition();
	void UpdateAveragePositionById(String positionId);
	void UpdateAveragePosition(Position position);
	void UpdateSfeAveragePositionById(String positionId);
	void UpdateSfeAveragePosition(Position position);
	void UpdateLmeAveragePositionById(String positionId);
	void UpdateLmeAveragePosition(Position position);
	ActionResult<String> Square(List<Position> positions);
	ActionResult<String> ImportLMEsViaList(List<Position> positions, String userName, String userId);
	void ImportLMEsViaStream(InputStream stream) throws Exception;
	ActionResult<List<Position>> PositionsByContractId(String contractId);
	ActionResult<List<Position>> PositionsByLotId(String lotId);
	ActionResult<List<Position>> PositionsOpening(String invoiceId);
	ActionResult<List<Position>> PositionsBothWayByInvoiceId(String invoiceId);
	Criteria GetCriteria();
	ActionResult<String> SaveVirtualSwapPosition(List<Position> positions) throws Exception;
	ActionResult<String> SavePosition2Broker(Position position);
	ActionResult<Position> Save(Position position);
	ActionResult<String> SaveCarryPositions(List<Position> CarryPositions);
	ActionResult<String> Position2Lot(List<Position> positions);
	ActionResult<String> Position2Lot(Position position);
	ActionResult<String> UnPosition2Lot(Position position);
	ActionResult<String> Delete(String id);
	/**逻辑说明：
	 * 1. 拆分出来的数量必须小于原数量
	 * 2. 如果头寸已经被用于保值，则不允许拆分
	 * @param cpSplitPosition
	 * @param userId
	 * @return
	 */
	ActionResult<String> SplitPosition(CpSplitPosition cpSplitPosition, String userId);
	ActionResult<Position> GetById(String positionId);
	ActionResult<Reuter> GetReuterPrice(String CommodityId, Date TradeDate, Date PromptDate);
	ActionResult<String> CreateVirtual(String positionId);
	ActionResult<String> CreateVirtual(Position position);
	ActionResult<Position> GetCounterPartById(String positionId);
	ActionResult<List<Position>> GetCarryPostionsById(String positionId);
	List<Position> GetVirtualSwapPositionByCounterpartId(String counterpartId);
	List<Position> Positions(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy,
			String orderBy);
	
	ActionResult<List<Position>> UndistributePositionRisk(PositionParams param);
	List<Position> getPositionListByPosition4BrokerId(List<String> position4BrokerIdList);
	ActionResult<List<Position>> Arbitrage(Position position);

}