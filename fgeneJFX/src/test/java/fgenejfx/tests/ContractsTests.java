package fgenejfx.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import fgenejfx.controllers.League;
import fgenejfx.models.ContractsAgent;
import fgenejfx.models.HistoryAgent;
import fgenejfx.models.Pilot;

public class ContractsTests {
	
	@Test
	public void updateContractOfRetiringPilot() {
		League l = League.get();
		ContractsAgent cag = ContractsAgent.get();
		HistoryAgent hag = HistoryAgent.get();
		
		Pilot p = new Pilot("");
		assertEquals(p.getRookieYear(), League.get().getYear());
		
		for (int i = 1; i < Pilot.MAX_YEARS_ON_CAREER; i++) {
			l.passYear();
		}
		assertEquals(1, p.getYearsUntilRetirement());
		
		assertEquals(18, l.getYear());
		HashSet<Pilot> set = new HashSet<>();
		set.add(p);
		cag.updateContracts(set);
		assertNotNull(cag.getTeamOf(p));
		assertEquals(1, cag.getRemainingYearsOfContract(p));
		
		l.passYear();
		
		cag.updateContracts(new HashSet<>());
		assertThrows(NoSuchElementException.class, () -> cag.getTeamOf(p));
		assertNotNull(hag.getContractsHistory(l.getYear()-1).getTeamOf(p));
		
	}
	
}
