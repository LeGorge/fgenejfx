package fgenejfx.models;

import java.io.Serializable;

import fgenejfx.interfaces.StatsMonitorable;
import fgenejfx.utils.Utils;

public class Pilot implements Serializable, StatsMonitorable {

	private static final long serialVersionUID = 1L;

	private String name;
	private Integer AI;
	private Double xp = 0.0;
	private Integer rookieYear;
	
	public Integer getRookieYear() {
		return rookieYear;
	}

	public void setRookieYear(Integer rookieYear) {
		this.rookieYear = rookieYear;
	}

	private LifeStats lifeStats = new LifeStats();
	private Stats stats = new Stats();
	
	public Pilot() {
	}
	
	public Pilot(String name) {
		this.name = name;
		this.AI = Utils.genGaussian(103, 3);
	}
	
//	public Integer getCareerLeft() {
//		return 18 - this.stats.size();
//	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAI() {
		return AI;
	}

	public void setAI(Integer aI) {
		AI = aI;
	}

	public Double getXp() {
		return xp;
	}

	public void setXp(Double xp) {
		this.xp = xp;
	}


	public LifeStats getLifeStats() {
		return lifeStats;
	}

	public void setLifeStats(LifeStats lifeStats) {
		this.lifeStats = lifeStats;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Pilot other = (Pilot) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
