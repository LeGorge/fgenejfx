package fgenejfx.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fgenejfx.models.Group;
import fgenejfx.models.Pilot;

public class GroupTests {

	@Test
	public void posOf() {
		Pilot p1 = new Pilot("A1");
		Pilot p2 = new Pilot("A2");
		Pilot p3 = new Pilot("A3");
		Pilot p4 = new Pilot("A4");
		Pilot p5 = new Pilot("A5");
		Pilot p6 = new Pilot("A6");
		
		Set<Pilot> ps = new HashSet<>();
		ps.add(p1);
		ps.add(p2);
		ps.add(p3);
		ps.add(p4);
		ps.add(p5);
		ps.add(p6);
		
		Group g = new Group(ps);
		
		g.statsOf(p1).setP1st(1);
		assertTrue(g.statsOf(p1).getPts() == 8);
		
		g.statsOf(p2).setP1st(1);
		g.statsOf(p2).setP2nd(1);
		assertTrue(g.posOf(p2) == 1);
		assertTrue(g.posOf(p1) == 2);
		
		g.statsOf(p3).setP3rd(1);
		g.statsOf(p4).setP4th(1);
		g.statsOf(p5).setP5th(1);
		g.statsOf(p6).setP6th(1);
		assertTrue(g.posOf(p6) == 6);
	}
	@Test
	public void closeFight() {
		Pilot p1 = new Pilot("A1");
		Pilot p2 = new Pilot("A2");
		Pilot p3 = new Pilot("A3");
		Pilot p4 = new Pilot("A4");
		Pilot p5 = new Pilot("A5");
		Pilot p6 = new Pilot("A6");
		
		Set<Pilot> ps = new HashSet<>();
		ps.add(p1);
		ps.add(p2);
		ps.add(p3);
		ps.add(p4);
		ps.add(p5);
		ps.add(p6);
		
		Group g = new Group(ps);
		
		assertTrue(g.closeFight(p1));
		assertTrue(g.closeFight(p2));
		
		g.statsOf(p1).setP1st(3);
		assertTrue(g.statsOf(p1).getPts() == 24);
		assertFalse(g.closeFight(p1));
		
		g.statsOf(p2).setP1st(2);
		assertTrue(g.closeFight(p1));
		assertTrue(g.closeFight(p2));
	}
}
