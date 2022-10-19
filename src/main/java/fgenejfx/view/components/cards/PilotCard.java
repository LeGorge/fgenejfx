package fgenejfx.view.components.cards;

import java.util.NoSuchElementException;

import fgenejfx.App;
import fgenejfx.controllers.League;
import fgenejfx.models.Pilot;
import fgenejfx.models.enums.LeagueTime;
import fgenejfx.utils.Utils;
import fgenejfx.view.components.CustomGridPane;
import fgenejfx.view.components.CustomTitledPane;
import fgenejfx.view.components.LabelFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class PilotCard extends CustomGridPane {
	
	public PilotCard(Pilot p, Integer year) {
		super();
		this.setAlignment(Pos.CENTER);
		this.setGridLinesVisible(false);
		
		add(LabelFactory.subtitle32(p.getName()), 0, 0);
		add(statsPane(p, year), 0, 1);
		
		var life = p.getLifeStats();
		add(new CustomTitledPane("For pilots",medalsPane(life.getpGold(), life.getpSilver(), life.getpBronze())),0,2);
		add(new CustomTitledPane("For teams",medalsPane(life.gettGold(), life.gettSilver(), life.gettBronze())),0,3);
		
		allignAllH();
	}
	
	private CustomGridPane statsPane(Pilot p, Integer year) {
		var pane = new CustomGridPane();
		
		pane.add(LabelFactory.subtitle24("PPR"), 0, 0);
		pane.add(LabelFactory.normal(Utils.integerFormat.format(p.getPPR(LeagueTime.SEASON))), 0, 1);
		
		pane.add(LabelFactory.subtitle24("WPR"), 3, 0);
		pane.add(LabelFactory.normal(Utils.twoPlacesFormat.format(p.getWPR(LeagueTime.SEASON))), 3, 1);
		
		pane.add(LabelFactory.subtitle24("LAST"), 6, 0);
		try {
			pane.add(LabelFactory.normal(League.get().season(year-1).topPosOf(p)), 6, 1);
		} catch (NoSuchElementException e) {
			pane.add(LabelFactory.normal("-"), 6, 1);
		}
		
		pane.allignAllH();
		return pane;
	}
	
	private CustomGridPane medalsPane(Integer g, Integer s, Integer b) {
		var pane = new CustomGridPane();
		
		pane.add(getGraphic("goldMedal"), 0, 0);
		pane.add(LabelFactory.normal(g.toString()), 0, 1);
		pane.add(getGraphic("silverMedal"), 3, 0);
		pane.add(LabelFactory.normal(s.toString()), 3, 1);
		pane.add(getGraphic("bronzeMedal"), 6, 0);
		pane.add(LabelFactory.normal(b.toString()), 6, 1);
		
		pane.allignAllH();
		return pane;
	}
	
	private Label getGraphic(String g) {
		var label = new Label();
		var view = new ImageView(App.graphics.get(g));
		view.setPreserveRatio(true);
		view.setFitHeight(35);
		label.setGraphic(view);
		return label;
	}
	
}
