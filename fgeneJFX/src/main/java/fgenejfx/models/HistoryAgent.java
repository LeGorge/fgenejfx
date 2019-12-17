package fgenejfx.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HistoryAgent implements Serializable{
	private static final long serialVersionUID = 1L;
	private static HistoryAgent history;

	private Map<Pilot, PilotHistory> pilotHistory = new HashMap<>();
	private Map<Team, TeamHistory> teamHistory = new HashMap<>();
	private Map<Integer, ContractsHistory> contractHistory = new HashMap<>();
	
	public Set<Pilot> getPilots(){
		Set<Pilot> all = new HashSet<>(this.pilotHistory.keySet());
		return all;
	}
	public Set<Team> getTeams(){
		Set<Team> all = this.teamHistory.keySet();
		return all;
	}
	
	//=========================================================================================== get singletons
	private HistoryAgent() {
		HistoryAgent.history = this;
	}
	public static HistoryAgent get() {
		if(history == null) {
			new HistoryAgent();
		}
		return history;
	}
	public static void set(HistoryAgent ag) {
		if(history == null) {
			history = ag;
		}
	}
	
	public PilotHistory getPilotHistory(Pilot p) {
		return this.pilotHistory.get(p);
	}
	
	public TeamHistory getTeamHistory(Team t) {
		return this.teamHistory.get(t);
	}
	
	public ContractsHistory getContractsHistory(Integer i) {
		return this.contractHistory.get(i);
	}
}
