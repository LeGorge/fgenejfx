package fgenejfx.view.engine;

import java.util.LinkedHashMap;

import fgenejfx.models.enums.LeagueTime;

public class MethodMapper {

  private static LinkedHashMap<String, Object[]> pts = null;
  private static LinkedHashMap<String, Object[]> p1st = null;
  private static LinkedHashMap<String, Object[]> p2nd = null;
  private static LinkedHashMap<String, Object[]> p3rd = null;
  private static LinkedHashMap<String, Object[]> p4th = null;
  private static LinkedHashMap<String, Object[]> p5th = null;
  private static LinkedHashMap<String, Object[]> p6th = null;
  
  private static LinkedHashMap<String, Object[]> ptr = null;
  private static LinkedHashMap<String, Object[]> winr = null;
  private static LinkedHashMap<String, Object[]> per = null;
  
  private static LinkedHashMap<String, Object[]> teamOf = null;
  
  private static LinkedHashMap<String, Object[]> seasonStatOf = null;
  private static LinkedHashMap<String, Object[]> pplayoffStatOf = null;
  private static LinkedHashMap<String, Object[]> tplayoffStatOf = null;
  
  // ============================================================================================
  // Absolute Stats
  // ============================================================================================
  public static LinkedHashMap<String, Object[]> pts(Integer year, LeagueTime time){
    if(pts == null) {
      switch (time) {
      case SEASON:
        pts = new LinkedHashMap<>(seasonStatOf(year));
        break;
      case PPLAYOFF:
        pts = new LinkedHashMap<>(pplayoffStatOf(year));
        break;
      case TPLAYOFF:
        pts = new LinkedHashMap<>(tplayoffStatOf(year));
        break;
      }
      pts.put("getPts", null);
    }
    return pts;
  }
  public static LinkedHashMap<String, Object[]> p1st(Integer year){
    if(p1st == null) {
      p1st = new LinkedHashMap<>(seasonStatOf(year));
      p1st.put("getP1st", null);
    }
    return p1st;
  }
  public static LinkedHashMap<String, Object[]> p2nd(Integer year){
    if(p2nd == null) {
      p2nd = new LinkedHashMap<>(seasonStatOf(year));
      p2nd.put("getP2nd", null);
    }
    return p2nd;
  }
  public static LinkedHashMap<String, Object[]> p3rd(Integer year){
    if(p3rd == null) {
      p3rd = new LinkedHashMap<>(seasonStatOf(year));
      p3rd.put("getP3rd", null);
    }
    return p3rd;
  }
  public static LinkedHashMap<String, Object[]> p4th(Integer year){
    if(p4th == null) {
      p4th = new LinkedHashMap<>(seasonStatOf(year));
      p4th.put("getP4th", null);
    }
    return p4th;
  }
  public static LinkedHashMap<String, Object[]> p5th(Integer year){
    if(p5th == null) {
      p5th = new LinkedHashMap<>(seasonStatOf(year));
      p5th.put("getP5th", null);
    }
    return p5th;
  }
  public static LinkedHashMap<String, Object[]> p6th(Integer year){
    if(p6th == null) {
      p6th = new LinkedHashMap<>(seasonStatOf(year));
      p6th.put("getP6th", null);
    }
    return p6th;
  }
  
  // ============================================================================================
  // Info
  // ============================================================================================
  public static LinkedHashMap<String, Object[]> teamOf(Integer year){
    if(teamOf == null) {
      teamOf = new LinkedHashMap<>();
      Object[] teamParams = {"this",year};
      teamOf.put("teamOf", teamParams);
    }
    return teamOf;
  }
  
  // ============================================================================================
  // Dynamic Stats
  // ============================================================================================
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
    return winr;
  }
  public static LinkedHashMap<String, Object[]> per(Integer year){
    if(per == null) {
      per = new LinkedHashMap<>(seasonStatOf(year));
      per.put("getPer", null);
    }
    return per;
  }
  
  // ============================================================================================
  // privates
  // ============================================================================================
  private static LinkedHashMap<String, Object[]> seasonStatOf(Integer year){
    if(seasonStatOf == null) {
      seasonStatOf = new LinkedHashMap<>();
      Object[] ptrParams1 = {year};
      seasonStatOf.put("season", ptrParams1);
      Object[] ptrParams2 = {"this"};
      seasonStatOf.put("seasonStatsOf", ptrParams2);
    }
    return seasonStatOf;
  }
  
  private static LinkedHashMap<String, Object[]> pplayoffStatOf(Integer year){
    if(pplayoffStatOf == null) {
      pplayoffStatOf = new LinkedHashMap<>();
      Object[] ptrParams1 = {year};
      pplayoffStatOf.put("season", ptrParams1);
      Object[] ptrParams2 = {"this"};
      pplayoffStatOf.put("pplayoffStatsOf", ptrParams2);
    }
    return seasonStatOf;
  }
  
  private static LinkedHashMap<String, Object[]> tplayoffStatOf(Integer year){
    if(tplayoffStatOf == null) {
      tplayoffStatOf = new LinkedHashMap<>();
      Object[] ptrParams1 = {year};
      tplayoffStatOf.put("season", ptrParams1);
      Object[] ptrParams2 = {"this"};
      tplayoffStatOf.put("tplayoffStatOf", ptrParams2);
    }
    return seasonStatOf;
  }
  
  
}
