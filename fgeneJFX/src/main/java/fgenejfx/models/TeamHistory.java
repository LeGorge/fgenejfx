package fgenejfx.models;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import fgenejfx.controllers.League;
import fgenejfx.exceptions.CopyException;
import fgenejfx.utils.Utils;

public class TeamHistory implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected TeamHistory() {
	}
	
	private Map<Integer, Stats> stats = new HashMap<Integer, Stats>();
	private Map<Integer, EnumMap<Powers, Double>> powers = new HashMap<Integer, EnumMap<Powers, Double>>();
	
	public Stats getStatByYear(Integer year) {
		return stats.get(year);
	}
	public EnumMap<Powers, Double> getPowersByYear(Integer year) {
		return powers.get(year);
	}
	
	public void save(Team t, Stats s) throws CopyException {
		powers.put(League.get().getYear(), Utils.copy(t.getPowers()));
		stats.put(League.get().getYear(), Utils.copy(s));
	}
}
