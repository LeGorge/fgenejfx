package fgenejfx.models;

import java.io.Serializable;

import fgenejfx.models.enums.OpEnum;

public class Stats implements Serializable {

	private static final long serialVersionUID = 1L;

	private RaceStats season = new RaceStats();
	private RaceStats pPlayoff = new RaceStats();
	private RaceStats tPlayoff = new RaceStats();
	
	public Stats() {
		// TODO Auto-generated constructor stub
	}
	
	public RaceStats getStatsTotals() {
		RaceStats r = RaceStats.somarStats(season, pPlayoff, OpEnum.SUM);
		return RaceStats.somarStats(r, tPlayoff, OpEnum.SUM);
	}


	public RaceStats getSeason() {
		return season;
	}

	public void setSeason(RaceStats season) {
		this.season = season;
	}

	public RaceStats getpPlayoff() {
		return pPlayoff;
	}

	public void setpPlayoff(RaceStats pPlayoff) {
		this.pPlayoff = pPlayoff;
	}

	public RaceStats gettPlayoff() {
		return tPlayoff;
	}

	public void settPlayoff(RaceStats tPlayoff) {
		this.tPlayoff = tPlayoff;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pPlayoff == null) ? 0 : pPlayoff.hashCode());
		result = prime * result + ((season == null) ? 0 : season.hashCode());
		result = prime * result + ((tPlayoff == null) ? 0 : tPlayoff.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stats other = (Stats) obj;
		if (pPlayoff == null) {
			if (other.pPlayoff != null)
				return false;
		} else if (!pPlayoff.equals(other.pPlayoff))
			return false;
		if (season == null) {
			if (other.season != null)
				return false;
		} else if (!season.equals(other.season))
			return false;
		if (tPlayoff == null) {
			if (other.tPlayoff != null)
				return false;
		} else if (!tPlayoff.equals(other.tPlayoff))
			return false;
		return true;
	}
}