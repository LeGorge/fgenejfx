package fgenejfx.view.components.panes;

import org.kordamp.ikonli.javafx.FontIcon;

import fgenejfx.App;
import fgenejfx.controllers.League;
import fgenejfx.models.Season;
import fgenejfx.models.enums.Front;
import fgenejfx.models.enums.State;
import fgenejfx.view.DraftView;
import fgenejfx.view.components.CustomGridPane;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class TitlePaneBySeason extends CustomGridPane{

	public TitlePaneBySeason(Integer year, Front front) {
		super(Pos.CENTER);
		var l = League.get();
		var currentSeason = l.getSeason();
		var currentYear = l.getYear();
		
		Text title = new Text("Season " + year);
		title.getStyleClass().add("title");

		Button before = new Button("", new FontIcon("fa-arrow-left"));
		before.setPrefSize(25, 40);
		if (year != 1) {
			before.setOnAction(e -> {
				App.navigate(front, year - 1);
			});
		} else {
			before.setDisable(true);
		}

		Button after = new Button("", new FontIcon("fa-arrow-right"));
		after.setPrefSize(25, 40);
		if (year != currentYear) {
			after.setOnAction(e -> {
				App.navigate(front, year + 1);
			});
		} else if (front.equals(Front.SEASON) && currentSeason.isEnded()) {
			after.setOnAction(e -> {
				l.changeSeason();
				App.navigate(front, year + 1);
			});
		} else if(front.equals(Front.DRAFT) && !DraftView.inDraft && currentSeason.isInDraft()) {
			after.setOnAction(e -> {
				l.startSeason();
				App.navigate(Front.SEASON, year);
			});
		} else {
			after.setDisable(true);
		}

		add(before, 0, 0);
		add(title, 1, 0);
		add(after, 2, 0);
	}
	
}