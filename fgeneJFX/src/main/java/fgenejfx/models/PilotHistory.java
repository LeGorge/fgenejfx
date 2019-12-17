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
	
	//=========================================================================================== get
	public Stats getStatByYear(Integer year) {
		return stats.get(year);
	}
	public Integer getAiByYear(Integer year) {
		return ais.get(year);
	}
	
	//=========================================================================================== save
	public void saveAi(Integer year, Integer ai) throws CopyException {
		ais.put(year, ai);
	}
	
	public void saveStat(Integer year, Stats s) throws CopyException {
		stats.put(year, Utils.copy(s));
	}
	
}
