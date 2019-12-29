package fgenejfx.models;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fgenejfx.controllers.League;
import fgenejfx.interfaces.StatsMonitorable;

public class Team implements Serializable, StatsMonitorable{
	private static final long serialVersionUID = 1L;
	
	private TeamsEnum name;
	
	private LifeStats lifeStats = new LifeStats();
	private Stats stats = new Stats();
	private EnumMap<Powers, Double> powers = new EnumMap<>(Powers.class);
	
	@JsonIgnore
	public static Team get(TeamsEnum name) throws NoSuchElementException {
		return League.get().getTeams().stream().filter(t->t.getName() == name).findFirst().get();
	}
	
	@JsonIgnore
	public static Team get(String name) throws NoSuchElementException {
		return Team.get(TeamsEnum.valueOf(name));
	}
	
	public Team() {
	}
	public Team(TeamsEnum tEnum) {
		this.name = tEnum;
		for (Powers p : Powers.values()) {
			this.powers.put(p, p.def);
		}
	}

	public TeamsEnum getName() {
		return name;
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
	@Override
	public String toString() {
		return name.toString();
	}
}
