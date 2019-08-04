package fgenejfx.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import fgenejfx.exceptions.CopyException;
import fgenejfx.utils.Utils;

public class PilotHistory implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected PilotHistory() {
	}
	
	private Map<Integer, Integer> ais = new HashMap<>();
	private Map<Integer, Stats> stats = new HashMap<>();
	
	public Stats getStatByYear(Integer year) {
		return stats.get(year);
	}
	public Integer getAiByYear(Integer year) {
		return ais.get(year);
	}
	public void save(Integer year, Pilot p, Stats s) throws CopyException {
		ais.put(year, p.getAI());
		stats.put(year, Utils.copy(s));
	}
	
}
