package fgenejfx.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fgenejfx.jackson.DraftDeserializer;

@JsonDeserialize(using = DraftDeserializer.class)
public class Draft implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
	@JsonIdentityReference(alwaysAsId = true)
	private Pilot pilot;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
	@JsonIdentityReference(alwaysAsId = true)
	private Team team;

	private Integer years;

	public Draft() {
	}

	public Draft(Pilot pilot, Team team, Integer years) {
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
