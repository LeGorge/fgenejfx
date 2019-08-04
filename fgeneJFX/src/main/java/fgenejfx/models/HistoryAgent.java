package fgenejfx.models;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fgenejfx.exceptions.CopyException;
import fgenejfx.exceptions.NaoEncontradoException;
import fgenejfx.interfaces.StatsMonitorable;

public class HistoryAgent implements Serializable{
	private static final long serialVersionUID = 1L;

	private static HistoryAgent history;

	private Map<Pilot, PilotHistory> pilotHistory = new HashMap<>();
	private Map<Team, TeamHistory> teamHistory = new HashMap<>();
	private Map<Integer, List<Contract>> contractHistory = new HashMap<>();
	
	private HistoryAgent() {
		HistoryAgent.history = this;
	}
	
	public void save(Integer year, Pilot p, Stats s) throws CopyException {
		pilotHistory.computeIfAbsent(p, value -> new PilotHistory());
		pilotHistory.get(p).save(year, p, s);
	}
	public void save(Integer year, Team t, Stats s) throws CopyException {
		teamHistory.computeIfAbsent(t, value -> new TeamHistory());
		teamHistory.get(t).save(year, t, s);
	}
	public void save(Integer year, List<Contract> c) throws CopyException {
		contractHistory.put(year, c);
	}
	
	public Stats getStatByYear(StatsMonitorable obj, Integer year) throws NaoEncontradoException {
		if(obj instanceof Pilot) {
			return pilotHistory.get(obj).getStatByYear(year);
		}
		if(obj instanceof Team) {
			return teamHistory.get(obj).getStatByYear(year);
		}
		throw new NaoEncontradoException();
	}
	
	public Integer getAiByYear(Pilot p, Integer year) throws NaoEncontradoException {
		return pilotHistory.get(p).getAiByYear(year);
	}
	public EnumMap<Powers, Double> getPowersByYear(Team t, Integer year) throws NaoEncontradoException {
		return teamHistory.get(t).getPowersByYear(year);
	}
	
	public static HistoryAgent get() {
		if(history == null) {
			new HistoryAgent();
		}
		return history;
	}
}
