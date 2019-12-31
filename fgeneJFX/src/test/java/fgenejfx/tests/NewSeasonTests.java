package fgenejfx.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import fgenejfx.controllers.League;
import fgenejfx.controllers.PersistanceController;
import fgenejfx.exceptions.NotValidException;
import fgenejfx.models.ContractsAgent;
import fgenejfx.models.Group;
import fgenejfx.models.Pilot;
import fgenejfx.models.Season;
import fgenejfx.models.Team;
import fgenejfx.models.TeamsEnum;

public class NewSeasonTests {

	@Test
	public void groups() {
		PersistanceController.load();
		Season s = new Season();

		for (Group g : s.getSeason()) {
			for (Pilot p : g.pilots()) {
				assertTrue(s.seasonGroupOf(p) == g);
				Team t = ContractsAgent.get().teamOf(p);
				Pilot p2 = ContractsAgent.get().pilotsOf(t).stream().filter(pp -> pp != p).findFirst().get();
				assertTrue(s.seasonGroupOf(p2) == g);
			}
		}
	}

	@Test
	public void updatePowers() throws NotValidException {
		PersistanceController.load();
		ContractsAgent cag = ContractsAgent.get();
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

		League.get().changeSeason();

		Set<Team> teams = new HashSet<>(cag.teams());
		int totalCarPower  = teams.stream().collect(Collectors.summingInt(Team::carPower));
		assertTrue(totalCarPower == 0);

		teams.remove(g.firstTeam(s.getYear()));
		totalCarPower  = teams.stream().collect(Collectors.summingInt(Team::carPower));
		assertTrue(totalCarPower != 0);

	}

}
