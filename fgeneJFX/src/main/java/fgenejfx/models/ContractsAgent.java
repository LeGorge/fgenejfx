package fgenejfx.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import fgenejfx.controllers.League;
import fgenejfx.controllers.PersistanceController;
import fgenejfx.exceptions.PilotInactivationException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ContractsAgent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final Integer CONTRACTS_PER_SEASON = 36;
	public static final Integer PILOTS_PER_TEAM = 2;
	
	private static ContractsAgent agent;
	
	private Set<Contract> contracts = new HashSet<>();
	
//	public ContractsAgent(Map<Team, List<Pilot>> map, Map<Pilot, Integer> contractsMap) {
//		map.entrySet().stream().forEach(entry->{
//			biMapPilot1.put(entry.getKey(), entry.getValue().get(0));
//			biMapPilot2.put(entry.getKey(), entry.getValue().get(1));
//		});
//		this.contractsMap = contractsMap;
//	}

	//=========================================================================================== get all
	public Set<Pilot> pilots(){
		Set<Pilot> all = this.contracts.stream().map(c -> c.getPilot()).collect(Collectors.toSet());
		return all;
	}
	
	public Set<Team> teams(){
		Set<Team> all = this.contracts.stream().map(c -> c.getTeam()).collect(Collectors.toSet());
		return all;
	}
	
	//=========================================================================================== get relations
	private Contract contract(Pilot p) {
		return contracts.stream().filter(c->c.getPilot() == p).findFirst().get();
	}
	private Set<Contract> contracts(Team t) {
		return contracts.stream().filter(c->c.getTeam() == t).collect(Collectors.toSet());
	}
	public Integer remainingYearsOfContract(Pilot p) throws NoSuchElementException {
		return contract(p).getYears();
	}
	public Team teamOf(Pilot p) throws NoSuchElementException {
		return contract(p).getTeam();
	}
	public List<Pilot> pilotsOf(Team t) throws NoSuchElementException {
		return contracts.stream()
			.filter(c->c.getTeam() == t)
			.sorted()
			.map(Contract::getPilot)
			.collect(Collectors.toList());
	}
	public Set<Pilot> rookies(){
		return contracts.stream().filter(c->c.getPilot().isRookie()).map(a->a.getPilot()).collect(Collectors.toSet());
	}
	//=========================================================================================== operations
//	public void updateContracts(){
//		updateContracts(CONTRACTS_PER_SEASON);
//	}
	public void updateContracts(Set<Pilot> rookies){
		//save to history
		// if(League.get().getYear() != 1){
		// 	HistoryAgent.get()
		// 		.history(League.get().getYear()-1)
		// 		.save(contracts);
		// }
		
		//reduce one year on all contacts
		for (Contract c : contracts) {
			c.passYear();
		}
		//contracts.stream().forEach(Contract::passYear); ATTENTION: can't change contents of iterating stream
		
		//remove terminated contracts
		Set<Contract> ended = contracts.stream().filter(c->c.isDone()).collect(Collectors.toSet());
		contracts.removeAll(ended);
		
		//get Pilots with no contract
		Set<Pilot> noContract = ended.stream()
			.filter(c->c.getPilot().isActive())
			.map(Contract::getPilot)
			.collect(Collectors.toSet());
		
		//create necessary rookies CHANGED: NOT A RESPONSABILITY OF THIS METHOD
//		int nNewPilots = (max - contracts.size()) - noContract.size();
//		if(nNewPilots < 0) {
//			nNewPilots = 0;
//		}
		noContract.addAll(rookies);
		
		//get Teams with room for pilots
		Set<Team> teamsWithRoom = League.get().getTeams().stream()
				.filter(t -> ContractsAgent.get().pilotsOf(t).size() != PILOTS_PER_TEAM)
				.collect(Collectors.toSet());
		
		executeFreeAgency(noContract, teamsWithRoom);
		
		//get retiring Pilots
		Set<Pilot> retiring = ended.stream()
				.filter(c->!c.getPilot().isActive())
				.map(Contract::getPilot)
				.collect(Collectors.toSet());
//		cleanPilotFolder(retiring);
	}
	
	private void executeFreeAgency(Set<Pilot> pilots, Set<Team> teams) {
		List<Pilot> pilotsOrdered = pilots.stream()
			.sorted((p2, p1) -> p1.getAI().compareTo(p2.getAI()))
			.collect(Collectors.toList());
		pilotsOrdered.stream().forEachOrdered(p->{
			List<Team> entries = new ArrayList<>(teams);
			if(League.get().getYear() != 1){
				Team last = HistoryAgent.get()
						.history(League.get().getYear()-1)
						.teamOf(p);
				if(entries.contains(last)) {
					entries.add(last);
				}
			}
			Team sorted = entries.get(new Random().nextInt(entries.size()));
			contracts.add(new Contract(p,sorted,canSignFirst(sorted)));
			if(pilotsOf(sorted).size() == PILOTS_PER_TEAM) {
				teams.remove(sorted);
			}
		});
	}
	
	private Boolean canSignFirst(Team t) {
		Set<Contract> set = this.contracts(t);
		if(set.isEmpty())return true;
		if(set.size() == 2)return false;
		return !set.stream().findFirst().get().getIsFirst();
	}
	
	private void cleanPilotFolder(Set<Pilot> pilots) {
		pilots.stream().forEach(c->{
			try {
				PersistanceController.inactivatePilotFile(c);
			} catch (PilotInactivationException e) {
				Alert alert = new Alert(
					AlertType.ERROR, 
					"the "+c.getName()+".drv pilot file couldn't be moved to history, "
							+ "please do it mannually",
					ButtonType.OK
				);
				alert.showAndWait();
			}
		});
	}
	
	//=========================================================================================== get singleton
	private ContractsAgent() {
		ContractsAgent.agent = this;
	}
	public static ContractsAgent get() {
		if(agent == null) {
			new ContractsAgent();
		}
		return agent;
	}
	public static void set(ContractsAgent ag) {
		if(agent == null) {
			agent = ag;
		}
	}
	//=========================================================================================== getters & setters
	public Set<Contract> getContracts() {
		return this.contracts;
	}
}
