package fgenejfx.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import fgenejfx.controllers.League;
import fgenejfx.models.Pilot;

public class NewPilotsTests {

	@Test
	public void pilotCreation() {
		final int HOWMANY = 2;
		Set<Pilot> set = League.get().createNewPilots(HOWMANY);
		assertEquals(set.size(), HOWMANY);
		
		set.forEach(p ->{
			//---------------------------------------------------- ai
			assertNotNull(p.getAI());
			assertTrue(p.getAI() > 50 && p.getAI() < 150);
			
			//---------------------------------------------------- rookieYear
			assertNotNull(p.getRookieYear());
			assertEquals(p.getRookieYear(), League.get().getYear());
			
			//---------------------------------------------------- stats
			assertNotNull(p.getLifeStats());
			assertNotNull(p.getStats());
			assertNotNull(p.getStats().getSeason());
			assertNotNull(p.getStats().getpPlayoff());
			assertNotNull(p.getStats().gettPlayoff());
		});
	}
	
}
