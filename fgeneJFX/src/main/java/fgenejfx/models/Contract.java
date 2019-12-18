package fgenejfx.models;

import java.io.Serializable;
import java.util.Random;

import fgenejfx.controllers.League;

public class Contract implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final Integer MAX_YEARS_ON_CONTRACT = 8;

	private Pilot pilot;
	private Team team;
	private Integer years;
	
	public void passYear() {
		years--;
	}
	public boolean isDone() {
		return years == 0; 
	}
	
	public Contract(Pilot pilot, Team team) {
		super();
		this.pilot = pilot;
		this.team = team;
		int maxContract = pilot.getYearsUntilRetirement() > MAX_YEARS_ON_CONTRACT ?
				MAX_YEARS_ON_CONTRACT : pilot.getYearsUntilRetirement();
		this.years = new Random().nextInt(maxContract)+1;
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
