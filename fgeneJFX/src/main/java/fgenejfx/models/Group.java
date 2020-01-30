package fgenejfx.models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
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
	  if(this.pilotsMap.values().stream().filter(s -> s.per != 0).findAny().isPresent()) {
	    return; //already calculated
	  }
	  Pilot ps[] = this.pilotsMap.keySet().stream().sorted().toArray(Pilot[]::new);
	  Double ai[] = this.pilotsMap.keySet().stream().sorted().map(p -> new Double(p.getAi()))
	      .toArray(Double[]::new);
	  Double pts[] = this.pilotsMap.keySet().stream().sorted().map(p -> this.statsOf(p).getPtRate())
	      .toArray(Double[]::new);
	  Double up = Arrays.stream(ai).max((n1,n2) -> n1.compareTo(n2)).get();
	  Double down = Arrays.stream(ai).min((n1,n2) -> n1.compareTo(n2)).get();
	  Double total = this.pilotsMap.values().stream()
	      .collect(Collectors.summingDouble(s -> s.getPtRate()));
	  
	  double[] arrayPreviewed = new double[6];
	  double somaPreviewed = 0;
	  for (int i = 0; i < ai.length; i++) {
	    double[] diff = new double[5];
	    for (int j = 0; j < ai.length; j++) {
	      if(i==j){
	        continue;
	      }else {
	        if(i<j){
	          diff[j-1] = ai[i]-ai[j];
	        }else{
	          diff[j] = ai[i]-ai[j];
	        }
	      }
	    }
	    double deviation = new StandardDeviation(false).evaluate(diff);
	    double diffMedia = new Mean().evaluate(diff);
	    
	    double max = up;
	    double min = down;
	    if(diffMedia > 0) {
	      min = down - diffMedia;
	      max = up + ((deviation * 2) - diffMedia);
	    }else {
	      if(diffMedia != 0) {
	        max = up - diffMedia;
	        min = down - ((deviation * 2) + diffMedia);
	      }
	    }
	    
	    double previewed = (ai[i] - min) / (max - min);
	    if(previewed < 0){
	      previewed = 0;
	    }
	    if(previewed > deviation * 2){
	      previewed = deviation * 2;
	    }
	    arrayPreviewed[i] = previewed;
	    somaPreviewed += previewed;
	  }
	  
	  for (int i = 0; i < arrayPreviewed.length; i++) {
	    double result = arrayPreviewed[i]*(total/somaPreviewed);
	    double per = Utils.round((pts[i] - result)*10, 1);
	    this.pilotsMap.get(ps[i]).setPer(per);
	  }
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

	// =============================================================================================
	// getters & setters
	// =============================================================================================
	public Map<Pilot, RaceStats> getPilotsMap() {
		return this.pilotsMap;
	}
}
