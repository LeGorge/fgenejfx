package fgenejfx.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import fgenejfx.controllers.League;
import fgenejfx.exceptions.NotValidException;

public class Season implements Serializable{
	private static final long serialVersionUID = 1L;
	public static Season get(Integer year){
		return League.get().getYear().equals(year) ?
			League.get().getSeason() :
			HistoryAgent.get().getSeasons().stream().filter(s -> s.getYear().equals(year)).findFirst().get();
	}
	
	private Integer year;
	
	private Group[] season = new Group[6];
	
	private Group pPlayoff;
	private Group tPlayoff;


	//=========================================================================================== pilots
	public Set<Pilot> pilots() {
		return Arrays.stream(season).flatMap(g -> g.pilots().stream()).collect(Collectors.toSet());
	}
	
	public RaceStats seasonStatsOf(Pilot p) throws NoSuchElementException {
		return Arrays.stream(season).filter(g->g.statsOf(p)!=null).findFirst().get().statsOf(p);
	}
	
	public Group seasonGroupOf(Pilot p) throws NoSuchElementException {
		return Arrays.stream(season).filter(g->g.contains(p)).findFirst().get();
	}
	
	//=========================================================================================== teams
	public Set<Team> teams(){
		return League.get().getTeams();
	}
	
	public RaceStats seasonStatsOf(Team t) throws NoSuchElementException {
		return Arrays.stream(season)
				.filter(g->g.contains(t, this.year)).findFirst().get()
				.statsOf(t,this.year);
	}
	
	public List<Team> pPlayoffTeams() {
		return pPlayoff.teams(this.year);
	}
	
	public Team tChamp() {
		return tPlayoff.firstTeam(this.year);
	}
	
	public Team pChamp() {
		return pPlayoff.firstTeam(this.year);
	}
	
	//=========================================================================================== op
	public void startPlayoffs() throws NotValidException{
		//pplayoff
		for (Group g : season) {
			this.pPlayoff.addPilot(g.firstPilot());
		}
		
		//tplayoff
		Set<Pilot> ps = this.teams().stream()
				.sorted((t2,t1) -> this.seasonStatsOf(t1).compareTo(this.seasonStatsOf(t2)))
				.limit(3)
				.flatMap(t -> League.get().pilotsOf(t, year).stream())
				.collect(Collectors.toSet());
		this.tPlayoff = new Group(ps);
	}
	
	//=========================================================================================== creation
	public Season() {
		League l = League.get();
		this.year = l.getYear();
		List<Team> teams = new ArrayList<>(l.getTeams());
		Collections.shuffle(teams);
		buildGroups(teams);
	}
	
	private void buildGroups(List<Team> teams) {
		for (int i = 0; i < season.length; i++) {
			season[i] = new Group(teams.subList(i*3, i*3+3).stream()
			.map(t->ContractsAgent.get().pilotsOf(t))
			.flatMap(l->l.stream())
			.collect(Collectors.toSet()));
		}
	}
	
	//=========================================================================================== getters and setters
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Group[] getSeason() {
		return season;
	}
	public void setSeason(Group[] season) {
		this.season = season;
	}
	public Group getpPlayoff() {
		return pPlayoff;
	}
	public void setpPlayoff(Group pPlayoff) {
		this.pPlayoff = pPlayoff;
	}
	public Group gettPlayoff() {
		return tPlayoff;
	}
	public void settPlayoff(Group tPlayoff) {
		this.tPlayoff = tPlayoff;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Season other = (Season) obj;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.year.toString();
	}
	public Boolean ended() {
		return true;
	}
	
}
