package fgenejfx.models;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
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

	@JsonIgnore
	public List<Pilot> getPilots(){
		return pilotsMap.keySet().stream().sorted(
			(p1, p2) -> this.pilotsMap.get(p1).compareTo(this.pilotsMap.get(p2))
			).collect(Collectors.toList());
	}

	public Map<Pilot, RaceStats> getPilotsMap(){
		return this.pilotsMap;
	}
	// public void setPilotsMap(Map<Pilot, RaceStats> pilotsMap){
	// 	this.pilotsMap = pilotsMap;
	// }
}
