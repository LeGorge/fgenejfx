package fgenejfx.controllers;

import java.util.Random;

import fgenejfx.exceptions.NotValidException;
import fgenejfx.models.ContractsAgent;
import fgenejfx.models.Group;
import fgenejfx.models.HistoryAgent;
import fgenejfx.models.Pilot;
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
		
		//pass year
		l.passYear();
		
		//update contracts
		int newPilots = ContractsAgent.get().willRetire().size();
		cag.updateContracts(l.createNewPilots(newPilots));
		
		//new season
	}
	
	public void updatePowers(){
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
	
	public void updateAI(){
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
			p.updateAi(seasonPlacing, lastYearSeasonPlacing, pplayoffPlacing, lastYearPplayoffPlacing, season.closeFight(p));
			GenerallyFilesController.updateDriverAI(p);
		}
	}

}
