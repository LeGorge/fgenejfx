package fgenejfx.models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fgenejfx.controllers.League;
import fgenejfx.exceptions.NotValidException;
import fgenejfx.jackson.MapDeserializer;
import fgenejfx.utils.Utils;

public class Group implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonDeserialize(using = MapDeserializer.class, keyAs = Pilot.class, contentAs = RaceStats.class)
	private Map<Pilot, RaceStats> pilotsMap = new HashMap<>();

	public Group() {
	}

	public Group(Set<Pilot> ps) {
		ps.forEach(p -> pilotsMap.put(p, new RaceStats()));
	}

	public void addPilot(Pilot p) throws NotValidException {
		if (this.pilotsMap.keySet().size() != 6) {
			pilotsMap.put(p, new RaceStats());
		} else {
			throw new NotValidException();
		}
	}

	// =============================================================================================
	// state of season related methods
	// =============================================================================================
	@JsonIgnore
	public boolean isEmpty() {
		long notEmpties = this.pilotsMap.values().stream().filter(s -> !s.isEmpty()).count();
		return notEmpties == 0;
	}

	// =============================================================================================
	// pilot related methods
	// =============================================================================================
	public RaceStats statsOf(Pilot p) {
		return pilotsMap.get(p);
	}

	public Integer posOf(Pilot p) {
		return this.pilotsOrdered().indexOf(p) + 1;
	}

	public Boolean contains(Pilot p) {
		return pilotsMap.containsKey(p);
	}

	public Boolean closeFight(Pilot p) {
		int index = this.posOf(p) - 1;
		if (index > 0) {
			Pilot aux = this.pilots().get(index - 1);
			if (Math.abs(statsOf(aux).getPts() - statsOf(p).getPts()) <= 10) {
				return true;
			}
		}
		if (index < 5) {
			Pilot aux = this.pilots().get(index + 1);
			if (Math.abs(statsOf(aux).getPts() - statsOf(p).getPts()) <= 10) {
				return true;
			}
		}
		return false;
	}

	public Pilot firstPilot() {
		return this.pilotsOrdered().get(0);
	}

	public List<Pilot> pilots() {
		return this.pilotsOrdered();
	}

	// =============================================================================================
	// team related methods
	// =============================================================================================
	public Boolean contains(Team t, Integer year) {
		return this.teamsSet(year).contains(t);
	}

	public List<Team> teams(Integer year) {
		return this.teamsSet(year).stream()
				.sorted((t2, t1) -> this.statsOf(t1, year).compareTo(this.statsOf(t2, year)))
				.collect(Collectors.toList());
	}

	public Integer posOf(Team t, Integer year) {
		return this.teams(year).indexOf(t) + 1;
	}

	public Team firstTeam(Integer year) {
		return this.teamsSet(year).stream()
				.sorted((t2, t1) -> this.statsOf(t1, year).compareTo(this.statsOf(t2, year))).findFirst()
				.get();
	}

	public RaceStats statsOf(Team t, Integer year) {
		RaceStats stat = new RaceStatsTeam();
		for (Pilot p : League.get().pilotsOf(t, year)) {
			if (this.pilots().contains(p)) {
				stat = RaceStats.sum(stat, this.statsOf(p));
			}
		}
		return stat;
	}

	// =============================================================================================
	// operations
	// =============================================================================================
	public void updateStat(Pilot p, RaceStats stat) {
		if (this.pilotsMap.containsKey(p)) {
			this.pilotsMap.put(p, stat);
		}
	}
	
	public void updatePer() {
//	  if(this.pilotsMap.values().stream().anyMatch(s -> s.per != 0)) { return; }
	  
	  var pilotSet = this.pilotsMap.keySet();
	  final int totalAi = pilotSet.stream().collect(Collectors.summingInt(Pilot::getAi));
	  final int totalPoints = pilotSet.stream()
			  .collect(Collectors.summingInt(p -> this.pilotsMap.get(p).getPts()));
	  
	  pilotSet.stream().forEach(p -> {
		  float percent = (p.getAi().floatValue()-100) / (totalAi-600);
		  int expected = Math.round(percent * totalPoints);
		  Integer per = this.pilotsMap.get(p).getPts() - expected;
		  this.pilotsMap.get(p).setPer(per);
	  });
	}
	
	// =============================================================================================
	// private methods
	// =============================================================================================
	private Set<Team> teamsSet(Integer year) {
		return pilotsMap.keySet().stream().map(p -> League.get().teamOf(p, year))
				.collect(Collectors.toSet());
	}

	private List<Pilot> pilotsOrdered() {
		return pilotsMap.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).map(e -> e.getKey())
				.collect(Collectors.toList());
	}
	
	//============================================================================================
	// simulation
	// ============================================================================================
	public void simulate() {
	  Iterator<RaceStats> i = this.pilotsMap.values().iterator();
	  i.next().setP1st(1);
	  i.next().setP2nd(1);
	  i.next().setP3rd(1);
	  i.next().setP4th(1);
	  i.next().setP5th(1);
	  i.next().setP6th(1);
	  
	  this.updatePer();
	}

	// =============================================================================================
	// getters & setters
	// =============================================================================================
	public Map<Pilot, RaceStats> getPilotsMap() {
		return this.pilotsMap;
	}
}
