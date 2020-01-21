package fgenejfx.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import fgenejfx.controllers.GenerallyFilesController;
import fgenejfx.controllers.League;
import fgenejfx.exceptions.NotValidException;
import fgenejfx.models.enums.OpEnum;

public class Season implements Serializable {
  private static final long serialVersionUID = 1L;

  public static Season get(Integer year) {
    return League.get().getYear().equals(year) ? League.get().getSeason()
        : HistoryAgent.get().getSeasons().stream().filter(s -> s.getYear().equals(year))
            .findFirst().get();
  }

  private Integer year;

  private Group[] season = new Group[6];

  private Group pPlayoff;
  private Group tPlayoff;

  // ============================================================================================
  // pilot related methods
  // ============================================================================================
  public Set<Pilot> pilots() {
    return Arrays.stream(season).flatMap(g -> g.pilots().stream()).collect(Collectors.toSet());
  }

  public RaceStats seasonStatsOf(Pilot p) throws NoSuchElementException {
    return Arrays.stream(season).filter(g -> g.statsOf(p) != null).findFirst().get().statsOf(p);
  }

  public Group seasonGroupOf(Pilot p) throws NoSuchElementException {
    return Arrays.stream(season).filter(g -> g.contains(p)).findFirst().get();
  }

  // ============================================================================================
  // team related methods
  // ============================================================================================
  public Set<Team> teams() {
    return League.get().getTeams();
  }

  public RaceStats seasonStatsOf(Team t) throws NoSuchElementException {
    return Arrays.stream(season).filter(g -> g.contains(t, this.year))
        .findFirst().get().statsOf(t, this.year);
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

  // ============================================================================================
  // state machine
  // ============================================================================================
  public Boolean ended() {
    if (this.inPlayoffs()) {
      return !this.pPlayoff.isEmpty() && !this.tPlayoff.isEmpty();
    } else {
      return false;
    }
  }

  public Boolean playoffReady() {
    if (!this.inPlayoffs()) {
      long count = Arrays.stream(this.season).filter(g -> g.isEmpty()).count();
      return count == 0;
    } else {
      return false;
    }
  }

  public Boolean inPlayoffs() {
    return this.pPlayoff != null && this.tPlayoff != null;
  }
  
  public void update() throws NotValidException {
    if(!this.ended()) {
      
      if(!this.inPlayoffs()) {
        this.updateSeason();
        
        if(this.playoffReady()) {
          this.startPlayoffs();
        }
      }else {
        
        if(this.tPlayoff.isEmpty()) {
          updateTplayoff();
          
        }else {
          updatePplayoff();
        }
      }
    }
  }

  // ============================================================================================
  // mid-season operations
  // ============================================================================================
  private RaceStats total(Pilot p) {
    RaceStats result = this.seasonStatsOf(p);
    RaceStats tplayoff = this.tPlayoff.statsOf(p);
    if(tplayoff != null) {
      result = RaceStats.sum(result, tplayoff);
    }
    
    RaceStats pplayoff = this.pPlayoff.statsOf(p);
    if(pplayoff != null) {
      result = RaceStats.sum(result, pplayoff);
    }
    
    return RaceStats.sum(result, p.getStats().getStatsTotals());
  }
  
  private void updateSeason() {
    if (!this.ended()) {
      if (!inPlayoffs()) {
        Arrays.stream(this.season).forEach(g -> {
          g.pilots().stream().forEach(p -> {
            RaceStats read = GenerallyFilesController.readDriver(p.getName());
            g.updateStat(p,
                RaceStats.subtract(read, p.getStats().getStatsTotals()));
          });
        });
      }
    }
  }
  
  private void updateTplayoff() {
    this.tPlayoff.pilots().stream().forEach(p -> {
      RaceStats read = GenerallyFilesController.readDriver(p.getName());
      this.tPlayoff.updateStat(p,RaceStats.subtract(read, this.total(p)));
    });
  }
  
  private void updatePplayoff() {
    if (!this.ended() && inPlayoffs()) {
      this.pPlayoff.pilots().stream().forEach(p -> {
        RaceStats read = GenerallyFilesController.readDriver(p.getName());
        this.pPlayoff.updateStat(p,RaceStats.subtract(read, this.total(p)));
      });
    }
  }
        
  public void startPlayoffs() throws NotValidException {
    // pplayoff
    this.pPlayoff = new Group();
    for (Group g : season) {
      this.pPlayoff.addPilot(g.firstPilot());
    }

    // tplayoff
    Set<Pilot> ps = this.teams().stream()
        .sorted((t2, t1) -> this.seasonStatsOf(t1).compareTo(this.seasonStatsOf(t2))).limit(3)
        .flatMap(t -> League.get().pilotsOf(t, year).stream()).collect(Collectors.toSet());
    this.tPlayoff = new Group(ps);
  }

  // ============================================================================================
  // season creation
  // ============================================================================================
  public Season() {
    League l = League.get();
    this.year = l.getYear();
    List<Team> teams = new ArrayList<>(l.getTeams());
    Collections.shuffle(teams);
    buildGroups(teams);
  }

  private void buildGroups(List<Team> teams) {
    for (int i = 0; i < season.length; i++) {
      season[i] = new Group(
          teams.subList(i * 3, i * 3 + 3).stream().map(t -> ContractsAgent.get().pilotsOf(t))
              .flatMap(l -> l.stream()).collect(Collectors.toSet()));
    }
  }
  
  // ============================================================================================
  // getters and setters
  // ============================================================================================
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
}
