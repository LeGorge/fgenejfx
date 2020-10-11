package fgenejfx.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fgenejfx.controllers.League;
import fgenejfx.jackson.MapDeserializer;

public class HistoryAgent implements Serializable {
	private static final long serialVersionUID = 1L;
	private static HistoryAgent historyAgent;

	private List<Season> seasons = new ArrayList<>();

	@JsonDeserialize(using = MapDeserializer.class, keyAs = Season.class, contentAs = History.class)
	private Map<Season, History> historyMap = new LinkedHashMap<>();

	// ===========================================================================================
	// seasons
	public void save(Season s) {
		this.seasons.add(s);
		this.historyMap.put(s, new History(s));
	}

	public Season season(int year) throws NoSuchElementException {
		return seasons.stream().filter(s -> s.getYear() == year).findFirst().get();
	}

	// ===========================================================================================
	// history
	public History history(Season s) {
		return historyMap.get(s);
	}

	public History history(Integer year) throws NoSuchElementException {
		Season se = historyMap.keySet().stream().filter(s -> s.getYear().equals(year)).findFirst()
				.get();
		return this.history(se);
	}

	// ===========================================================================================
	// get singletons
	private HistoryAgent() {
		HistoryAgent.historyAgent = this;
	}

	public static HistoryAgent get() {
		if (historyAgent == null) {
			new HistoryAgent();
		}
		return historyAgent;
	}

	public static void set(HistoryAgent ag) {
		if (historyAgent == null) {
			historyAgent = ag;
		}
	}

	public static void reset() {
		new HistoryAgent();
	}

	// ===========================================================================================
	// getters & setters
	public List<Season> getSeasons() {
		return this.seasons;
	}

	public Map<Season, History> getHistoryMap() {
		return this.historyMap;
	}
}
