package fgenejfx.controllers;

import fgenejfx.models.ContractsAgent;
import fgenejfx.models.HistoryAgent;
import fgenejfx.models.Powers;

public class SeasonChangeController {
	private League l = League.get();
	private HistoryAgent hag = HistoryAgent.get();
	private ContractsAgent cag = ContractsAgent.get();


	public SeasonChangeController(){
		//save history
		hag.save(l.getSeason());

		//update powers
		updatePowers();

		//update ai
		//update stats
		
		//pass year
		l.passYear();

		//update contracts
		cag.updateContracts(l.createNewPilots(2));
	}

	public void updatePowers(){
		//-2 for pplayoff teams
		l.getSeason().pPlayoffTeams().forEach(t -> {
			Powers.update(t, 2, false);
		});

		//+1 for tplayoff champion
		Powers.update(l.getSeason().tChamp(), 1, true);
		
		//+1 for non-pplayoff teams
		l.getSeason().pPlayoffTeams().forEach(t -> {
			Powers.update(t, 2, false);
		});
		//-1 for random team
		l.getSeason().pPlayoffTeams().forEach(t -> {
			Powers.update(t, 2, false);
		});

		//updateBonus();
		for(Equipe eHere : equipes){
			Equipe eFGene = FGene.getEquipe(eHere.name);
			int flag = 0, cont2 = 0, cont = 0;
			while(flag == 0 && cont < 20){
				Powers p = Powers.get();
				double atual = eFGene.powers.get(p);
				if((atual < (p.def + FGene.MAXPOWERCHANGES*Math.abs(p.inc))) && (atual > (p.def - FGene.MAXPOWERCHANGES*Math.abs(p.inc)))){
					if(playoffs.contains(eHere.piloto1) || playoffs.contains(eHere.piloto2)){
						if(eFGene.currentBonus != Bonus.VETO){
							eFGene.powers.put(p,atual-p.inc);
						}
						if(++cont2 == 2){
							cont2 = 0;
							flag = 1;
						}
					}else{
						eFGene.powers.put(p,atual+p.inc);
						flag = 1;
					}
				}else{
					cont++;
				}
			}
		}
		new FileStreamController().updateCarFile();
	
	}
}
