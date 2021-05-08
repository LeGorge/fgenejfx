package fgenejfx;

import java.util.Arrays;

import fgenejfx.controllers.HistoryController;
import fgenejfx.controllers.League;
import fgenejfx.controllers.PersistanceController;
import fgenejfx.models.Group;

public class Patcher {

	public static void main(String[] args) {
		PersistanceController.load();
		
//		fixPer();
		
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

}
