package fgenejfx.controllers;

import java.util.List;
import java.util.Random;

import fgenejfx.exceptions.NotValidException;
import fgenejfx.models.ContractsAgent;
import fgenejfx.models.Group;
import fgenejfx.models.HistoryAgent;
import fgenejfx.models.Pilot;
import fgenejfx.models.RaceStats;
import fgenejfx.models.RaceStatsTeam;
import fgenejfx.models.Season;
import fgenejfx.models.Team;

public class SeasonChangeController {
	private League l = League.get();
	private HistoryAgent hag = HistoryAgent.get();
	private ContractsAgent cag = ContractsAgent.get();

	public SeasonChangeController() throws NotValidException {
		if(!l.getSeason().ended()) {
			throw new NotValidException();
		}
		
		//save history
		hag.save(l.getSeason());
		
		//update powers
		updatePowers();
		
		//update ai
		updateAI();
		
		//update stats
		updateStats();
		
		//pass year
		l.passYear();
		
		//update contracts
		int newPilots = ContractsAgent.get().willRetire().size();
		cag.updateContracts(l.createNewPilots(newPilots));
		
		//new season
		l.setSeason(new Season());
	}
	
	private void updatePowers(){
		//-2 for pplayoff teams
		l.getSeason().pPlayoffTeams().forEach(t -> {
			t.updatePowers(2, false);
		});
		
		//+1 for tplayoff champion
		l.getSeason().tChamp().updatePowers(1, true);
		
		//+1 for non-pplayoff teams
		cag.teams().stream().filter(t -> !l.getSeason().pPlayoffTeams().contains(t))
		.forEach(t -> t.updatePowers(1, true));
		
		//-1 for random team
		Team t = (Team)cag.teams().toArray()[new Random().nextInt(cag.teams().size())];
		t.updatePowers(1, false);
		
		//update car files with powers in generally
		GenerallyFilesController.updateCarFile();
	}
	
	private void updateAI(){
		Season last = null;
		if(l.getYear() != 1) {
			last = hag.season(l.getYear()-1);
		}
		for (Pilot p : l.getSeason().pilots()) {
			Group season = l.getSeason().seasonGroupOf(p);
			
			int seasonPlacing = season.posOf(p);
			int pplayoffPlacing = -1;
			if(seasonPlacing == 1) {
				pplayoffPlacing = l.getSeason().getpPlayoff().posOf(p);
			}
			
			int lastYearSeasonPlacing = -1;
			int lastYearPplayoffPlacing = -1;
			if(!p.isRookie() && last != null) {
				lastYearSeasonPlacing = last.seasonGroupOf(p).posOf(p);
				if(lastYearSeasonPlacing == 1) {
					lastYearPplayoffPlacing = last.getpPlayoff().posOf(p);
				}
			}
			p.updateAi(seasonPlacing, lastYearSeasonPlacing, pplayoffPlacing,
					lastYearPplayoffPlacing, season.closeFight(p));
			
			GenerallyFilesController.updateDriverAI(p);
		}
	}
	
	private void updateStats(){
		Season s = l.getSeason();
		
		//update season stats
		l.getTeams().forEach(t -> t.getLifeStats().incrementSeasons());
		for (Pilot p : s.pilots()) {
			p.getLifeStats().incrementSeasons();
			
			RaceStats st = s.seasonStatsOf(p);
			RaceStats newSt = RaceStats.somarStats(p.getStats().getSeason(), st, true);
			p.getStats().setSeason(newSt);
			
			Team t = l.teamOf(p, s.getYear());
			newSt = RaceStats.somarStats(t.getStats().getSeason(), st, true);
			t.getStats().setSeason(new RaceStatsTeam(newSt));
		}
		
		//update pplayoff stats
		int cont = 0;
		for (Pilot p : s.getpPlayoff().pilots()) {
			RaceStats st = s.getpPlayoff().statsOf(p);
			RaceStats newSt = RaceStats.somarStats(p.getStats().getpPlayoff(), st, true);
			p.getStats().setpPlayoff(newSt);
			
			Team t = l.teamOf(p, s.getYear());
			newSt = RaceStats.somarStats(t.getStats().getpPlayoff(), st, true);
			t.getStats().setpPlayoff(new RaceStatsTeam(newSt));
			
			//update lifeStats
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
		
		//update tplayoff stats
		for (Pilot p : s.gettPlayoff().pilots()) {
			RaceStats st = s.gettPlayoff().statsOf(p);
			RaceStats newSt = RaceStats.somarStats(p.getStats().gettPlayoff(), st, true);
			p.getStats().settPlayoff(newSt);
			
			Team t = l.teamOf(p, s.getYear());
			newSt = RaceStats.somarStats(t.getStats().gettPlayoff(), st, true);
			t.getStats().settPlayoff(new RaceStatsTeam(newSt));
		}
		
		//update tplayoff lifeStats
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
}