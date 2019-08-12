package fgenejfx.models;

import java.io.Serializable;
import java.util.EnumMap;

import fgenejfx.interfaces.StatsMonitorable;

public class Team implements Serializable, StatsMonitorable{
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private LifeStats lifeStats = new LifeStats();
	private Stats stats = new Stats();
	private EnumMap<Powers, Double> powers = new EnumMap<>(Powers.class);
	
	public Team() {
	}
	public Team(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public EnumMap<Powers, Double> getPowers() {
		return powers;
	}

	public void setPowers(EnumMap<Powers, Double> powers) {
		this.powers = powers;
	}
	
}
