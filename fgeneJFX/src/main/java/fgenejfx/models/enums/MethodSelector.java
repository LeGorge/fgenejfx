package fgenejfx.models.enums;

public enum MethodSelector {

  PTS("Points","getPts"),
  P1ST("1st","getP1st"),
  P2ND("2nd","getP2nd"),
  P3RD("3rd","getP3rd"),
  P4TH("4th","getP4th"),
  P5TH("5th","getP5th"),
  P6TH("6th","getP6th"),
  PTR("Point Rate","getPtRate"),
  WINR("Win Rate","getWinRate"),
  PER("PER","getPer");
	
  private String name;
	private String cmd;
	
	private MethodSelector(String name, String cmd) {
	  this.name = name;
		this.cmd = cmd;
	}
	
	public String getName() {
	  return this.name;
	}
	public String getCmd() {
		return this.cmd;
	}
  
}
