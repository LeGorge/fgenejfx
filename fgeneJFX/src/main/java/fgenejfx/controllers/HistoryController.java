package fgenejfx.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fgenejfx.jackson.MapDeserializer;
import fgenejfx.models.History;
import fgenejfx.models.Season;

public class HistoryController implements Serializable {
	private static final long serialVersionUID = 1L;
	private static HistoryController historyAgent;

	private List<Season> seasons = new ArrayList<>();

	@JsonDeserialize(using = MapDeserializer.class, keyAs = Season.class, contentAs = History.class)
	private Map<Season, History> historyMap = new LinkedHashMap<>();

	// ===========================================================================================
	// seasons
	public void save(Season s) {
		if(seasons.contains(s)) {
			this.historyMap.get(s).completeHistory(s);
		}else {
			this.seasons.add(s);
			var h = new History();
			h.completeHistory(s);
			this.historyMap.put(s, h);
		}
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
	private HistoryController() {
		HistoryController.historyAgent = this;
	}

	public static HistoryController get() {
		if (historyAgent == null) {
			new HistoryController();
		}
		return historyAgent;
	}

	public static void set(HistoryController ag) {
		if (historyAgent == null) {
			historyAgent = ag;
		}
	}

	public static void reset() {
		new HistoryController();
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
