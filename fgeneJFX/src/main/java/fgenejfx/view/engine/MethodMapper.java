package fgenejfx.view.engine;

import java.util.LinkedHashMap;

import fgenejfx.models.Powers;
import fgenejfx.models.enums.LeagueTime;
import fgenejfx.models.enums.MethodSelector;

public class MethodMapper {

  // ============================================================================================
  // by Season Stats
  // ============================================================================================
  public static LinkedHashMap<String, Object[]> bySeasonStat(Integer year, LeagueTime time,
      MethodSelector sel){
    LinkedHashMap<String, Object[]> mapping = new LinkedHashMap<>(statOf(year, time));
    mapping.put(sel.getCmd(), null);
    return mapping;
  }
  
  public static LinkedHashMap<String, Object[]> bySeasonPower(Integer year, Powers p){
    LinkedHashMap<String, Object[]> mapping = new LinkedHashMap<>();
    if(p != null) {
      Object[] ptrParams1 = {year, "this"};
      mapping.put("powers", ptrParams1);
      Object[] ptrParams2 = {p};
      mapping.put("get", ptrParams2);
    }else {
      Object[] ptrParams1 = {year, "this"};
      mapping.put("carPower", ptrParams1);
    }
    return mapping;
  }
  
  // ============================================================================================
  // Info
  // ============================================================================================
  public static LinkedHashMap<String, Object[]> teamOf(Integer year){
    LinkedHashMap<String, Object[]> mapping = new LinkedHashMap<>();
    Object[] teamParams = {"this",year};
    mapping.put("teamOf", teamParams);
    return mapping;
  }
  
  public static LinkedHashMap<String, Object[]> posOf(Integer year){
    LinkedHashMap<String, Object[]> mapping = new LinkedHashMap<>();
    Object[] posParams1 = {year};
    mapping.put("season", posParams1);
    Object[] posParams2 = {"this"};
    mapping.put("topPosOf", posParams2);
    return mapping;
  }
  
  public static LinkedHashMap<String, Object[]> scoutReportOf(Integer year){
    LinkedHashMap<String, Object[]> mapping = new LinkedHashMap<>();
    Object[] posParams1 = {year, "this"};
    mapping.put("scoutReportOf", posParams1);
    return mapping;
  }
  
  // ============================================================================================
  // Life Stats
  // ============================================================================================
  public static LinkedHashMap<String, Object[]> lifeStat(Integer year, LeagueTime time,
      MethodSelector sel){
    LinkedHashMap<String, Object[]> mapping = new LinkedHashMap<>();
    mapping.put("getStats", null);
    mapping.put(time.getCmd2(), null);
    mapping.put(sel.getCmd(), null);
    return mapping;
  }
  
  // ============================================================================================
  // privates
  // ============================================================================================
  private static LinkedHashMap<String, Object[]> statOf(Integer year, LeagueTime time){
    LinkedHashMap<String, Object[]> mapping = new LinkedHashMap<>();
	  Object[] ptrParams1 = {year};
	  mapping.put("season", ptrParams1);
	  Object[] ptrParams2 = {"this", time};
	  mapping.put(time.getCmd(), ptrParams2);
	  return mapping;
  }
  
}
