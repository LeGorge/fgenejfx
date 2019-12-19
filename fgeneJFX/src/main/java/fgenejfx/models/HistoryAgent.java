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
//	private Map<Integer, Season> seasons = new HashMap<>();
	
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
		if(this.pilotHistory.get(p) == null) {
			this.pilotHistory.put(p, new PilotHistory());
		}
		return this.pilotHistory.get(p);
	}
	
	public TeamHistory getTeamHistory(Team t) {
		if(this.teamHistory.get(t) == null) {
			this.teamHistory.put(t, new TeamHistory());
		}
		return this.teamHistory.get(t);
	}
	
	public ContractsHistory getContractsHistory(Integer i) {
		if(this.contractHistory.get(i) == null) {
			this.contractHistory.put(i, new ContractsHistory());
		}
		return this.contractHistory.get(i);
	}
}
