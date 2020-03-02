package fgenejfx.models;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fgenejfx.controllers.League;
import fgenejfx.interfaces.StatsMonitorable;
import fgenejfx.utils.Utils;

public class Pilot implements Serializable, StatsMonitorable, Comparable<Pilot> {
	private static final long serialVersionUID = 1L;
	public static final Integer MAX_YEARS_ON_CAREER = 18;

	private String name;
	private Integer ai;
	private Double xp = 0.0;
	private Integer rookieYear = League.get().getYear();
	private LifeStats lifeStats = new LifeStats();
	private Stats stats = new Stats();

	// ===========================================================================================
	// info
	@JsonIgnore
	public Boolean isRookie() {
		return League.get().getYear().equals(rookieYear);
	}

	@JsonIgnore
	public Boolean isActive() {
		return MAX_YEARS_ON_CAREER > (League.get().getYear() - rookieYear);
	}

	@JsonIgnore
	public Integer getYearsUntilRetirement() {
		return MAX_YEARS_ON_CAREER - (League.get().getYear() - rookieYear);
	}
	
	@JsonIgnore
	public Integer getYearsInTheLeague() {
	  return League.get().getYear() - rookieYear + 1;
	}

	// ===========================================================================================
	// operations
	public void updateAi(int seasonPlacing, int lastYearSeasonPlacing, int pplayoffPlacing,
			int lastYearPplayoffPlacing, boolean closeFight) {

		int potential = 0;

		// - 4 ai for champion repeaters
		if (pplayoffPlacing == 1 && lastYearPplayoffPlacing == 1) {
			this.ai -= 4;
		}

		// no more changes for champions
		if (pplayoffPlacing == 1) {
			return;
		}

		// - 1 ai for playoff repeaters
		if (seasonPlacing == 1 && lastYearSeasonPlacing == 1) {
			this.ai -= 1;
		}

		// + by age
		potential += this.timeInfluence();

		// + by position
		potential += this.seasonResultInfluence(seasonPlacing, closeFight);

		// apply potential
		this.applyPotential(potential);
	}

	// ===========================================================================================
	// privates
	private void applyPotential(int potential) {
		double rand = new Random().nextGaussian() * 0.5 + 0.3;
		if (rand < 0) {
			rand = 0.0;
		}
		this.xp += rand * potential;
		double aux = xp;
		Integer xpInt = (int) aux;
		this.ai += xpInt;
		this.xp -= xpInt;
	}

	private Integer timeInfluence() {
		Integer left = this.getYearsUntilRetirement();

		// flat ai increase of [2,3 or 4] for the first 6 years
		if (left > 12) {
			this.ai += new Random().nextInt(2) + 2;
		}

		// potential increase of 1 for 4 years after the first 6
		if (left > 9 && left < 12) {
			return 1;
		}

		return 0;
	}

	private Integer seasonResultInfluence(int seasonPlacing, boolean closeFight) {
		int result = 0;

		// 1st: 1 potential point ... 5th: 5 potential points
		int ptsBase[] = { 1, 2, 3, 4, 5, 6 };
		int pts = ptsBase[seasonPlacing - 1];
		result += pts;

		// 3 potential points if in a close fight for position
		if (closeFight) {
			result += 3;
		}

		return result;
	}

	// ===========================================================================================
	// get singleton
	public static Pilot get(String name) throws NoSuchElementException {
		return League.get().getPilots().stream().filter(p -> p.getName().equals(name)).findFirst()
				.get();
	}

	// ===========================================================================================
	// getters & setters
	public Pilot() {
	}

	public Pilot(String name) {
		this.name = name;
		this.ai = Utils.genGaussian(103, 3);
	}

	public Integer getRookieYear() {
		return rookieYear;
	}

	public void setRookieYear(Integer year) {
		this.rookieYear = year;
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

  @Override
  public int compareTo(Pilot o) {
    return this.name.compareTo(o.name);
  }
}
