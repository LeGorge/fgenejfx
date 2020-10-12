package fgenejfx.controllers;

import java.util.List;
import java.util.Random;
import java.util.Set;

import fgenejfx.exceptions.NotValidException;
import fgenejfx.models.Group;
import fgenejfx.models.Pilot;
import fgenejfx.models.RaceStats;
import fgenejfx.models.RaceStatsTeam;
import fgenejfx.models.Season;
import fgenejfx.models.Team;
import fgenejfx.models.enums.LeagueTime;
import fgenejfx.models.enums.OpEnum;
import fgenejfx.models.enums.State;

public class SeasonChangeController {
	private League l = League.get();
	private HistoryController hag = HistoryController.get();
	private ContractsController cag = ContractsController.get();
	private NewsController news = NewsController.get();

	public SeasonChangeController() throws NotValidException {
		if (l.getSeason().getState() != State.ENDED) {
			throw new NotValidException();
		}
		
		//news part I
		oldSeasonNews();
		Set<Pilot> upcomingFreeAgents = cag.upcomingFreeAgents();

		// save history
		hag.save(l.getSeason());

		// update powers
		updatePowers();

		// update ai
		updateAI();

		// update stats
		updateStats();

		// pass year
		l.passYear();

		// update contracts
		int newPilots = ContractsController.get().willRetire().size();
		cag.updateContracts(l.createNewPilots(newPilots));

		// new season
		l.setSeason(new Season());
		
		//news
		newSeasonNews(upcomingFreeAgents);
	}

	private void updatePowers() {
		// -2 for pplayoff teams
		l.getSeason().teams(LeagueTime.PPLAYOFF).forEach(t -> {
			t.updatePowers(2, OpEnum.SUBTRACT);
		});

		// +1 for tplayoff champion
		l.getSeason().champ(LeagueTime.TPLAYOFF).updatePowers(1, OpEnum.SUM);

		// +1 for non-pplayoff teams
		cag.teams().stream().filter(t -> !l.getSeason().teams(LeagueTime.PPLAYOFF).contains(t))
				.forEach(t -> t.updatePowers(1, OpEnum.SUM));

		// -1 for random team
		Team t = (Team) cag.teams().toArray()[new Random().nextInt(cag.teams().size())];
		t.updatePowers(1, OpEnum.SUBTRACT);

		// update car files with powers in generally
		//GenerallyFilesController.updateCarFile();
	}

	private void updateAI() {
		Season last = null;
		if (l.getYear() != 1) {
			last = hag.season(l.getYear() - 1);
		}
		for (Pilot p : l.getSeason().pilots()) {
			Group season = l.getSeason().seasonGroupOf(p);

			int seasonPlacing = season.posOf(p);
			int pplayoffPlacing = -1;
			if (seasonPlacing == 1) {
				pplayoffPlacing = l.getSeason().getpPlayoff().posOf(p);
			}

			int lastYearSeasonPlacing = -1;
			int lastYearPplayoffPlacing = -1;
			if (!p.isRookie() && last != null) {
				lastYearSeasonPlacing = last.seasonGroupOf(p).posOf(p);
				if (lastYearSeasonPlacing == 1) {
					lastYearPplayoffPlacing = last.getpPlayoff().posOf(p);
				}
			}
			p.updateAi(seasonPlacing, lastYearSeasonPlacing, pplayoffPlacing, lastYearPplayoffPlacing,
					season.closeFight(p));

			//GenerallyFilesController.updateDriverAI(p);
		}
	}

	private void updateStats() {
		Season s = l.getSeason();

		// update season stats
		l.getTeams().forEach(t -> t.getLifeStats().incrementSeasons());
		for (Pilot p : s.pilots()) {
			p.getLifeStats().incrementSeasons();

			RaceStats st = s.statsOf(p, LeagueTime.SEASON);
			RaceStats newSt = RaceStats.sum(p.getStats().getSeason(), st);
			p.getStats().setSeason(newSt);

			Team t = l.teamOf(p, s.getYear());
			newSt = RaceStats.sum(t.getStats().getSeason(), st);
			t.getStats().setSeason(new RaceStatsTeam(newSt));
		}

		// update pplayoff stats
		int cont = 0;
		for (Pilot p : s.getpPlayoff().pilots()) {
			RaceStats st = s.getpPlayoff().statsOf(p);
			RaceStats newSt = RaceStats.sum(p.getStats().getpPlayoff(), st);
			p.getStats().setpPlayoff(newSt);

			Team t = l.teamOf(p, s.getYear());
			newSt = RaceStats.sum(t.getStats().getpPlayoff(), st);
			t.getStats().setpPlayoff(new RaceStatsTeam(newSt));

			// update lifeStats
			p.getLifeStats().incrementpPlayoffs();
			t.getLifeStats().incrementpPlayoffs();
			switch (cont) {
			case 0:
				p.getLifeStats().incrementpGold();
				t.getLifeStats().incrementpGold();
				break;
			case 1:
				p.getLifeStats().incrementpSilver();
				t.getLifeStats().incrementpSilver();
				break;
			case 2:
				p.getLifeStats().incrementpBronze();
				t.getLifeStats().incrementpBronze();
				break;
			}
			cont++;
		}

		// update tplayoff stats
		for (Pilot p : s.gettPlayoff().pilots()) {
			RaceStats st = s.gettPlayoff().statsOf(p);
			RaceStats newSt = RaceStats.sum(p.getStats().gettPlayoff(), st);
			p.getStats().settPlayoff(newSt);

			Team t = l.teamOf(p, s.getYear());
			newSt = RaceStats.sum(t.getStats().gettPlayoff(), st);
			t.getStats().settPlayoff(new RaceStatsTeam(newSt));
		}

		// update tplayoff lifeStats
		cont = 0;
		for (Team t : s.gettPlayoff().teams(s.getYear())) {
			t.getLifeStats().incrementtPlayoffs();
			List<Pilot> ps = l.pilotsOf(t, s.getYear());
			switch (cont) {
			case 0:
				ps.get(0).getLifeStats().incrementtGold();
				ps.get(1).getLifeStats().incrementtGold();
				t.getLifeStats().incrementtGold();
				break;
			case 1:
				ps.get(0).getLifeStats().incrementtSilver();
				ps.get(1).getLifeStats().incrementtSilver();
				t.getLifeStats().incrementtSilver();
				break;
			case 2:
				ps.get(0).getLifeStats().incrementtBronze();
				ps.get(1).getLifeStats().incrementtBronze();
				t.getLifeStats().incrementtBronze();
				break;
			}
			cont++;
		}
	}
	
	private void oldSeasonNews() {
	  Set<Pilot> retired = cag.willRetire();
	  retired.stream().forEach(p ->{
	    news.add(l.getYear()+1, "Retirings: "+cag.teamOf(p)+"'s "+p+" has retired from the League");
	  });
	}
	
	private void newSeasonNews(Set<Pilot> changedContracts) {
	  Set<Pilot> rookies = cag.rookies();
	  rookies.stream().forEach(p ->{
	    news.add(l.getYear(), "Rookies: "+p+" was drafted by "+cag.teamOf(p));
	  });
	  
	  changedContracts.stream().forEach(p->{
	    Team oldT = hag.history(l.getYear()-1).teamOf(p);
	    Team newT = cag.teamOf(p);
	    if(oldT == newT) {
	      news.add(l.getYear(), "New Contracts: "+newT+" re-signed "+p+" to a "
            +cag.remainingYearsOfContract(p)+"-year deal");
	    }else {
	      news.add(l.getYear(), "New Contracts: "+newT+" signed "+oldT+"'s "+p+" to a "
	          +cag.remainingYearsOfContract(p)+"-year deal");
	    }
	  });
	}
}
