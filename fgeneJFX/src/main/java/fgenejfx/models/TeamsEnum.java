package fgenejfx.models;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum TeamsEnum{

	AUDI,BMW,CHEVROLET,FERRARI,FIAT,FORD,HONDA,HYUNDAY,JAGUAR,KIA,
	LAMBORGHINI,MCLAREN,MERCEDEZ,NISSAN,RENAULT,TOYOTA,VOLKSWAGEN,WILLIAMS;
	
//	public Team get() {
//		return League.get().getTeam(this);
//	}
//	public static Set<Team> all() {
//		return HistoryAgent.get().getAllTeams();
//	}
	public static Set<Team> create() {
		return Arrays.stream(TeamsEnum.values()).map(t->new Team(t)).collect(Collectors.toSet());
	}
}
