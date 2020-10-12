package fgenejfx.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import fgenejfx.controllers.ContractsController;
import fgenejfx.controllers.HistoryController;
import fgenejfx.controllers.League;
import fgenejfx.exceptions.NotValidException;
import fgenejfx.models.Pilot;

public class ContractsTests {

	@Test
	public void updateContractOfRetiringPilot() throws NotValidException {
		// League l = League.get();
		// ContractsAgent cag = ContractsAgent.get();
		// HistoryAgent hag = HistoryAgent.get();
		
		// Pilot p = new Pilot("A");
		// assertEquals(p.getRookieYear(), League.get().getYear());
		
		// for (int i = 1; i < Pilot.MAX_YEARS_ON_CAREER; i++) {
		// 	l.passYear();
		// }
		// assertEquals(1, p.getYearsUntilRetirement());
		
		// assertEquals(18, l.getYear());
		// HashSet<Pilot> set = new HashSet<>();
		// set.add(p);
		// cag.updateContracts(set);
		// assertNotNull(cag.teamOf(p));
		// assertEquals(1, cag.remainingYearsOfContract(p));
		
		// l.passYear();
		
		// set = new HashSet<>();
		// set.add(new Pilot("B"));
		// cag.updateContracts(set);
		// assertThrows(NoSuchElementException.class, () -> cag.teamOf(p));
		// assertNotNull(hag.history(l.getYear()-1).teamOf(p));
		
	}
	
}
