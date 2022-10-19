package fgenejfx.models.enums;

import java.util.List;
import java.util.stream.Collectors;

import fgenejfx.controllers.HistoryController;
import fgenejfx.controllers.League;
import fgenejfx.models.Season;

public enum SideType {

  PILOTSIDE{
    public List<String> list(){
      return League.get().getPilots().stream().sorted().map(p -> p.toString())
          .collect(Collectors.toList());
    }
  },
  
  TEAMSIDE{
    public List<String> list(){
      return League.get().getTeams().stream().sorted().map(t -> t.toString())
          .collect(Collectors.toList());
    }
  },
  
  SEASONSIDE{
    public List<String> list(){
      List<Season> ss = HistoryController.get().getSeasons();
      ss.add(League.get().getSeason());
      return ss.stream().sorted((s2,s1) -> s1.getYear().compareTo(s2.getYear()))
          .map(s -> s.toString())
          .collect(Collectors.toList());
    }
  };
  
  public abstract List<String> list();
}
