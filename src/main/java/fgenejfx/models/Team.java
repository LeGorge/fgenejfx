package fgenejfx.models;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import fgenejfx.controllers.League;
import fgenejfx.interfaces.StatsMonitorable;
import fgenejfx.models.enums.OpEnum;
import fgenejfx.models.enums.TeamsEnum;

public class Team implements Serializable, StatsMonitorable, Comparable<Team> {
	private static final long serialVersionUID = 1L;

	private TeamsEnum name;

	private LifeStats lifeStats = new LifeStats();
	private Stats stats = new Stats();
	private EnumMap<Powers, Double> powers = new EnumMap<>(Powers.class);

	// ===========================================================================================
	// powers
	public Double power(Powers p) {
		return this.powers.get(p);
	}

	public void updatePowers(Integer times, OpEnum op) {
		for (int i = 0; i < times; i++) {
			Powers.update(this.powers, op);
		}
	}

	public Integer carPower() {
		return Powers.carPower(this.powers);
	}

	// ===========================================================================================
	// get team
	public static Team get(TeamsEnum name) throws NoSuchElementException {
		return League.get().getTeams().stream().filter(t -> t.name == name).findFirst().get();
	}

	public static Team get(String name) throws NoSuchElementException {
		return Team.get(TeamsEnum.valueOf(name.toUpperCase()));
	}

	public Team() {
	}

	public Team(TeamsEnum tEnum) {
		this.name = tEnum;
		for (Powers p : Powers.values()) {
			this.powers.put(p, p.def);
		}
	}

	// ===========================================================================================
	// getters & setters
	public String getName() {
		return name.toString();
	}
//	public TeamsEnum getName() {
//		return name.toString();
//	}

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

  @Override
  public int compareTo(Team o) {
    return this.name.toString().compareTo(o.name.toString());
  }
}
