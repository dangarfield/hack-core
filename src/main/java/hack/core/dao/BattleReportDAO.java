package hack.core.dao;

import hack.core.models.BattleReport;

import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BattleReportDAO {

	@Autowired
	private MongoCollection battleReports;
	
	public void save(BattleReport battleReport) {
		battleReports.save(battleReport);
	}
	
	public void removeAllData() {
		battleReports.remove();
	}
}
