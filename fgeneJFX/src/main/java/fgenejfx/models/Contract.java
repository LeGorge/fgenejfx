package fgenejfx.models;

import java.io.Serializable;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fgenejfx.jackson.ContractDeserializer;

@JsonDeserialize(using = ContractDeserializer.class)
public class Contract implements Serializable, Comparable<Contract> {
	private static final long serialVersionUID = 1L;
	public static final Integer MAX_YEARS_ON_CONTRACT = 8;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
	@JsonIdentityReference(alwaysAsId = true)
	private Pilot pilot;

	// @JsonIgnoreProperties({"lifeStats", "stats","powers"})
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
	@JsonIdentityReference(alwaysAsId = true)
	private Team team;

	private Boolean isFirst;
	private Integer years;

	public void passYear() {
		years--;
	}

	@JsonIgnore
	public boolean isDone() {
		return years == 0;
	}

	public Contract() {
	}

	public Contract(Pilot pilot, Team team, Boolean isFirst) {
		super();
		this.pilot = pilot;
		this.team = team;
		this.isFirst = isFirst;
		int maxContract = pilot.getYearsUntilRetirement() > MAX_YEARS_ON_CONTRACT
				? MAX_YEARS_ON_CONTRACT
				: pilot.getYearsUntilRetirement();
		this.years = new Random().nextInt(maxContract) + 1;
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

	public Boolean getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(Boolean isFirst) {
		this.isFirst = isFirst;
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

	@Override
	public int compareTo(Contract c) {
		return this.getIsFirst().compareTo(c.getIsFirst()) * -1;
	}
}
