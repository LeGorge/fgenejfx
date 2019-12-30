package fgenejfx.models;

import java.io.Serializable;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fgenejfx.controllers.League;
import fgenejfx.interfaces.StatsMonitorable;
import fgenejfx.utils.Utils;

public class Pilot implements Serializable, StatsMonitorable {
	private static final long serialVersionUID = 1L;
	public static final Integer MAX_YEARS_ON_CAREER = 18;

	private String name;
	private Integer ai;
	private Double xp = 0.0;
	private Integer rookieYear = League.get().getYear();
	private LifeStats lifeStats = new LifeStats();
	private Stats stats = new Stats();
	
	//=========================================================================================== operations
	@JsonIgnore
	public Boolean isRookie() {
		return League.get().getYear().equals(rookieYear);
	}
	
	@JsonIgnore
	public Boolean isActive() {
		return MAX_YEARS_ON_CAREER > (League.get().getYear()-rookieYear);
	}
	
	@JsonIgnore
	public Integer getYearsUntilRetirement() {
		return MAX_YEARS_ON_CAREER - (League.get().getYear()-rookieYear);
	}
	
	//=========================================================================================== get singleton
	
	public static Pilot get(String name) throws NoSuchElementException{
		return League.get().getPilots().stream().filter(p->p.getName().equals(name)).findFirst().get();
	}
	
	//=========================================================================================== crud
	public Pilot() {
	}
	public Pilot(String name) {
		this.name = name;
		this.ai = Utils.genGaussian(103, 3);
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
	
	public Integer getAi() {
		return ai;
	}
	
	public void setAi(Integer ai) {
		this.ai = ai;
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
