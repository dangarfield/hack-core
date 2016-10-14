package hack.core.services;


import hack.core.models.BattleReport;
import hack.core.dao.BattleReportDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BattleReportService {

	@Autowired
	private BattleReportDAO battleReportDAO;
	
	public void save(BattleReport battleReport) {
		battleReportDAO.save(battleReport);
	}
}
