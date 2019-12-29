package fgenejfx.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fgenejfx.exceptions.NameGeneratorException;
import fgenejfx.models.Pilot;
import fgenejfx.models.Season;
import fgenejfx.models.Team;
import fgenejfx.utils.InternetDependantUtils;
import javafx.scene.control.TextInputDialog;

public class League implements Serializable{
	private static final long serialVersionUID = 1L;
	private static League league;

	private Set<Pilot> pilots = new HashSet<>();
	private Set<Team> teams = new HashSet<>();

	private Season season;
	private Integer year = 1;

	public void passYear() {
		this.year++;
	}

	//=========================================================================================== gets
	
	public Set<Pilot> activePilots(){
		return this.pilots.stream().filter(p->p.isActive()).collect(Collectors.toSet());
	}
	
//	//=========================================================================================== season
//	public void newSeason() {
//		Season s = new Season();
//		HistoryAgent.get().save(s);
//	}
	
	//=========================================================================================== new pilots
	public Set<Pilot> createNewPilots(int howMany) {
		String nomes[] = new String[howMany*2];
		try {
			nomes = InternetDependantUtils.getRandomNames(howMany*2);
			nomes = Arrays.stream(nomes).filter(n->isNameAvailable(n)).limit(howMany).toArray(String[]::new);
			if(nomes.length < howMany) {
				throw new NameGeneratorException();
			}
		} catch (NameGeneratorException e) {
			nomes = new String[howMany];
			int i = 0;
			
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("PILOT CREATION");
			dialog.setHeaderText("The Name Generator wasn't able to get a proper name, please try one yourself:");
			 
			while(i<howMany) {
				dialog.setContentText("Name for Pilot "+(i+1)+":");
				Optional<String> result = dialog.showAndWait();
				if(result.isPresent()) {
					if(isNameAvailable(result.get())) {
						nomes[0] = result.get();
						i++;
					}
				}
			}
		}
		return Arrays.stream(nomes).map(n->new Pilot(n)).collect(Collectors.toSet());
	}
	
	private boolean isNameAvailable(String name) {
		try {
			Pilot.get(name);
			return false;
		} catch (NoSuchElementException e) {
			return true;
		}
	}
	//=========================================================================================== get singleton
	private League() {
		League.league = this;
	}
	public static League get() {
		if(league == null) {
			new League();
		}
		return league;
	}
	public static void set(League l) {
		if(league == null) {
			league = l;
		}
	}
	//=========================================================================================== getters and setters
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Set<Pilot> getPilots(){
		return this.pilots;
	}
	public Set<Team> getTeams(){
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
