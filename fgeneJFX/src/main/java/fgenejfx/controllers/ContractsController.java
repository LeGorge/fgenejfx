package fgenejfx.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import fgenejfx.exceptions.NotValidException;
import fgenejfx.exceptions.PilotInactivationException;
import fgenejfx.models.Contract;
import fgenejfx.models.Pilot;
import fgenejfx.models.Team;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ContractsController implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final Integer CONTRACTS_PER_SEASON = 36;
	public static final Integer PILOTS_PER_TEAM = 2;

	private static ContractsController agent;

	private Set<Contract> contracts = new HashSet<>();

  // ============================================================================================
  // Info
  // ============================================================================================
	public Set<Pilot> pilots() {
		Set<Pilot> all = this.contracts.stream().map(c -> c.getPilot()).collect(Collectors.toSet());
		return all;
	}

	public Set<Team> teams() {
		Set<Team> all = this.contracts.stream().map(c -> c.getTeam()).collect(Collectors.toSet());
		return all;
	}

	public Set<Pilot> upcomingFreeAgents(){
	  Set<Pilot> ps = pilots().stream().filter(p -> remainingYearsOfContract(p) == 1)
	      .collect(Collectors.toSet());
	  ps.removeAll(willRetire());
	  return ps;
	}

	private Contract contract(Pilot p) {
		return contracts.stream().filter(c -> c.getPilot() == p).findFirst().get();
	}

	private Set<Contract> contracts(Team t) {
		return contracts.stream().filter(c -> c.getTeam() == t).collect(Collectors.toSet());
	}

	public Integer remainingYearsOfContract(Pilot p) throws NoSuchElementException {
		return contract(p).getYears();
	}

	public Team teamOf(Pilot p) throws NoSuchElementException {
		return contract(p).getTeam();
	}

	public List<Pilot> pilotsOf(Team t) throws NoSuchElementException {
		return contracts.stream().filter(c -> c.getTeam() == t).sorted().map(Contract::getPilot)
				.collect(Collectors.toList());
	}

	public Set<Pilot> willRetire() throws NoSuchElementException {
		return this.pilots().stream().filter(p -> p.getYearsUntilRetirement() == 1)
				.collect(Collectors.toSet());
	}

	public Set<Pilot> rookies() {
		return contracts.stream().filter(c -> c.getPilot().isRookie()).map(a -> a.getPilot())
				.collect(Collectors.toSet());
	}

	// ============================================================================================
	// Operations
	// ============================================================================================
	public void validateRookies(Set<Pilot> rookies) throws NotValidException {
		// verify if rookies are rookies
//		if(rookies.stream().filter(p -> !p.getRookieYear().equals(League.get().getYear())).findAny().isPresent()){
//			throw new NotValidException();
//		}

		// verify if league has room for the rookies
		Set<Pilot> retired = this.pilots().stream().filter(p -> !p.isActive())
				.collect(Collectors.toSet());
		if (rookies.size() != retired.size() && retired.size() != 0) {
			throw new NotValidException();
		}
	}

	public void updateContracts(Set<Pilot> rookies, Pilot pchamp) throws NotValidException {
	  //register rookies in League
	  League.get().getPilots().addAll(rookies);
	  
		// reduce one year on all contacts
		for (Contract c : contracts) {
			c.passYear();
		}
		// contracts.stream().forEach(Contract::passYear); ATTENTION: can't
		// change contents of iterating stream
		
		//pchampion cannot change teams
		contracts.stream().filter(c-> c.getPilot().equals(pchamp) && c.isDone()).forEach(c-> {
			if(c.getPilot().isActive()) {
				c.setYears(1);	
			}
		});

		// remove terminated contracts
		Set<Contract> ended = contracts.stream().filter(Contract::isDone).collect(Collectors.toSet());
		contracts.removeAll(ended);

		// get Pilots with no contract
		Set<Pilot> noContract = ended.stream().filter(c -> c.getPilot().isActive())
				.map(Contract::getPilot).collect(Collectors.toSet());
		noContract.addAll(rookies);

		// get Teams with room for pilots
		Set<Team> teamsWithRoom = League.get().getTeams().stream()
				.filter(t -> this.pilotsOf(t).size() != PILOTS_PER_TEAM).collect(Collectors.toSet());

		executeFreeAgency(noContract, teamsWithRoom);

		Set<Pilot> retired = this.pilots().stream().filter(p -> !p.isActive())
				.collect(Collectors.toSet());
		cleanPilotFolder(retired);
	}

	private void executeFreeAgency(Set<Pilot> pilots, Set<Team> teams) {
		List<Pilot> pilotsOrdered = pilots.stream().sorted((p2, p1) -> p1.getAi().compareTo(p2.getAi()))
				.collect(Collectors.toList());
		pilotsOrdered.stream().forEachOrdered(p -> {
			List<Team> entries = new ArrayList<>(teams);
			if (League.get().getYear() != 1) {
				try {
					Team last = HistoryController.get().history(League.get().getYear() - 1).teamOf(p);
					if (entries.contains(last)) {
						entries.add(last);
					}
				} catch (NoSuchElementException e) {
				}
			}
			Team sorted = entries.get(new Random().nextInt(entries.size()));
			contracts.add(new Contract(p, sorted, canSignFirst(sorted)));
			if (pilotsOf(sorted).size() == PILOTS_PER_TEAM) {
				teams.remove(sorted);
			}
		});
	}

	private Boolean canSignFirst(Team t) {
		Set<Contract> set = this.contracts(t);
		if (set.isEmpty())
			return true;
		if (set.size() == 2)
			return false;
		return !set.stream().findFirst().get().getIsFirst();
	}

	private void cleanPilotFolder(Set<Pilot> pilots) {
		pilots.stream().forEach(c -> {
			try {
				PersistanceController.inactivatePilotFile(c);
			} catch (PilotInactivationException e) {
				// Alert alert = new Alert(
				// AlertType.ERROR,
				// "the "+c.getName()+".drv pilot file couldn't be moved to
				// history, "
				// + "please do it mannually",
				// ButtonType.OK
				// );
				// alert.showAndWait();
			}
		});
	}

	// ===========================================================================================
	// get singleton
	private ContractsController() {
		ContractsController.agent = this;
	}

	public static ContractsController get() {
		if (agent == null) {
			new ContractsController();
		}
		return agent;
	}

	public static void set(ContractsController ag) {
		if (agent == null) {
			agent = ag;
		}
	}

	public static void reset() {
		new ContractsController();
	}

	// ===========================================================================================
	// getters & setters
	public Set<Contract> getContracts() {
		return this.contracts;
	}

	public void setContracts(Set<Contract> set) {
		this.contracts = set;
	}
}
