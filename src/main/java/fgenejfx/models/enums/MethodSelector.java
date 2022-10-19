package fgenejfx.models.enums;

public enum MethodSelector {

  PTS("Points","Points in the Round","getPts"),
  P1ST("1st","1st places in the round","getP1st"),
  P2ND("2nd","2nd","getP2nd"),
  P3RD("3rd","3rd","getP3rd"),
  P4TH("4th","4th","getP4th"),
  P5TH("5th","5th","getP5th"),
  P6TH("6th","6th","getP6th"),
  PTR("Point Rate","Percentage of points relative to max","getPtRate"),
  WINR("Win Rate","Percentage of 1st places","getWinRate"),
  PER("PER","Player Efficiency Rating - Indicates how many points th epilot should have had","getPer"),
  PPR("PPR","Points per Round","getPPR"),
  PERPR("PerPR","Player Efficiency Rating per Round","getPerPR");
	
  private String name;
  private String tip;
	private String cmd;
	
	private MethodSelector(String name, String tip, String cmd) {
	  this.name = name;
	  this.tip = tip;
		this.cmd = cmd;
	}
	
	public String getName() {
	  return this.name;
	}
	public String getTip() {
	  return this.tip;
	}
	public String getCmd() {
		return this.cmd;
	}
  
}
