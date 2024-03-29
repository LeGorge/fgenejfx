package fgenejfx.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fgenejfx.controllers.ContractsController;
import fgenejfx.controllers.GenerallyFilesController;
import fgenejfx.controllers.HistoryController;
import fgenejfx.controllers.League;
import fgenejfx.exceptions.NotValidException;
import fgenejfx.interfaces.StatsMonitorable;
import fgenejfx.models.enums.LeagueTime;
import fgenejfx.models.enums.State;

public class Season implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Season get(Integer year) {
		return League.get().getYear().equals(year) ? League.get().getSeason()
				: HistoryController.get().getSeasons().stream().filter(s -> s.getYear().equals(year)).findFirst().get();
	}

	private Integer year;
	private State state = State.INDRAFT;

	private Group[] season = new Group[6];

	private Group pPlayoff;
	private Group tPlayoff;

	// ============================================================================================
	// Info methods
	// ============================================================================================
	public Integer groupIndexOf(Pilot p) throws NoSuchElementException {
		return Arrays.asList(this.season).indexOf(seasonGroupOf(p)) + 1;
	}

	public Integer groupIndexOf(Team t) throws NoSuchElementException {
		return Arrays.asList(this.season).indexOf(seasonGroupOf(t)) + 1;
	}

	public Group playoffGroup(LeagueTime time) {
		switch (time) {
			case PPLAYOFF:
				return this.pPlayoff;
			case TPLAYOFF:
				return this.tPlayoff;
		}
		return null;
	}

	// ============================================================================================
	// pilot related methods
	// ============================================================================================
	public Set<Pilot> pilots() {
		return Arrays.stream(season).flatMap(g -> g.pilots().stream()).collect(Collectors.toSet());
	}

	public List<Pilot> pilots(LeagueTime time) {
		switch (time) {
		case SEASON:
			return Arrays.stream(season).flatMap(g -> g.pilots().stream()).collect(Collectors.toList());
		case PPLAYOFF:
			return (pPlayoff != null) ? pPlayoff.pilots() : new ArrayList<>();
		case TPLAYOFF:
			return (tPlayoff != null) ? tPlayoff.pilots() : new ArrayList<>();
		}
		return new ArrayList<>();
	}

	public RaceStats statsOf(Pilot p, LeagueTime time) {
		switch (time) {
		case SEASON:
			return Arrays.stream(season).filter(g -> g.statsOf(p) != null).findFirst().get().statsOf(p);
		case PPLAYOFF:
			return pPlayoff.statsOf(p);
		case TPLAYOFF:
			return tPlayoff.statsOf(p);
		}
		return null;
	}

	public RaceStats tplayoffStatsOf(Pilot p) {
		return tPlayoff.statsOf(p);
	}

	public Group seasonGroupOf(Pilot p) throws NoSuchElementException {
		return Arrays.stream(season).filter(g -> g.contains(p)).findFirst().get();
	}

	public Integer posOf(Pilot p) {
		if (this.pPlayoff.contains(p)) {
			return this.pPlayoff.posOf(p);
		} else {
			return this.seasonGroupOf(p).posOf(p) + 5;
		}
	}

	public String topPosOf(Pilot p) {
		Integer pos = posOf(p);
		if (pos > 6) {
			pos = (pos - 6) * 6 + 6;
		}
		return "Top " + pos;
	}

	// ============================================================================================
	// team related methods
	// ============================================================================================
	public Set<Team> teams() {
		return League.get().getTeams();
	}

	public List<Team> teams(LeagueTime time) {
		switch (time) {
		case SEASON:
			return new ArrayList<>(teams());
		case PPLAYOFF:
			return (pPlayoff != null) ? pPlayoff.teams(this.year) : new ArrayList<>();
		case TPLAYOFF:
			return (tPlayoff != null) ? tPlayoff.teams(this.year) : new ArrayList<>();
		}
		return new ArrayList<>();
	}

	public RaceStats statsOf(Team t, LeagueTime time) {
		switch (time) {
		case SEASON:
			return Arrays.stream(season).filter(g -> g.contains(t, this.year)).findFirst().get().statsOf(t, this.year);
		case PPLAYOFF:
			return pPlayoff.statsOf(t, this.year);
		case TPLAYOFF:
			return tPlayoff.statsOf(t, this.year);
		}
		return null;
	}

	public StatsMonitorable champ(LeagueTime time, Class c) {
		if (state == State.ENDED) {
			switch (time) {
			case PPLAYOFF:
				return c.equals(Team.class) ? pPlayoff.firstTeam(this.year) : pPlayoff.firstPilot();
			case TPLAYOFF:
				return c.equals(Team.class) ? tPlayoff.firstTeam(this.year) : tPlayoff.firstPilot();
			}
		}
		return null;
	}

	public Group seasonGroupOf(Team t) throws NoSuchElementException {
		return Arrays.stream(season).filter(g -> g.contains(t, this.year)).findFirst().get();
	}

	// ============================================================================================
	// state machine
	// ============================================================================================
	public void sync(Integer index) throws NotValidException {
		if (state == State.INSEASON) {
			this.syncGroupStats(index);
			if (!Arrays.stream(this.season).anyMatch(g -> g.isEmpty())) {
				this.startPlayoffs();
			}
		}
	}

	public void sync(LeagueTime time) throws NotValidException {
		if (state == State.INPLAYOFF) {
			switch (time) {
			case PPLAYOFF:
				syncPplayoffStats();
				if (!readySync(LeagueTime.TPLAYOFF, null)) {
					this.state = State.ENDED;
				}
				break;
			case TPLAYOFF:
				syncTplayoffStats();
				if (!readySync(LeagueTime.PPLAYOFF, null)) {
					this.state = State.ENDED;
				}
				break;
			default:
				break;
			}
		}
	}

	public boolean readySync(LeagueTime time, Integer index) {
		switch (time) {
		case SEASON:
			return this.season[index].isEmpty();
		case TPLAYOFF:
			return state == State.INPLAYOFF && this.tPlayoff.isEmpty();
		case PPLAYOFF:
			return state == State.INPLAYOFF && this.pPlayoff.isEmpty();
		}
		return false;
	}

	// ============================================================================================
	// mid-season operations
	// ============================================================================================
	private RaceStats total(Pilot p) {
		RaceStats result = this.statsOf(p, LeagueTime.SEASON);
		RaceStats tplayoff = this.tPlayoff.statsOf(p);
		if (tplayoff != null) {
			result = RaceStats.sum(result, tplayoff);
		}

		RaceStats pplayoff = this.pPlayoff.statsOf(p);
		if (pplayoff != null) {
			result = RaceStats.sum(result, pplayoff);
		}

		return RaceStats.sum(result, p.getStats().getStatsTotals());
	}

	private void syncGroupStats(Integer index) {
		Group g = this.season[index];
		g.pilots().stream().forEach(p -> {
			RaceStats read = GenerallyFilesController.readDriver(p.getName());
			g.updateStat(p, RaceStats.subtract(read, p.getStats().getStatsTotals()));
		});
		g.updatePer();
	}

	private void syncTplayoffStats() {
		this.tPlayoff.pilots().stream().forEach(p -> {
			RaceStats read = GenerallyFilesController.readDriver(p.getName());
			this.tPlayoff.updateStat(p, RaceStats.subtract(read, this.total(p)));
		});
		this.tPlayoff.updatePer();
	}

	private void syncPplayoffStats() {
		this.pPlayoff.pilots().stream().forEach(p -> {
			RaceStats read = GenerallyFilesController.readDriver(p.getName());
			this.pPlayoff.updateStat(p, RaceStats.subtract(read, this.total(p)));
		});
		this.pPlayoff.updatePer();
	}

	public void startPlayoffs() throws NotValidException {
		// pplayoff
		this.pPlayoff = new Group();
		for (Group g : season) {
			this.pPlayoff.addPilot(g.firstPilot());
		}

		// tplayoff
		Set<Pilot> ps = this.teams().stream()
				.sorted((t2, t1) -> this.statsOf(t1, LeagueTime.SEASON).compareTo(this.statsOf(t2, LeagueTime.SEASON)))
				.limit(3).flatMap(t -> League.get().pilotsOf(t, year).stream()).collect(Collectors.toSet());
		this.tPlayoff = new Group(ps);
		this.state = State.INPLAYOFF;
	}

	// ============================================================================================
	// season creation
	// ============================================================================================
	public Season() {
		this.year = League.get().getYear();
	}
	
	public void startSeason() {
		List<Team> teams = new ArrayList<>(League.get().getTeams());
		Collections.shuffle(teams);
		buildGroups(teams);
		setState(State.INSEASON);
	}

	private void buildGroups(List<Team> teams) {
		for (int i = 0; i < season.length; i++) {
			season[i] = new Group(
					teams.subList(i * 3, i * 3 + 3).stream().map(t -> ContractsController.get().pilotsOf(t))
							.flatMap(l -> l.stream()).collect(Collectors.toSet()));
		}
	}

	// ============================================================================================
	// simulation
	// ============================================================================================
	public void simulate(LeagueTime time, Integer groupIndex) {
		switch (time) {
		case SEASON:
			Group g = this.season[groupIndex];
			g.simulate();
			if (groupIndex == 5) {
				startPlayoffs();
			}
			break;
		case TPLAYOFF:
			this.tPlayoff.simulate();
			break;
		case PPLAYOFF:
			this.pPlayoff.simulate();
			this.state = State.ENDED;
			break;
		}
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

	public State getState() {
		return state;
	}

	@JsonIgnore
	public Boolean isEnded() {
		return state == State.ENDED;
	}
	@JsonIgnore
	public Boolean isInSeason() {
		return state == State.INSEASON;
	}
	@JsonIgnore
	public Boolean isInDraft() {
		return state == State.INDRAFT;
	}
	@JsonIgnore
	public Boolean isInPlayoffs() {
		return state == State.INPLAYOFF;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Group[] getSeason() {
		return season;
	}

	public void setSeason(Group[] season) {
		this.season = season;
	}

	public Group getpPlayoff() {
		return pPlayoff;
	}

	public void setpPlayoff(Group pPlayoff) {
		this.pPlayoff = pPlayoff;
	}

	public Group gettPlayoff() {
		return tPlayoff;
	}

	public void settPlayoff(Group tPlayoff) {
		this.tPlayoff = tPlayoff;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Season other = (Season) obj;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.year.toString();
	}
}
