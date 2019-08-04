package fgenejfx.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import fgenejfx.exceptions.CopyException;
import fgenejfx.exceptions.NaoEncontradoException;
import fgenejfx.models.HistoryAgent;
import fgenejfx.models.Pilot;
import fgenejfx.models.Team;

public class UnityTests {

	@Test
	public void historyTest() throws NaoEncontradoException, CopyException {
		HistoryAgent history = HistoryAgent.get();
		
		Team t = new Team();
		history.save(1, t, t.getStats());
		
		assertEquals(t.getPowers(), history.getPowersByYear(t, 1));
		assertNotSame(t.getPowers(), history.getPowersByYear(t, 1));
		assertEquals(t.getStats(), history.getStatByYear(t, 1));
		assertNotSame(t.getStats(), history.getStatByYear(t, 1));
		
		Pilot p = new Pilot();
		history.save(1, p, p.getStats());
		assertEquals(p.getStats(), history.getStatByYear(p, 1));
		assertNotSame(p.getStats(), history.getStatByYear(p, 1));
	}
}
