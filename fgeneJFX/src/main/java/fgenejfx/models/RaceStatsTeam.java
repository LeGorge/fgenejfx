package fgenejfx.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RaceStatsTeam extends RaceStats {

	private static final long serialVersionUID = 1L;

	@Override
	@JsonIgnore
	public Double getPtRate() {
		return getTotalRaces() != 0 ? 
			new Double( getPts() / (getTotalRaces()/2) / 13 ) : 
			0.0d;
	}
	
	@Override
	@JsonIgnore
	public Double getWinRate() {
		return getTotalRaces() != 0 ? 
			new Double(p1st / (getTotalRaces()/2) ) : 
			0.0d ;
	}
}
