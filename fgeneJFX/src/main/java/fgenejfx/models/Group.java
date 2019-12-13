package fgenejfx.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Group implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Map<Pilot, RaceStats> pilots = new HashMap<>();
	
	public Group(List<Pilot> ps) {
		ps.forEach(p->pilots.put(p, new RaceStats()));
	}
	public RaceStats statsOf(Pilot p) {
		return pilots.get(p);
	}
	public Integer posOf(Pilot p) {
		List<Pilot> ps = pilots.entrySet().stream()
				.sorted(Map.Entry.comparingByValue())
				.map(e->e.getKey())
				.collect(Collectors.toList());
		return ps.indexOf(p)+1;
	}
	public Set<Pilot> getPilots(){
		return pilots.keySet();
	}
}
