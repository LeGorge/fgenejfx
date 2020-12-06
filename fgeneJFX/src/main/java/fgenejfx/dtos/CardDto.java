package fgenejfx.dtos;

import fgenejfx.controllers.League;
import fgenejfx.models.Pilot;
import fgenejfx.models.Team;
import fgenejfx.models.enums.LeagueTime;
import lombok.Data;

@Data
public class CardDto {

	private Integer year;
	
	private Pilot p1;
	private Pilot p2;
	
	private Team t;
	private Integer pts;
	private Integer p1st;
	
	public CardDto(Integer year, Pilot p1) {
		League l = League.get();
		this.year = year;
		this.p1 = p1;
		
		this.t = l.teamOf(p1, year);
		this.pts =  l.season(year).statsOf(p1, LeagueTime.PPLAYOFF).getPts();
		this.p1st =  l.season(year).statsOf(p1, LeagueTime.PPLAYOFF).getP1st();
	}
	public CardDto(Integer year, Team t) {
		League l = League.get();
		this.year = year;
		this.t = t;
		
		var pilots = League.get().pilotsOf(t, year);
		this.p1 = pilots.get(0);
		this.p2 = pilots.get(1);
		this.pts =  l.season(year).statsOf(t, LeagueTime.TPLAYOFF).getPts();
		this.p1st =  l.season(year).statsOf(t, LeagueTime.TPLAYOFF).getP1st();
	}
	
}
