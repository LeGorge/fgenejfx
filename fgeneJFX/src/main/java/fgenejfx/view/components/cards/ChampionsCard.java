package fgenejfx.view.components.cards;

import fgenejfx.dtos.CardDto;
import fgenejfx.models.enums.LeagueTime;
import fgenejfx.view.components.CustomHyperlink;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class ChampionsCard extends GridPane {

	public ChampionsCard(CardDto dto, LeagueTime time) {
		super();
		this.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(10, 10, 10, 10));
		this.setStyle("-fx-padding: 10; -fx-border-style: none;"); 
//				"-fx-border-width: 2;" + 
//				"-fx-border-insets: 5;" + 
//				"-fx-border-radius: 15;" + 
//				"-fx-border-color: black;");
		this.setGridLinesVisible(false);
		
		Text team = new Text("Team: ");
		Text pilot1 = new Text("Pilot 1: ");
		Text pilot2 = new Text("Pilot 2: ");
		Text pts = new Text("Points: ");
		Text p1st = new Text("1st: ");
		
		if(time == LeagueTime.PPLAYOFF) {
			add(pilot1, 0, 0);
			add(new CustomHyperlink(dto.getP1().getName(), "name"), 1, 0);
			add(team, 0, 1);
			add(new CustomHyperlink(dto.getT().getName(), "name"), 1, 1);
			add(pts, 0, 2);
			add(new Text(dto.getPts().toString()), 1, 2);
			add(p1st, 0, 3);
			add(new Text(dto.getP1st().toString()), 1, 3);
		}else {
			add(team, 0, 0);
			add(new CustomHyperlink(dto.getT().getName(), "name"), 1, 0);
			add(pilot1, 0, 1);
			add(new CustomHyperlink(dto.getP1().getName(), "name"), 1, 1);
			add(pilot2, 0, 2);
			add(new CustomHyperlink(dto.getP2().getName(), "name"), 1, 2);
			add(pts, 0, 3);
			add(new Text(dto.getPts().toString()), 1, 3);
			add(p1st, 0, 4);
			add(new Text(dto.getP1st().toString()), 1, 4);
		}
	}

}
