package fgenejfx;

import java.util.Arrays;

import fgenejfx.controllers.HistoryController;
import fgenejfx.controllers.League;
import fgenejfx.controllers.PersistanceController;
import fgenejfx.models.Group;
import fgenejfx.models.LifeStats;

public class Patcher {

	public static void main(String[] args) {
		PersistanceController.load();
		
//		fixPer();
		fixHistory();
		
		PersistanceController.save();
	}
	
	private static void fixPer() {
		var l = League.get();
		
		l.getPilots().stream().forEach(p -> {
			p.getStats().getSeason().setPer(0);
			p.getStats().gettPlayoff().setPer(0);
			p.getStats().getpPlayoff().setPer(0);
		});
		
		l.getTeams().stream().forEach(t -> {
			t.getStats().getSeason().setPer(0);
			t.getStats().gettPlayoff().setPer(0);
			t.getStats().getpPlayoff().setPer(0);
		});
		
		var hag = HistoryController.get();
		
		hag.getSeasons().stream().forEach(s -> {
			if(s.isEnded()) {
				Arrays.stream(s.getSeason()).forEach(Group::updatePer);
				s.gettPlayoff().updatePer();
				s.getpPlayoff().updatePer();
				
				Arrays.stream(s.getSeason()).forEach(g -> {
					g.getPilotsMap().keySet().stream().forEach(p -> {
						var per = g.getPilotsMap().get(p).getPer();
						p.getStats().getSeason().addPer(per);
						var team = l.teamOf(p, s.getYear());
						team.getStats().getSeason().addPer(per);
					});
				});
				s.gettPlayoff().getPilotsMap().keySet().stream().forEach(p -> {
					var per = s.gettPlayoff().getPilotsMap().get(p).getPer();
					p.getStats().gettPlayoff().addPer(per);
					var team = l.teamOf(p, s.getYear());
					team.getStats().gettPlayoff().addPer(per);
				});
				s.getpPlayoff().getPilotsMap().keySet().stream().forEach(p -> {
					var per = s.getpPlayoff().getPilotsMap().get(p).getPer();
					p.getStats().getpPlayoff().addPer(per);
					var team = l.teamOf(p, s.getYear());
					team.getStats().getpPlayoff().addPer(per);
				});
			}
		});
		
		Arrays.stream(l.getSeason().getSeason())
				.filter(g -> g.getPilotsMap().values().stream().anyMatch(stat -> stat.getPts() > 0))
				.forEach(Group::updatePer);
	}


	private static void fixHistory(){
		var l = League.get();
		var hag = HistoryController.get();

		// l.getPilots().stream().forEach(p -> {
		// 	p.getLifeStats().settPlayoffs(0);
		// 	p.getLifeStats().settGold(0);
		// 	p.getLifeStats().settSilver(0);
		// 	p.getLifeStats().settBronze(0);
		// });

		l.getTeams().stream().forEach(t -> {
			t.getLifeStats().settPlayoffs(0);
			t.getLifeStats().settGold(0);
			t.getLifeStats().settSilver(0);
			t.getLifeStats().settBronze(0);
		});

		// l.getTeams().stream().forEach(t -> {
		// 	t.setLifeStats(new LifeStats());
		// });

		hag.getSeasons().stream().forEach(s -> {
			if(s.isEnded()) {

				// pilots
				// s.pilots().stream().forEach(p -> {
				// 	p.getLifeStats().incrementSeasons();
				// });

				// var group_pilots = s.getpPlayoff().pilots();
				// group_pilots.get(0).getLifeStats().incrementpGold();
				// group_pilots.get(1).getLifeStats().incrementpSilver();
				// group_pilots.get(2).getLifeStats().incrementpBronze();
				// group_pilots.stream().forEach(p -> {
				// 	p.getLifeStats().incrementpPlayoffs();
				// });

				var group_teams = s.gettPlayoff().teams(s.getYear());
				group_teams.get(0).getLifeStats().incrementtGold();
				group_teams.get(1).getLifeStats().incrementtSilver();
				group_teams.get(2).getLifeStats().incrementtBronze();
				// l.pilotsOf(group_teams.get(0), s.getYear()).forEach(p -> {
				// 	p.getLifeStats().incrementtPlayoffs();
				// 	p.getLifeStats().incrementtGold();
				// });

				// l.pilotsOf(group_teams.get(1), s.getYear()).forEach(p -> {
				// 	p.getLifeStats().incrementtPlayoffs();
				// 	p.getLifeStats().incrementtSilver();
				// });

				// l.pilotsOf(group_teams.get(2), s.getYear()).forEach(p -> {
				// 	p.getLifeStats().incrementtPlayoffs();
				// 	p.getLifeStats().incrementtBronze();
				// });

				// teams
				// s.teams().stream().forEach(t -> {
				// 	t.getLifeStats().incrementSeasons();
				// });

				// var group_teams = s.getpPlayoff().teams(s.getYear());
				// group_teams.get(0).getLifeStats().incrementpGold();
				// group_teams.get(1).getLifeStats().incrementpSilver();
				// group_teams.get(2).getLifeStats().incrementpBronze();
				// group_teams.stream().forEach(t -> {
				// 	t.getLifeStats().incrementpPlayoffs();
				// });

				// group_teams = s.gettPlayoff().teams(s.getYear());
				// group_teams.get(0).getLifeStats().incrementtGold();
				// group_teams.get(1).getLifeStats().incrementtSilver();
				// group_teams.get(2).getLifeStats().incrementtBronze();
				group_teams.stream().forEach(t -> {
					t.getLifeStats().incrementtPlayoffs();
				});

				

			}
		});
	}
}
