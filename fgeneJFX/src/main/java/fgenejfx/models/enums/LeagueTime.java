package fgenejfx.models.enums;

public enum LeagueTime {

  SEASON("statsOf", "getSeason"),
  PPLAYOFF("statsOf", "getpPlayoff"),
  TPLAYOFF("statsOf","gettPlayoff");

  public static String param(LeagueTime time){
	  switch (time) {
		  case PPLAYOFF:
			  return "p";
		  case TPLAYOFF:
			  return "t";
	  }
	  return null;
  }
	
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
