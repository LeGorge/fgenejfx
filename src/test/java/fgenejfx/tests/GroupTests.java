package fgenejfx.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fgenejfx.controllers.ContractsController;
import fgenejfx.controllers.HistoryController;
import fgenejfx.controllers.League;
import fgenejfx.models.Contract;
import fgenejfx.models.Group;
import fgenejfx.models.Pilot;
import fgenejfx.models.RaceStats;
import fgenejfx.models.Team;
import fgenejfx.models.enums.TeamsEnum;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;

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
		HistoryController.reset();
		ContractsController.reset();
		
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
		ContractsController.get().setContracts(cs);
		
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
	
	@Test
  public void per() {
	  new MockUp<RaceStats>() {
      @Mock
      public Integer getPts(Invocation inv) {
        RaceStats invoked = inv.getInvokedInstance();
        Integer[] is = {107,101,64,61,20,8};
        return is[invoked.getP1st()];
      }
      @Mock
      public Integer getTotalRaces() {
        return 19;
      }
    };
    
//    double[] assertResult = {0.8361815007433951, 0.5653067318158819, 0.39752549817412935,
//        0.4300688392653924, 0.14591743000120114, 0.0};
    double[] assertResult = {-1.3, 1.0, 0.2, -0.3, -0.1, 0.5};
    
    p1.setAi(143);
    p2.setAi(136);
    p3.setAi(131);
    p4.setAi(132);
    p5.setAi(112);
    p6.setAi(108);
    
    //mapping for mock of getPts()
    g.statsOf(p1).setP1st(0);
    g.statsOf(p2).setP1st(1);
    g.statsOf(p3).setP1st(2);
    g.statsOf(p4).setP1st(3);
    g.statsOf(p5).setP1st(4);
    g.statsOf(p6).setP1st(5);
    assertEquals(107, g.statsOf(p1).getPts());
    assertEquals(101, g.statsOf(p2).getPts());
    assertEquals(64, g.statsOf(p3).getPts());
    assertEquals(61, g.statsOf(p4).getPts());
    assertEquals(20, g.statsOf(p5).getPts());
    assertEquals(8, g.statsOf(p6).getPts());
    
    g.updatePer();
    
//    assertEquals(assertResult[0] ,g.statsOf(p1).getPer());
//    assertEquals(assertResult[1] ,g.statsOf(p2).getPer());
//    assertEquals(assertResult[2] ,g.statsOf(p3).getPer());
//    assertEquals(assertResult[3] ,g.statsOf(p4).getPer());
//    assertEquals(assertResult[4] ,g.statsOf(p5).getPer());
//    assertEquals(assertResult[5] ,g.statsOf(p6).getPer());
  }
}
