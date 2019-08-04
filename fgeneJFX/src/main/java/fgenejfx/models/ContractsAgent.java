package fgenejfx.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ContractsAgent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static ContractsAgent agent;
	
	private List<Contract> contracts = new ArrayList<>();
	
	private ContractsAgent() {
		ContractsAgent.agent = this;
	}
//	public ContractsAgent(Map<Team, List<Pilot>> map, Map<Pilot, Integer> contractsMap) {
//		map.entrySet().stream().forEach(entry->{
//			biMapPilot1.put(entry.getKey(), entry.getValue().get(0));
//			biMapPilot2.put(entry.getKey(), entry.getValue().get(1));
//		});
//		this.contractsMap = contractsMap;
//	}

	public Team getTeamOf(Pilot p) throws NoSuchElementException {
		return contracts.stream().filter(c->c.getPilot() == p).findFirst().get().getTeam();
	}
	public List<Pilot> getPilotsOf(Team t) throws NoSuchElementException {
		return contracts.stream().filter(c->c.getTeam() == t).collect(Collectors.toList())
				.stream().map(Contract::getPilot).collect(Collectors.toList());
	}
	
	public static ContractsAgent get() {
		if(agent == null) {
			new ContractsAgent();
		}
		return agent;
	}
}
