package fgenejfx.view;

import java.util.LinkedHashMap;

public class MethodMapper {

  private static LinkedHashMap<String, Object[]> teamOf = null;
  private static LinkedHashMap<String, Object[]> seasonStatOf = null;
  private static LinkedHashMap<String, Object[]> ptr = null;
  private static LinkedHashMap<String, Object[]> winr = null;
  private static LinkedHashMap<String, Object[]> per = null;
  
  public static LinkedHashMap<String, Object[]> teamOf(Integer year){
    if(teamOf == null) {
      teamOf = new LinkedHashMap<>();
      Object[] teamParams = {"this",year};
      teamOf.put("teamOf", teamParams);
    }
    return teamOf;
  }
  public static LinkedHashMap<String, Object[]> seasonStatOf(Integer year){
    if(seasonStatOf == null) {
      seasonStatOf = new LinkedHashMap<>();
      Object[] ptrParams1 = {year};
      seasonStatOf.put("season", ptrParams1);
      Object[] ptrParams2 = {"this"};
      seasonStatOf.put("seasonStatsOf", ptrParams2);
    }
    return seasonStatOf;
  }
  public static LinkedHashMap<String, Object[]> ptr(Integer year){
    if(ptr == null) {
      ptr = new LinkedHashMap<>(seasonStatOf(year));
      ptr.put("getPtRate", null);
    }
    return ptr;
  }
  public static LinkedHashMap<String, Object[]> winr(Integer year){
    if(winr == null) {
      winr = new LinkedHashMap<>(seasonStatOf(year));
      winr.put("getWinRate", null);
    }
    return ptr;
  }
  public static LinkedHashMap<String, Object[]> per(Integer year){
    if(per == null) {
      per = new LinkedHashMap<>(seasonStatOf(year));
      per.put("getPer", null);
    }
    return ptr;
  }
}
