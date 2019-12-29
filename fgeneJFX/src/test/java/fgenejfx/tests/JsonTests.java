package fgenejfx.tests;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import fgenejfx.controllers.League;
import fgenejfx.controllers.PersistanceController;
import fgenejfx.exceptions.CopyException;
import fgenejfx.models.ContractsAgent;
import fgenejfx.models.HistoryAgent;
import fgenejfx.models.Pilot;
import fgenejfx.models.Season;
import fgenejfx.models.Team;
import fgenejfx.models.TeamHistory;
import fgenejfx.models.TeamsEnum;

public class JsonTests {

	@Test
	public void updateContractOfRetiringPilot() {
		League l = League.get();
		HistoryAgent hag = HistoryAgent.get();
		ContractsAgent cag = ContractsAgent.get();

		l.setTeams(TeamsEnum.create());
		Set<Pilot> ps = new HashSet<>();
		for (int i = 0; i < 36; i++) {
			Pilot p = new Pilot("A"+i);
			ps.add(p);
		}
		l.setPilots(ps);

		cag.updateContracts(ps);
		Season s = new Season();
		l.setSeason(s);

		hag.save(s);

		String json = PersistanceController.json(hag);
		System.out.println(json);

		// PersistanceController.save(l.getSeason());
		// String json2 = PersistanceController.json(l.getSeason());
		// System.out.println(json2);
		
		HistoryAgent g2 = (HistoryAgent)PersistanceController.loadJSON(json, HistoryAgent.class);
		// Group g2 = PersistanceController.loadJSON(json);
		// League g2 = (League)PersistanceController.loadJSON(json, League.class);
		
		// g2.pilots().forEach(p -> System.out.println(p.getName() +" - "+ p.getAI()));
	}
	
}
