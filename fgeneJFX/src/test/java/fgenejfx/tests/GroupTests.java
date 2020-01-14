package fgenejfx.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fgenejfx.controllers.League;
import fgenejfx.models.Contract;
import fgenejfx.models.ContractsAgent;
import fgenejfx.models.Group;
import fgenejfx.models.HistoryAgent;
import fgenejfx.models.Pilot;
import fgenejfx.models.Team;
import fgenejfx.models.enums.TeamsEnum;

public class GroupTests {
	League l;
	
	private Group g;
	
	private Pilot p1;
	private Pilot p2;
	private Pilot p3;
	private Pilot p4;
	private Pilot p5;
	private Pilot p6;
	
	private Team t1;
	private Team t2;
	private Team t3;

	@BeforeEach
	public void setup() {
		League.reset();
		HistoryAgent.reset();
		ContractsAgent.reset();
		
		l = League.get();
		
		p1 = new Pilot("A1");
		p2 = new Pilot("A2");
		p3 = new Pilot("A3");
		p4 = new Pilot("A4");
		p5 = new Pilot("A5");
		p6 = new Pilot("A6");
		Set<Pilot> ps = new HashSet<>();
		ps.add(p1);
		ps.add(p2);
		ps.add(p3);
		ps.add(p4);
		ps.add(p5);
		ps.add(p6);
		
		t1 = Team.get(TeamsEnum.AUDI);
		t2 = Team.get(TeamsEnum.MCLAREN);
		t3 = Team.get(TeamsEnum.MERCEDEZ);
		
		Set<Contract> cs = new HashSet<>();
		cs.add(new Contract(p1, t1, true));
		cs.add(new Contract(p2, t1, false));
		cs.add(new Contract(p3, t2, true));
		cs.add(new Contract(p4, t2, false));
		cs.add(new Contract(p5, t3, true));
		cs.add(new Contract(p6, t3, false));
		ContractsAgent.get().setContracts(cs);
		
		g = new Group(ps);
	}
	
	@Test
	public void posOfPilot() {
		//obvious 1st
		g.statsOf(p1).setP1st(1);
		assertTrue(g.posOf(p1) == 1);
		assertTrue(g.statsOf(p1).getPts() == 8);
		
		//obvious 1st and 2nd
		g.statsOf(p2).setP1st(1);
		g.statsOf(p2).setP2nd(1);
		assertTrue(g.posOf(p2) == 1);
		assertTrue(g.posOf(p1) == 2);
		
		//obvious 6th
		g.statsOf(p3).setP3rd(1);
		g.statsOf(p4).setP4th(1);
		g.statsOf(p5).setP5th(1);
		g.statsOf(p6).setP6th(1);
		assertTrue(g.posOf(p6) == 6);
		
		//draw tiebreak
		g.statsOf(p5).setP5th(2);
		assertTrue(g.statsOf(p4).getPts() == g.statsOf(p5).getPts());
		assertTrue(g.posOf(p5) == 5);
	}
	
	@Test
	public void closeFight() {
		assertTrue(g.closeFight(p1));
		assertTrue(g.closeFight(p2));
		
		g.statsOf(p1).setP1st(3);
		assertTrue(g.statsOf(p1).getPts() == 24);
		assertFalse(g.closeFight(p1));
		
		g.statsOf(p2).setP1st(2);
		assertTrue(g.closeFight(p1));
		assertTrue(g.closeFight(p2));
	}
	
	@Test
	public void firstTeam() {
		//obvious 1st
		g.statsOf(p1).setP1st(1);
		assertTrue(g.firstTeam(1) == t1);
		assertTrue(g.posOf(t1,1) == 1);
		
		//sum of pilots stats
		g.statsOf(p2).setP1st(1);
		assertTrue(g.statsOf(t1,1).getPts() == 16);
		
		//draw tiebreak
		g.statsOf(p3).setP4th(1);
		g.statsOf(p5).setP5th(2);
		assertTrue(g.statsOf(t2,1).getPts() == g.statsOf(t3,1).getPts());
		assertTrue(g.posOf(t3,1) == 3);
	}
}
