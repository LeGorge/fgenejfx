package fgenejfx.controllers;

import java.util.Random;

import fgenejfx.exceptions.NotValidException;
import fgenejfx.models.ContractsAgent;
import fgenejfx.models.HistoryAgent;
import fgenejfx.models.Powers;
import fgenejfx.models.Team;

public class SeasonChangeController {
	private League l = League.get();
	private HistoryAgent hag = HistoryAgent.get();
	private ContractsAgent cag = ContractsAgent.get();

	public SeasonChangeController() throws NotValidException {
		//save history
		hag.save(l.getSeason());
		
		//update powers
		updatePowers();
		
		//update ai
		//update stats
		
		//pass year
		l.passYear();
		
		//update contracts
		int newPilots = ContractsAgent.get().willRetire().size();
		cag.updateContracts(l.createNewPilots(newPilots));
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
		new GenerallyFilesController().updateCarFile();
	}
}
