package fgenejfx.models;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import fgenejfx.controllers.League;
import fgenejfx.exceptions.CopyException;
import fgenejfx.exceptions.NaoEncontradoException;
import fgenejfx.interfaces.StatsMonitorable;

public class HistoryAgent implements Serializable{
	private static final long serialVersionUID = 1L;
	private static HistoryAgent history;

	private Map<Pilot, PilotHistory> pilotHistory = new HashMap<>();
	private Map<Team, TeamHistory> teamHistory = new HashMap<>();
	private Map<Integer, ContractsHistory> contractHistory = new HashMap<>();
	
	//=========================================================================================== stats
	public Stats getStatByYear(StatsMonitorable obj, Integer year) throws NaoEncontradoException {
		if(obj instanceof Pilot) {
			return pilotHistory.get(obj).getStatByYear(year);
		}
		if(obj instanceof Team) {
			return teamHistory.get(obj).getStatByYear(year);
		}
		throw new NaoEncontradoException();
	}
	//=========================================================================================== pilot
	public void save(Pilot p, Stats s) throws CopyException {
		pilotHistory.computeIfAbsent(p, value -> new PilotHistory());
		pilotHistory.get(p).save(p, s);
	}
	public Integer getAiByYear(Pilot p, Integer year) throws NaoEncontradoException {
		return pilotHistory.get(p).getAiByYear(year);
	}
	public Set<Pilot> getAllPilots(){
		Set<Pilot> all = new HashSet<>(this.pilotHistory.keySet());
		all.addAll(ContractsAgent.get().getRookies());
		return all;
	}
	public Pilot getPilot(String name) {
		return this.pilotHistory.keySet().stream().filter(p->p.getName().equals(name)).findFirst().get();
	}
	//=========================================================================================== team
	public void save(Team t, Stats s) throws CopyException {
		teamHistory.computeIfAbsent(t, value -> new TeamHistory());
		teamHistory.get(t).save(t, s);
	}
	public EnumMap<Powers, Double> getPowersByYear(Team t, Integer year) throws NaoEncontradoException {
		return teamHistory.get(t).getPowersByYear(year);
	}
	public Set<Team> getAllTeams(){
		Set<Team> all = this.teamHistory.keySet();
		if(all.isEmpty()) {
			return TeamsEnum.create(); 
		}else {
			return all;
		}
	}
	public Team get(TeamsEnum tEnum) {
		return this.teamHistory.keySet().stream().filter(t->t.getName() == tEnum).findFirst().get();
	}
	//=========================================================================================== contracts
	public void save(Set<Contract> contracts) {
		int year = League.get().getYear();
		contractHistory.computeIfAbsent(year, value -> new ContractsHistory());
		contractHistory.get(year).save(contracts);
	}
	public Team getTeamOf(Integer year, Pilot p) throws NoSuchElementException {
		return contractHistory.get(year).getTeamOf(p);
	}
	public List<Pilot> getPilotsOf(Integer year, Team t) throws NoSuchElementException {
		return contractHistory.get(year).getPilotsOf(t);
	}
	//=========================================================================================== get singleton
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
}
