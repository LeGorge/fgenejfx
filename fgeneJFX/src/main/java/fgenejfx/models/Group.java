package fgenejfx.models;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fgenejfx.jackson.MapDeserializer;

public class Group implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// @JsonSerialize(using = MapToArraySerializer.class)
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
		List<Pilot> ps = pilotsMap.entrySet().stream()
			.sorted(Map.Entry.comparingByValue())
			.map(e->e.getKey())
			.collect(Collectors.toList());
		return ps.indexOf(p)+1;
	}
	public Boolean contains(Pilot p) {
		return pilotsMap.containsKey(p);
	}
	public List<Pilot> pilots(){
		return pilotsMap.keySet().stream()
			.sorted(
				(p1, p2) -> this.pilotsMap.get(p1).compareTo(this.pilotsMap.get(p2))
			).collect(Collectors.toList());
	}
	//=========================================================================================== team
	public Set<Team> teams() {
		return pilotsMap.keySet().stream()
			.map(p -> ContractsAgent.get().teamOf(p))
			.collect(Collectors.toSet());
	}
	public Team firstTeam() {
		return this.teams().stream()
			.sorted((t1, t2) -> this.statsOf(t1).compareTo(this.statsOf(t2)))
			.findFirst().get();
	}
	public RaceStats statsOf(Team t) {
		RaceStats stat = new RaceStatsTeam();
		ContractsAgent.get().pilotsOf(t).forEach(p -> {
			RaceStats.somarStats(stat, this.statsOf(p), true);
		});
		return stat;
	}
	//=========================================================================================== getters & setters
	public Map<Pilot, RaceStats> getPilotsMap(){
		return this.pilotsMap;
	}
}
