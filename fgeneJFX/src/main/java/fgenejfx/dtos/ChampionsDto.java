package fgenejfx.dtos;

import fgenejfx.controllers.HistoryController;
import fgenejfx.models.enums.LeagueTime;
import lombok.Data;

@Data
public class ChampionsDto {

	private Integer year;
	
	private CardDto c1;
	private CardDto c2;
	private CardDto c3;
	private CardDto c4;
	private CardDto c5;
	private CardDto c6;
	
	public ChampionsDto(Integer year, LeagueTime time) {
		this.year = year;
		if(time == LeagueTime.PPLAYOFF) {
			var cards = HistoryController.get().season(year).getpPlayoff().pilots()
					.stream().map(p -> new CardDto(year, p)).toArray(CardDto[]::new);
			c1 = cards[0];
			c2 = cards[1];
			c3 = cards[2];
			c4 = cards[3];
			c5 = cards[4];
			c6 = cards[5];
		}
		if(time == LeagueTime.TPLAYOFF) {
			var cards = HistoryController.get().season(year).gettPlayoff().teams(year)
					.stream().map(t -> new CardDto(year, t)).toArray(CardDto[]::new);
			c1 = cards[0];
			c2 = cards[1];
			c3 = cards[2];
		}
		
	}
	
}
