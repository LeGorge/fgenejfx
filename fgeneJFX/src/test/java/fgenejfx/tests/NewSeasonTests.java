package fgenejfx.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fgenejfx.controllers.ContractsController;
import fgenejfx.controllers.GenerallyFilesController;
import fgenejfx.controllers.HistoryController;
import fgenejfx.controllers.League;
import fgenejfx.controllers.PersistanceController;
import fgenejfx.exceptions.NotValidException;
import fgenejfx.models.Contract;
import fgenejfx.models.Group;
import fgenejfx.models.Pilot;
import fgenejfx.models.Powers;
import fgenejfx.models.RaceStats;
import fgenejfx.models.Season;
import fgenejfx.models.Team;
import fgenejfx.utils.Utils;
import mockit.Mock;
import mockit.MockUp;

public class NewSeasonTests {
	
	@BeforeEach
	public void reset() {
		League.reset();
		HistoryController.reset();
		ContractsController.reset();
	}
	
	@Test
	public void groups() {
		PersistanceController.load();
		Season s = new Season();

		for (Group g : s.getSeason()) {
			for (Pilot p : g.pilots()) {
				assertTrue(s.seasonGroupOf(p) == g);
				Team t = ContractsController.get().teamOf(p);
				Pilot p2 = ContractsController.get().pilotsOf(t).stream().filter(pp -> pp != p).findFirst().get();
				assertTrue(s.seasonGroupOf(p2) == g);
			}
		}
	}

	@Test
	public void updatePowers() throws NotValidException {
		PersistanceController.load();
		ContractsController cag = ContractsController.get();
		Season s = League.get().getSeason();

		cag.teams().forEach(t -> {
			assertTrue(t.carPower() == 0);
		});

		Group g = new Group(
			cag.teams().stream().map(t -> cag.pilotsOf(t).get(0))
				.limit(6).collect(Collectors.toSet())
		);
			
		s.setpPlayoff(g);
		s.settPlayoff(s.getSeason()[1]);

		Group g2 = s.gettPlayoff();
		Pilot p = g2.firstPilot();
		g2.statsOf(p).setP1st(1);
		g2 = s.getpPlayoff();
		p = g2.firstPilot();
		g2.statsOf(p).setP1st(1);
		
		League.get().changeSeason();

		Set<Team> teams = new HashSet<>(cag.teams());
		int totalCarPower  = teams.stream().collect(Collectors.summingInt(Team::carPower));
		assertTrue(totalCarPower == 0);

		teams.remove(g.firstTeam(s.getYear()));
		totalCarPower  = teams.stream().collect(Collectors.summingInt(Team::carPower));
		assertTrue(totalCarPower != 0);
	}

//	@Test
//	public void midSeasonStates() throws NotValidException {
//	  new MockUp<GenerallyFilesController>() {
//      @Mock
//      public RaceStats readDriver(String name) {
//          return new RaceStats();
//      }
//	  };
//
//	  Utils.begin();
//	  League l = League.get();
//	  Season s = l.getSeason();
//	  Arrays.stream(s.getSeason()).forEach(g -> {
//	    Pilot p = g.firstPilot();
//	    g.statsOf(p).setP1st(1);
//	    p.getStats().getSeason().setP1st(1);
//	  });
//
//	  assertFalse(s.ended());
//	  assertTrue(s.playoffReady());
//
//	  s.sync();
//	  assertTrue(s.inPlayoffs());
//
//	  Group g = s.gettPlayoff();
//	  Pilot p = g.firstPilot();
//	  g.statsOf(p).setP1st(1);
//	  assertFalse(s.ended());
//
//	  g = s.getpPlayoff();
//	  p = g.firstPilot();
//	  g.statsOf(p).setP1st(1);
//	  assertTrue(s.ended());
//	}
	
	@Test
	public void changeSeasonFlow() throws NotValidException {
	  new MockUp<GenerallyFilesController>() {
	    @Mock
	    public RaceStats readDriver(String name) {
	      return new RaceStats();
	    }
	  };
	  
		Utils.begin();
		League l = League.get();
		Season s = l.getSeason();
		ContractsController cag = ContractsController.get();
		HistoryController hag = HistoryController.get();
		Arrays.stream(s.getSeason()).forEach(g -> g.statsOf(g.firstPilot()).setP1st(1));
		s.startPlayoffs();
		
		Group gp = s.getpPlayoff();
		List<Pilot> psp = gp.pilots();
		gp.statsOf(psp.get(0)).setP1st(1);
		gp.statsOf(psp.get(1)).setP2nd(1);
		gp.statsOf(psp.get(2)).setP3rd(1);
		gp.statsOf(psp.get(3)).setP4th(1);
		gp.statsOf(psp.get(4)).setP5th(1);
		gp.statsOf(psp.get(5)).setP6th(1);
		
		Group gt = s.gettPlayoff();
		List<Pilot> pst = gt.pilots();
		gt.statsOf(pst.get(0)).setP1st(1);
		gt.statsOf(pst.get(1)).setP2nd(1);
		gt.statsOf(pst.get(2)).setP3rd(1);
		gt.statsOf(pst.get(3)).setP4th(1);
		gt.statsOf(pst.get(4)).setP5th(1);
		gt.statsOf(pst.get(5)).setP6th(1);
		
		int oldYear = l.getYear();
		Team oldTeam = l.teamOf(psp.get(0), l.getYear());
		int oldTeamCarPower = oldTeam.carPower();
		Team teamTchamp = gt.firstTeam(l.getYear());
		Pilot pilotPchamp = gp.firstPilot();
		int oldAi = pilotPchamp.getAi();
		Pilot oldRookie = cag.rookies().stream().findFirst().get();
		int oldRookieAi = oldRookie.getAi();
		Contract notDone = cag.getContracts().stream().filter(c -> c.getYears() != 1)
				.findAny().get();
		int oldYearsNotDone = notDone.getYears();
		Contract done = cag.getContracts().stream().filter(c -> c.getYears() == 1)
				.findAny().get();
		
		l.changeSeason();
		
		//history
		assertSame(s, hag.season(oldYear));
		assertEquals(oldAi, hag.history(s).ai(pilotPchamp));
		assertEquals(oldTeamCarPower, Powers.carPower(hag.history(s).powers(oldTeam)));
		
		//powers
		assertEquals(-1, teamTchamp.carPower());
		
		//ai
		assertNotEquals(oldRookieAi, oldRookie.getAi());
		assertEquals(oldAi, pilotPchamp.getAi());
		
		//stats
		assertEquals(1, pilotPchamp.getLifeStats().getPGold());
		assertEquals(1, pilotPchamp.getStats().getSeason().getP1st());
		assertEquals(1, pilotPchamp.getStats().getpPlayoff().getP1st());
		
		assertEquals(1, teamTchamp.getLifeStats().getTGold());
		assertEquals(1, teamTchamp.getStats().getSeason().getP1st());
		
		
		//pass year
		assertEquals(1, l.getYear() - oldYear);
		
		//update contracts
		assertTrue(cag.getContracts().contains(notDone));
		assertEquals(1, oldYearsNotDone - notDone.getYears());
		assertFalse(cag.getContracts().contains(done));
		
		//new season
		assertNotSame(s, l.getSeason());
	}
}
