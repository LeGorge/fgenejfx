package fgenejfx.controllers;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fgenejfx.exceptions.NameGeneratorException;
import fgenejfx.exceptions.NotValidException;
import fgenejfx.models.ContractsAgent;
import fgenejfx.models.HistoryAgent;
import fgenejfx.models.Pilot;
import fgenejfx.models.Season;
import fgenejfx.models.Team;
import fgenejfx.models.enums.TeamsEnum;
import fgenejfx.utils.InternetDependantUtils;
import javafx.scene.control.TextInputDialog;

public class League implements Serializable {
	private static final long serialVersionUID = 1L;
	private static League league;

	private Set<Pilot> pilots = new HashSet<>();
	private Set<Team> teams = new HashSet<>();

	private Season season;
	private Integer year = 1;

	public void passYear() {
		this.year++;
	}

	// ============================================================================================
	// gets
	// ============================================================================================
	public Team teamOf(Pilot p, Integer year) throws NoSuchElementException {
		if (year == this.year) {
			return ContractsAgent.get().teamOf(p);
		} else {
			return HistoryAgent.get().history(year).teamOf(p);
		}
	}

	public List<Pilot> pilotsOf(Team t, Integer year) throws NoSuchElementException {
		if (year == this.year) {
			return ContractsAgent.get().pilotsOf(t);
		} else {
			return HistoryAgent.get().history(year).pilotsOf(t);
		}
	}
	public Season season(int year){
		if(year == this.year){
			return this.season;
		}else{
			return HistoryAgent.get().season(year);
		}
	}

	// ============================================================================================
	// season
	// ============================================================================================
	public void changeSeason() throws NotValidException {
		new SeasonChangeController();
	}

	// ============================================================================================
	// new pilots
	// ============================================================================================
	public Set<Pilot> createNewPilots(int howMany) {
		if (howMany == 0)
			return new HashSet<>();
		String nomes[] = new String[howMany * 2];
		try {
			nomes = InternetDependantUtils.getRandomNames(howMany * 2);
			nomes = Arrays.stream(nomes).filter(n -> isNameAvailable(n)).limit(howMany)
					.toArray(String[]::new);
			if (nomes.length < howMany) {
				throw new NameGeneratorException();
			}
		} catch (NameGeneratorException e) {
			nomes = new String[howMany];
			int i = 0;

			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("PILOT CREATION");
			dialog.setHeaderText(
					"The Name Generator wasn't able to get a proper name, " + "please try one yourself:");

			while (i < howMany) {
				dialog.setContentText("Name for Pilot " + (i + 1) + ":");
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()) {
					if (isNameAvailable(result.get())) {
						nomes[0] = result.get();
						i++;
					}
				}
			}
		}
		return Arrays.stream(nomes).map(n -> new Pilot(n)).collect(Collectors.toSet());
	}

	private boolean isNameAvailable(String name) {
		try {
			Pilot.get(name);
			return false;
		} catch (NoSuchElementException e) {
			return true;
		}
	}

	// ============================================================================================
	// get singleton
	// ============================================================================================
	private League() {
		this.setTeams(TeamsEnum.create());
		League.league = this;
	}

	public static League get() {
		if (league == null) {
			new League();
		}
		return league;
	}

	public static void set(League l) {
		league = l;
	}

	public static void reset() {
		new League();
	}

	// ============================================================================================
	// getters and setters
	// ============================================================================================
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Set<Pilot> getPilots() {
		return this.pilots;
	}

	public Set<Team> getTeams() {
		return this.teams;
	}

	public void setPilots(Set<Pilot> pilots) {
		this.pilots = pilots;
	}

	public void setTeams(Set<Team> teams) {
		this.teams = teams;
	}

	@JsonIgnore
	public Season getSeason() {
		return this.season;
	}

	public void setSeason(Season s) {
		this.season = s;
		this.year = s.getYear();
	}

}
