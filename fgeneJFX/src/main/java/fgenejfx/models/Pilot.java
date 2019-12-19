package fgenejfx.models;

import java.io.Serializable;
import java.util.NoSuchElementException;

import fgenejfx.controllers.League;
import fgenejfx.interfaces.StatsMonitorable;
import fgenejfx.utils.Utils;

public class Pilot implements Serializable, StatsMonitorable {
	private static final long serialVersionUID = 1L;
	public static final Integer MAX_YEARS_ON_CAREER = 18;

	private String name;
	private Integer AI;
	private Double xp = 0.0;
	private Integer rookieYear = League.get().getYear();
	private LifeStats lifeStats = new LifeStats();
	private Stats stats = new Stats();
	
	//=========================================================================================== operations
	public Boolean isRookie() {
		return League.get().getYear().equals(rookieYear);
	}
	
	public Boolean isActive() {
		return MAX_YEARS_ON_CAREER > (League.get().getYear()-rookieYear);
	}
	
	public Integer getYearsUntilRetirement() {
		return MAX_YEARS_ON_CAREER - (League.get().getYear()-rookieYear);
	}
	
	//=========================================================================================== get singleton
	
	public static Pilot get(String name) throws NoSuchElementException{
		return League.get().getPilot(name);
	}
	
	//=========================================================================================== crud
	public Pilot(String name) {
		this.name = name;
		this.AI = Utils.genGaussian(103, 3);
	}

	public Integer getRookieYear() {
		return rookieYear;
	}
	
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
	public String toString() {
		return name;
	}
}
