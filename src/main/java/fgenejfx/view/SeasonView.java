package fgenejfx.view;

import java.util.*;

import fgenejfx.interfaces.StatsMonitorable;
import fgenejfx.models.utils.RaceStatsComparator;
import org.kordamp.ikonli.javafx.FontIcon;

import fgenejfx.App;
import fgenejfx.controllers.ContractsController;
import fgenejfx.models.Contract;
import fgenejfx.models.Group;
import fgenejfx.models.Pilot;
import fgenejfx.models.Powers;
import fgenejfx.models.Season;
import fgenejfx.models.Team;
import fgenejfx.models.enums.Front;
import fgenejfx.models.enums.LeagueTime;
import fgenejfx.models.enums.MethodSelector;
import fgenejfx.models.enums.State;
import fgenejfx.view.components.CustomGridPane;
import fgenejfx.view.components.CustomTableView;
import fgenejfx.view.components.panes.TitlePaneBySeason;
import fgenejfx.view.components.tableViews.CurrentContractsTableView;
import fgenejfx.view.components.tableViews.HistoryContractsTableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;

public class SeasonView extends CustomGridPane {
	private Integer year;
	private Season season;

	public SeasonView(int year) {
		super(Pos.TOP_CENTER);
		this.year = year;
		this.season = l.season(year);
		this.minHeightProperty().bind(App.stage.heightProperty().subtract(98));
		
		this.add(mainPane(), 0, 0, 1, 1);
		this.add(newsPane(), 0, 1, 1, 1);
		this.add(pSeasonGrid(), 0, 2, 1, 1);
		this.add(carTable(), 0, 3, 1, 1);
	}

	private Boolean isCurrentSeason(){
		return this.year == l.getYear();
	}

	// ============================================================================================
	// Main Panel - Groups and playoffs
	// ============================================================================================
	private CustomGridPane mainPane() {
		CustomGridPane pane = new CustomGridPane(Pos.CENTER);
		pane.add(new TitlePaneBySeason(this.year, Front.SEASON), 0, 0);
		pane.add(groups(), 0, 1, 1, 1);
		pane.add(tplayoffGrid(), 0, 2, 1, 1);
		pane.add(pplayoffGrid(), 0, 3, 1, 1);
		return pane;
	}

	private CustomGridPane tplayoffGrid() {
		LeagueTime time = LeagueTime.TPLAYOFF;
		CustomGridPane tplayoffPane = new CustomGridPane(Pos.CENTER);
		tplayoffPane.add(updateBut(time), 0, 0, 4, 1);
		tplayoffPane.add(playoffStatTable(time, season.playoffGroup(time)), 0, 1, 3, 1);

		CustomTableView<Team> table = new CustomTableView<>(year);
		table.setPrefHeight(100);
		table.setPrefWidth(320);

		table.addNameColumn();

		if(!season.isEnded()){
			table.addSimpleColumn("G", "Golds", "lifeStats.tGold")
					.addSimpleColumn("S", "Silvers", "lifeStats.tSilver")
					.addSimpleColumn("B", "Bronzes", "lifeStats.tBronze");
		}

		table.addBySeasonStatColumn(time, MethodSelector.PTS)
				.addBySeasonStatColumn(time, MethodSelector.P1ST);

		table.getItems().addAll(season.teams(time));
		if(!season.isEnded()){
			table.sortByColumns(4, 5);
		}else{
			table.sortByColumns(1, 2);
		}
		tplayoffPane.add(table, 3, 1, 1, 1);
		return tplayoffPane;
	}

	private CustomGridPane pplayoffGrid() {
		LeagueTime time = LeagueTime.PPLAYOFF;
		CustomGridPane pplayoffPane = new CustomGridPane(Pos.CENTER);
		pplayoffPane.add(updateBut(time), 0, 0);
		pplayoffPane.add(playoffStatTable(time, season.playoffGroup(time)), 0, 1);
		return pplayoffPane;
	}

	private CustomGridPane pSeasonGrid() {
		CustomGridPane pane = new CustomGridPane(Pos.CENTER);
		pane.setHgap(25);
		pane.setPadding(new Insets(25));
		pane.add(statTable(LeagueTime.SEASON, 420.0, 840.0, true, false, season.pilots()), 0, 0);

		if(isCurrentSeason()){
			var table = CurrentContractsTableView.build(year);
			table.setPrefHeight(460);
			table.setPrefWidth(400);
			pane.add(table, 1, 0);
		}

		return pane;
	}

	private CustomGridPane groups() {
		CustomGridPane grid = new CustomGridPane(Pos.CENTER);
		Group[] gs = season.getSeason();

		Separator sep = new Separator();
		sep.setOrientation(Orientation.HORIZONTAL);
		sep.setPrefHeight(10);
		sep.setVisible(false);
		grid.add(sep, 0, 2, 3, 1);

		for (int i = 0; i < gs.length; i++) {
			CustomTableView<Pilot> table = new CustomTableView<>(year);
			table.setPrefHeight(170);
			table.setPrefWidth(500);

//			Start fields
			table.addTeamColumn()
					.addNameColumn()
					.addAgeColumn()
					.addTopPosColumn()
					.addScoutReportColumn();

//			Conditional Fields
			if (!season.isEnded()) {
				table.addSimpleColumn("SC", "Season Champ", "lifeStats.pPlayoffs")
						.addPerRoundColumn(LeagueTime.SEASON, MethodSelector.PPR)
						.addPerRoundColumn(LeagueTime.SEASON, MethodSelector.PERPR);
			}

//			End Fields
			table.addBySeasonStatColumn(LeagueTime.SEASON, MethodSelector.PTS)
					.addBySeasonStatColumn(LeagueTime.SEASON, MethodSelector.PER);

			var sorting_container = gs[i].pilotsSortedNaturally(year);
			if(!gs[i].isEmpty()){
				Comparator byStats = new RaceStatsComparator(gs[i]);
				sorting_container.sort(byStats);
			}
			table.getItems().addAll(sorting_container);

			if (this.season.getState() != State.INSEASON) {
				table.colorRow(Collections.singletonMap(0, "gold"));
			}

			Button but = new Button("Update");
			but.prefWidthProperty().bind(table.prefWidthProperty());
			if (gs[i].isEmpty()) {
				int f = i;
				but.setOnAction(e -> {
					if(App.prod){
						season.sync(f);
					}else{
						l.season(year).simulate(LeagueTime.SEASON, f);
					}
					App.update();
				});
			} else {
				but.setDisable(true);
			}

			grid.add(but, i - ((i / 3) * 3), (i / 3) * 3);
			grid.add(table, i - ((i / 3) * 3), ((i / 3) * 3) + 1);
		}
		return grid;
	}

	// ============================================================================================
	// News Panel
	// ============================================================================================
	private CustomGridPane newsPane() {
		CustomGridPane pane = new CustomGridPane(Pos.CENTER);
		List<String> news = newsController.get(this.season);
		if (news != null) {
			CustomTableView<String> table = new CustomTableView<>(year);
			table.setPrefWidth(1480.0);

//      ObservableList<String> details = FXCollections.observableArrayList(news);

			TableColumn<String, String> col = new TableColumn<>("News");
			col.setPrefWidth(1480.0);
			col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
			col.setCellFactory(c -> new TableCell<String, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (item != null) {
						Text text = new Text(item);
						text.setStyle(" -fx-font-size: 12pt;" + " -fx-text-wrap: true;" +
//                          " -fx-font-weight: bold;;" +
						" -fx-text-alignment:center;");
						text.setWrappingWidth(col.getPrefWidth() - 15);
						this.setPrefHeight(text.getLayoutBounds().getHeight() + 10);
						this.setGraphic(text);
					}
				}
			});

			table.getColumns().add(col);
			table.getItems().addAll(news);

			pane.add(table, 0, 0);
		} else {
			newsController.add(this.season, "teste Season " + this.season.getYear());
		}
		return pane;
	}

	// ============================================================================================
	// Team Stats and Cars Panel
	// ============================================================================================
	private CustomGridPane carTable() {
		CustomGridPane pane = new CustomGridPane(Pos.CENTER);
		pane.setHgap(25);
		pane.setPadding(new Insets(25));
		CustomTableView<Team> table = new CustomTableView<>(year);
		table.setPrefHeight(460);
		table.setPrefWidth(640);

		table.addNameColumn().addGroupColumn().addPowersColumn(Powers.AIR).addPowersColumn(Powers.POWER)
				.addPowersColumn(Powers.SLOWDOWN).addPowersColumn(Powers.GRIP).addPowersColumn(null);

		table.getItems().addAll(season.teams());
		table.sortByColumns(6, 0);
		pane.add(statTable(LeagueTime.SEASON, 460.0, 840.0, false, false, l.season(year).teams()), 0, 0);
		pane.add(table, 1, 0);
		return pane;
	}

	// ============================================================================================
	// privates
	// ============================================================================================
	private CustomTableView playoffStatTable(LeagueTime t, Group g) {
		CustomTableView<StatsMonitorable> table = new CustomTableView<>(year);
		table.setPrefHeight(170.0);
		table.setPrefWidth(600.0);

		table.addTeamColumn().addNameColumn();

		if (!season.isEnded()) {
			table.addSimpleColumn("RGold", "Round Golds", String.format("lifeStats.%sGold", LeagueTime.param(t)))
					.addSimpleColumn("RSilver", "Round Silvers", String.format("lifeStats.%sSilver", LeagueTime.param(t)))
					.addSimpleColumn("RBronze", "Round Bronzes", String.format("lifeStats.%sBronze", LeagueTime.param(t)))
					.addPerRoundColumn(t, MethodSelector.PPR)
					.addPerRoundColumn(t, MethodSelector.PERPR);
		}

		table.addBySeasonStatColumn(t, MethodSelector.PTS).addBySeasonStatColumn(t, MethodSelector.P1ST)
				.addBySeasonStatColumn(t, MethodSelector.P2ND).addBySeasonStatColumn(t, MethodSelector.P3RD)
				.addBySeasonStatColumn(t, MethodSelector.P4TH).addBySeasonStatColumn(t, MethodSelector.P5TH)
				.addBySeasonStatColumn(t, MethodSelector.P6TH).addBySeasonStatColumn(t, MethodSelector.PER)
				.addHiddenContractColumn();

		if(g != null){
			List<Pilot> sorting_container = null;
			switch (t) {
				case PPLAYOFF:
					sorting_container = g.pilots();
					break;
				case TPLAYOFF:
					sorting_container = g.pilotsSortedNaturally(year);
					break;
			}
			if(!g.isEmpty()){
				Comparator byStats = new RaceStatsComparator(g);
				sorting_container.sort(byStats);
			}
			table.getItems().addAll(sorting_container);
		}
		return table;
	}

	private <A> CustomTableView statTable(LeagueTime t, Double prefHeight, Double prefWidth, boolean addTeamCol,
			boolean addUpdateButton, Collection<?> children) {
		CustomTableView<A> table = new CustomTableView<>(year);
		table.setPrefHeight(prefHeight);
		table.setPrefWidth(prefWidth);

		int control = 0;
		if (addTeamCol) {
			table.addTeamColumn();
			control++;
			table.setPrefWidth(prefWidth + 100.0D);
		}
		table.addNameColumn().addBySeasonStatColumn(t, MethodSelector.PTS).addBySeasonStatColumn(t, MethodSelector.P1ST)
				.addBySeasonStatColumn(t, MethodSelector.P2ND).addBySeasonStatColumn(t, MethodSelector.P3RD)
				.addBySeasonStatColumn(t, MethodSelector.P4TH).addBySeasonStatColumn(t, MethodSelector.P5TH)
				.addBySeasonStatColumn(t, MethodSelector.P6TH).addBySeasonStatColumn(t, MethodSelector.PER)
				.addGroupColumn();

		table.getItems().addAll((Collection<? extends A>) children);
		table.sortByColumns(1 + control, 2 + control, 3 + control, 4 + control, 5 + control, 6 + control, 7 + control);

		return table;
	}

	private Button updateBut(LeagueTime time) {
		Button but = new Button("Update");
		but.setMaxWidth(Double.MAX_VALUE);
		if (season.readySync(time, null)) {
			but.setOnAction(e -> {
				if(App.prod){
					season.sync(time);
				}else{
					l.season(year).simulate(time, null);
				}
//        if(time.equals(LeagueTime.PPLAYOFF)) {
//          League.get().changeSeason();
//        }

				App.update();
			});
		} else {
			but.setDisable(true);
		}
		return but;
	}
}
