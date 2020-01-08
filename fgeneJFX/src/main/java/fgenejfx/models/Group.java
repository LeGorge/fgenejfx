package fgenejfx.models;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fgenejfx.controllers.League;
import fgenejfx.jackson.MapDeserializer;

public class Group implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@JsonDeserialize(using = MapDeserializer.class, keyAs = Pilot.class, contentAs = RaceStats.class)
	private Map<Pilot, RaceStats> pilotsMap = new HashMap<>();
	
	public Group() {
	}
	public Group(Set<Pilot> ps) {
		ps.forEach(p->pilotsMap.put(p, new RaceStats()));
	}
	
	//=========================================================================================== pilot
	public RaceStats statsOf(Pilot p) {
		return pilotsMap.get(p);
	}
	
	public Integer posOf(Pilot p) {
		return this.pilotsOrdered().indexOf(p)+1;
	}
	
	public Boolean contains(Pilot p) {
		return pilotsMap.containsKey(p);
	}
	
	public Boolean closeFight(Pilot p) {
		int index = this.posOf(p) - 1;
		if(index > 0) {
			Pilot aux = this.pilots().get(index - 1);
			if(Math.abs(statsOf(aux).getPts() - statsOf(p).getPts()) <= 10) {
				return true;
			}
		}
		if(index < 5) {
			Pilot aux = this.pilots().get(index + 1);
			if(Math.abs(statsOf(aux).getPts() - statsOf(p).getPts()) <= 10) {
				return true;
			}
		}
		return false;
	}
	
	public List<Pilot> pilots(){
		return this.pilotsOrdered();
	}
	
	//=========================================================================================== team
	public List<Team> teams(Integer year){
		return this.teamsSet(year).stream()
				.sorted((t2, t1) -> this.statsOf(t1,year).compareTo(this.statsOf(t2,year)))
				.collect(Collectors.toList());
	}
	
	public Integer posOf(Team t, Integer year) {
		return this.teams(year).indexOf(t)+1;
	}
	
	public Team firstTeam(Integer year) {
		return this.teamsSet(year).stream()
			.sorted((t2, t1) -> this.statsOf(t1,year).compareTo(this.statsOf(t2,year)))
			.findFirst().get();
	}
	
	public RaceStats statsOf(Team t,Integer year) {
		RaceStats stat = new RaceStatsTeam();
		for (Pilot p : League.get().pilotsOf(t,year)) {
			if(this.pilots().contains(p)){
				stat = RaceStats.somarStats(stat, this.statsOf(p), true);
			}
		}
		return stat;
	}
	
	//=========================================================================================== privates
	private Set<Team> teamsSet(Integer year) {
		return pilotsMap.keySet().stream()
				.map(p -> League.get().teamOf(p,year))
				.collect(Collectors.toSet());
	}
	private List<Pilot> pilotsOrdered(){
		return pilotsMap.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.map(e->e.getKey())
				.collect(Collectors.toList());
	}
	
	//=========================================================================================== getters & setters
	public Map<Pilot, RaceStats> getPilotsMap(){
		return this.pilotsMap;
	}
}
