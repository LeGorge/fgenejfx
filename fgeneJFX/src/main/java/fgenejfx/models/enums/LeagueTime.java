package fgenejfx.models.enums;

public enum LeagueTime {

  SEASON("seasonStatsOf", "getSeason"),
  PPLAYOFF("pplayoffStatsOf", "getpPlayoff"),
  TPLAYOFF("tplayoffStatsOf","gettPlayoff");
	
	private String cmd;
	private String cmd2;
	
	private LeagueTime(String cmd, String cmd2) {
		this.cmd = cmd;
		this.cmd2 = cmd2;
	}
	
	public String getCmd() {
		return this.cmd;
	}
	public String getCmd2() {
	  return this.cmd2;
	}
  
}
