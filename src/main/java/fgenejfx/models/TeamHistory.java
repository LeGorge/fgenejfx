package fgenejfx.models;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import fgenejfx.exceptions.CopyException;
import fgenejfx.utils.Utils;

public class TeamHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	protected TeamHistory() {
	}

	private Map<Integer, Stats> stats = new HashMap<Integer, Stats>();
	private Map<Integer, EnumMap<Powers, Double>> powers = new HashMap<Integer, EnumMap<Powers, Double>>();

	// ===========================================================================================
	// get
	public Stats getStatByYear(Integer year) {
		return stats.get(year);
	}

	public EnumMap<Powers, Double> getPowersByYear(Integer year) {
		return powers.get(year);
	}

	// ===========================================================================================
	// save
	public void saveStat(Integer year, Stats s) throws CopyException {
		stats.put(year, Utils.copy(s));
	}

	public void savePowers(Integer year, EnumMap<Powers, Double> powers) throws CopyException {
		this.powers.put(year, Utils.copy(powers));
	}

	public Map<Integer, EnumMap<Powers, Double>> getPowers() {
		return this.powers;
	}

	public Map<Integer, Stats> getStats() {
		return this.stats;
	}
}
