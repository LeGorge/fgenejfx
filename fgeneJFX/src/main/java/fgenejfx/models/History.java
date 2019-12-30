package fgenejfx.models;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fgenejfx.exceptions.CopyException;
import fgenejfx.jackson.MapDeserializer;
import fgenejfx.utils.Utils;

public class History implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Map<String, String> relations = new HashMap<>();

	@JsonDeserialize(using = MapDeserializer.class, keyAs = Pilot.class, contentAs = Integer.class)
	private Map<Pilot, Integer> ais = new HashMap<>();
	
	@JsonDeserialize(using = MapDeserializer.class, keyAs = Team.class, contentAs = EnumMap.class)
	private Map<Team, EnumMap<Powers, Double>> powers = new HashMap<>();

	public History(){
	}
	public History(Season s){
		ContractsAgent.get().pilots().forEach(p ->{
			this.saveAi(p, p.getAi());
		});
		ContractsAgent.get().teams().forEach(t ->{
			try {
				this.savePowers(t, t.getPowers());
			} catch (CopyException e) {
				e.printStackTrace();
			}
		});
		this.save(ContractsAgent.get().getContracts());
	}
	
	//=========================================================================================== relations
	public void save(Set<Contract> contracts) {
		contracts.stream().forEach(c->
			this.relations.put(c.getPilot().getName(), c.getTeam().getName().toString())
		);
	}
	public Team teamOf(Pilot p) throws NoSuchElementException {
		return Team.get(relations.get(p.getName()));
	}
	public List<Pilot> pilotsOf(Team t) throws NoSuchElementException {
		return relations.keySet().stream()
		.filter(p->relations.get(p) == t.getName().toString())
		.map(p -> Pilot.get(p))
		.sorted()
		.collect(Collectors.toList());
	}
	//=========================================================================================== ai
	public void saveAi(Pilot p, Integer ai) {
		ais.put(p, ai);
	}
	public Integer ai(Pilot p) {
		return ais.get(p);
	}
	//=========================================================================================== powers
	public void savePowers(Team t, EnumMap<Powers, Double> powers) throws CopyException {
		this.powers.put(t, Utils.copy(powers));
	}
	public EnumMap<Powers, Double> powers(Team t) {
		return powers.get(t);
	}
	//=========================================================================================== getter & setters
	public Map<String,String> getRelations() {
		return this.relations;
	}

	public Map<Pilot,Integer> getAis() {
		return this.ais;
	}

	public Map<Team,EnumMap<Powers,Double>> getPowers() {
		return this.powers;
	}
}
