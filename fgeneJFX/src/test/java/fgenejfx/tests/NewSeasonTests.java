package fgenejfx.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fgenejfx.controllers.PersistanceController;
import fgenejfx.models.ContractsAgent;
import fgenejfx.models.Group;
import fgenejfx.models.Pilot;
import fgenejfx.models.Season;
import fgenejfx.models.Team;

public class NewSeasonTests {

	@Test
	public void groups() {
		PersistanceController.load();
		Season s = new Season();

		for (Group g : s.getSeason()) {
			for (Pilot p : g.pilots()) {
				assertTrue(s.seasonGroupOf(p) == g);
				Team t = ContractsAgent.get().teamOf(p);
				Pilot p2 = ContractsAgent.get().pilotsOf(t).stream()
				.filter(pp -> pp != p).findFirst().get();
				assertTrue(s.seasonGroupOf(p2) == g);
			}
		}
	}
	
}
