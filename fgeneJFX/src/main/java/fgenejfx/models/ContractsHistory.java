package fgenejfx.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.util.Pair;

public class ContractsHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	private Set<Pair<Pilot, Team>> relations = new HashSet<>();

	// ===========================================================================================
	// save
	public void save(Set<Contract> contracts) {
		relations = contracts.stream().map(c -> new Pair<>(c.getPilot(), c.getTeam()))
				.collect(Collectors.toSet());
	}

	// ===========================================================================================
	// get relations
	public Team getTeamOf(Pilot p) throws NoSuchElementException {
		return relations.stream().filter(c -> c.getKey() == p).findFirst().get().getValue();
	}

	public List<Pilot> getPilotsOf(Team t) throws NoSuchElementException {
		return relations.stream().filter(c -> c.getValue() == t).collect(Collectors.toList()).stream()
				.map(Pair::getKey).collect(Collectors.toList());
	}
}
