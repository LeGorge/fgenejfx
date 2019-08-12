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

public class ContractsAgent implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Integer MAXCONTRACT = 8;
	private static ContractsAgent agent;
	
	private Set<Contract> contracts = new HashSet<>();
	
//	public ContractsAgent(Map<Team, List<Pilot>> map, Map<Pilot, Integer> contractsMap) {
//		map.entrySet().stream().forEach(entry->{
//			biMapPilot1.put(entry.getKey(), entry.getValue().get(0));
//			biMapPilot2.put(entry.getKey(), entry.getValue().get(1));
//		});
//		this.contractsMap = contractsMap;
//	}

	//=========================================================================================== get relations
	private Contract getContract(Pilot p) {
		return contracts.stream().filter(c->c.getPilot() == p).findFirst().get();
	}
	public Integer getRemainingYearsOfContract(Pilot p) throws NoSuchElementException {
		return getContract(p).getYears();
	}
	public Team getTeamOf(Pilot p) throws NoSuchElementException {
		return getContract(p).getTeam();
	}
	public List<Pilot> getPilotsOf(Team t) throws NoSuchElementException {
		return contracts.stream().filter(c->c.getTeam() == t).collect(Collectors.toList())
				.stream().map(Contract::getPilot).collect(Collectors.toList());
	}
	//=========================================================================================== operations
	public Set<Pilot> getRookies(){
		return contracts.stream().filter(c->c.getPilot().isRookie()).map(a->a.getPilot()).collect(Collectors.toSet());
	}
	
	public void updateContracts(Set<Pilot> rookies){
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
		noContract.addAll(rookies);
		
		//get Teams with room for pilots
		Set<Team> teamsWithRoom = ended.stream()
				.filter(c->c.getPilot().isActive())
				.map(Contract::getTeam)
				.distinct()
				.collect(Collectors.toSet());
		
		executeFreeAgency(noContract, teamsWithRoom);
		HistoryAgent.get().save(contracts);
	}
	
	private void executeFreeAgency(Set<Pilot> pilots, Set<Team> teams) {
		List<Pilot> pilotsOrdered = pilots.stream()
			.sorted((p2, p1) -> p1.getAI().compareTo(p2.getAI()))
			.collect(Collectors.toList());
		pilotsOrdered.stream().forEachOrdered(p->{
			List<Team> entries = new ArrayList<>(teams);
			try {
				Team last = HistoryAgent.get().getTeamOf(League.get().getYear()-1, p);
				if(entries.contains(last)) {
					entries.add(last);
				}
			} catch (NoSuchElementException e) {
			}
			Team sorted = entries.get(new Random().nextInt(entries.size()));
			contracts.add(new Contract(p,sorted,new Random().nextInt(MAXCONTRACT)+1));
			if(getPilotsOf(sorted).size() == 2) {
				teams.remove(sorted);
			}
		});
	}
	
	private void endContracts(Set<Contract> end) {
//		end.stream()
//				.filter(c->!c.getPilot().isActive())
//				.forEach(c->{
//					try {
//						PersistanceController.inactivatePilotFile(c.getPilot());
//					} catch (PilotInactivationException e) {
//						Alert alert = new Alert(
//								AlertType.ERROR, 
//								"the "+c.getPilot().getName()+".drv pilot file couldn't be moved to history, "
//										+ "please do it mannually",
//								ButtonType.OK
//							);
//						alert.showAndWait();
//					}
//				});
		
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
}
