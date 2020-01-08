package fgenejfx.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RaceStatsTeam extends RaceStats {

	private static final long serialVersionUID = 1L;

	public RaceStatsTeam() {
	}
	
	public RaceStatsTeam(Integer p1st, Integer p2nd, Integer p3rd, Integer p4th, Integer p5th, Integer p6th) {
		super(p1st,p2nd,p3rd,p4th,p5th,p6th);
	}
	
	public RaceStatsTeam(RaceStats st) {
		super(st.p1st,st.p2nd,st.p3rd,st.p4th,st.p5th,st.p6th);
	}
	
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
