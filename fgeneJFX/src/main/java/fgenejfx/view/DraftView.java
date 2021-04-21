package fgenejfx.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fgenejfx.App;
import fgenejfx.controllers.ContractsController;
import fgenejfx.controllers.HistoryController;
import fgenejfx.models.Draft;
import fgenejfx.models.Pilot;
import fgenejfx.models.Team;
import fgenejfx.models.enums.Front;
import fgenejfx.models.enums.LeagueTime;
import fgenejfx.models.enums.State;
import fgenejfx.utils.Utils;
import fgenejfx.view.components.CustomGridPane;
import fgenejfx.view.components.CustomTitledPane;
import fgenejfx.view.components.cards.PilotCard;
import fgenejfx.view.components.panes.TitlePaneBySeason;
import fgenejfx.view.components.tableViews.DraftsTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class DraftView extends CustomGridPane {
	private static List<Pilot> pilots = new ArrayList<>();
	private static LinkedHashMap<Team, Double> teams = new LinkedHashMap<>();
	public static Boolean inDraft = false;
	
	private Integer year;
	private Pilot currentPilot;

	public static void setDraftData(Set<Pilot> pilots, Set<Team> teams){
		var time = LeagueTime.SEASON;
		DraftView.pilots = pilots.stream()
				.sorted((p1, p2) -> p2.getPPR(time).compareTo(p1.getPPR(time)))
				.collect(Collectors.toList());
		teams.stream().forEach(t -> DraftView.teams.put(t, 1.0));
		inDraft = true;
	}
	
	public DraftView(Integer year) {
		super(Pos.TOP_CENTER);
		this.minHeightProperty().bind(App.stage.heightProperty().subtract(67));
		this.add(new TitlePaneBySeason(year, Front.DRAFT), 0, 0);
		this.year = year;

		if (l.season(year).isInDraft()) {
			add(this.draftPane(), 0, 1);
			add(new CustomGridPane(this.draftButton()), 0, 2);
		} else {
			if(year == 8) {
				return;
			}
			add(this.draftHistoryPane(), 0, 1);
		}
	}

	private CustomGridPane draftHistoryPane() {
		return draftedPane();
	}
	
	private CustomGridPane draftPane() {
		HBox pane = new HBox();
		if(!DraftView.pilots.isEmpty()) {
			this.currentPilot = DraftView.pilots.get(0);
			pane.getChildren().add(pilotPane());
		}
		if(!DraftView.teams.isEmpty()) {
			pane.getChildren().add(teamPane());
		}
		pane.getChildren().add(draftedPane());
		pane.setSpacing(50);
		return new CustomGridPane(pane);
	}
	
	// ============================================================================================
	// Pilot Pane
	// ============================================================================================
	private CustomGridPane pilotPane() {
		CustomTitledPane pane = new CustomTitledPane("Pilot", new PilotCard(currentPilot, this.year));
		return new CustomGridPane(pane);
	}
	
	// ============================================================================================
	// Teams Pane
	// ============================================================================================
	private CustomGridPane teamPane() {
		var data = DraftView.teams.keySet().stream()
				.map(t -> new PieChart.Data(t.getName(), DraftView.teams.get(t)))
				.collect(Collectors.toList());
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(data);
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Teams");
        chart.setLabelLineLength(10);
        chart.setLegendSide(Side.LEFT);
        chart.setPrefHeight(700);
        chart.setPrefWidth(800);
        HBox.setHgrow(chart, Priority.ALWAYS);
		return new CustomGridPane(chart);
	}
	
	// ============================================================================================
	// Drafted Pane
	// ============================================================================================
	private CustomGridPane draftedPane() {
		CustomGridPane pane = new CustomGridPane(Pos.CENTER);
		DraftsTableView table = new DraftsTableView(year);

		table.addPilotColumn().addTeamColumn().addYearsColumn();
		table.getItems().addAll(historyController.history(year).getDrafts());

		table.setPrefWidth(300);
		table.setDynamicHeight();
		pane.add(table, 0, 0);
		
		return pane;
	}
	
	// ============================================================================================
	// Draft Button
	// ============================================================================================
	private Button draftButton() {
		Button b = new Button("Select");
		b.setDisable(!DraftView.inDraft);
		b.setPrefSize(300, 100);
		
		b.setOnAction(e -> {
			Team chosenTeam = choose();
			Boolean isTeamFull = ContractsController.get().newContract(currentPilot, chosenTeam);
			HistoryController.get().history(year).save(new Draft(
					currentPilot, 
					chosenTeam, 
					ContractsController.get().remainingYearsOfContract(currentPilot)
				));
			if(isTeamFull) {
				DraftView.teams.remove(chosenTeam);
			}
			DraftView.pilots.remove(currentPilot);
			if(DraftView.pilots.isEmpty() && DraftView.teams.isEmpty()) {
				DraftView.inDraft = false;
			}
			App.update();
		});
		
		return b;
	}
	
	private Team choose() {
		double total = DraftView.teams.values().stream().reduce(0.0, Double::sum);
		double any = Utils.rand.nextDouble() * total;
		Double aux = 0.0;
		for (Team t : DraftView.teams.keySet()) {
			aux += DraftView.teams.get(t);
			if(aux >= any) {
				return t; 
			}
		}
		return null;
	}
}
