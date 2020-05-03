package fgenejfx.view.engine;

import java.util.LinkedHashMap;

import fgenejfx.models.enums.LeagueTime;
import fgenejfx.models.enums.MethodSelector;

public class MethodMapper {

  // ============================================================================================
  // by Season Stats
  // ============================================================================================
  public static LinkedHashMap<String, Object[]> bySeasonStat(Integer year, LeagueTime time,
      MethodSelector sel){
    LinkedHashMap<String, Object[]> map = new LinkedHashMap<>(statOf(year, time));
    map.put(sel.getCmd(), null);
    return map;
  }
  
  // ============================================================================================
  // Info
  // ============================================================================================
  public static LinkedHashMap<String, Object[]> teamOf(Integer year){
    LinkedHashMap<String, Object[]> teamOf = new LinkedHashMap<>();
    Object[] teamParams = {"this",year};
    teamOf.put("teamOf", teamParams);
    return teamOf;
  }
  
  // ============================================================================================
  // Life Stats
  // ============================================================================================
  public static LinkedHashMap<String, Object[]> lifeStat(Integer year, LeagueTime time,
      MethodSelector sel){
    LinkedHashMap<String, Object[]> ptr = new LinkedHashMap<>();
    ptr.put("getStats", null);
    ptr.put(time.getCmd2(), null);
    ptr.put(sel.getCmd(), null);
    return ptr;
  }
  
  // ============================================================================================
  // privates
  // ============================================================================================
  private static LinkedHashMap<String, Object[]> statOf(Integer year, LeagueTime time){
    LinkedHashMap<String, Object[]> statOf = new LinkedHashMap<>();
	  Object[] ptrParams1 = {year};
	  statOf.put("season", ptrParams1);
	  Object[] ptrParams2 = {"this", time};
	  statOf.put(time.getCmd(), ptrParams2);
	  return statOf;
  }
  
}
