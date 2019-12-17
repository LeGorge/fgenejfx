package fgenejfx.models;

import java.io.Serializable;

public class Contract implements Serializable{
	private static final long serialVersionUID = 1L;

	private Pilot pilot;
	private Team team;
	private Integer years;
	
	public void passYear() {
		years--;
	}
	public boolean isDone() {
		return years == 0; 
	}
	
	public Contract(Pilot pilot, Team team, Integer years) {
		super();
		this.pilot = pilot;
		this.team = team;
		this.years = years;
	}
	public Pilot getPilot() {
		return pilot;
	}
	public void setPilot(Pilot pilot) {
		this.pilot = pilot;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public Integer getYears() {
		return years;
	}
	public void setYears(Integer years) {
		this.years = years;
	}
	@Override
	public String toString() {
		return pilot.getName() + " - " + team.getName() + " : " + years;
	}
}
