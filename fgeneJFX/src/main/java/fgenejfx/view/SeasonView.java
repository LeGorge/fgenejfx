package fgenejfx.view;

import java.awt.Label;
import java.util.Collection;

import fgenejfx.App;
import fgenejfx.controllers.League;
import fgenejfx.models.Group;
import fgenejfx.models.Pilot;
import fgenejfx.models.Team;
import fgenejfx.models.enums.Front;
import fgenejfx.models.enums.LeagueTime;
import fgenejfx.models.enums.MethodSelector;
import fgenejfx.view.engine.CustomGridPane;
import fgenejfx.view.engine.CustomTableView;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class SeasonView extends CustomGridPane {
  
  private League l = League.get();
  private Integer year;

  public SeasonView(int year) {
    super(Pos.TOP_CENTER);
    this.year = year;
    this.minHeightProperty().bind(App.stage.heightProperty().subtract(98));
    
    this.add(mainPane(), 0, 0, 1, 1);
    this.add(statGrid(LeagueTime.SEASON, 420.0, 440.0, true, false, l.season(year).pilots()), 0, 1, 1, 1);
    this.add(statGrid(LeagueTime.SEASON, 460.0, 300.0, false, false, l.season(year).teams()), 0, 2, 1, 1);
  }

  private CustomGridPane mainPane() {
    CustomGridPane pane = new CustomGridPane(Pos.CENTER);
    
    CustomGridPane titlePane = new CustomGridPane(Pos.CENTER);
    Text title = new Text("Season "+ year);
    title.getStyleClass().add("title");
    titlePane.add(title, 0, 0);
    
    pane.add(titlePane, 0, 0);
    pane.add(groups(), 0, 1, 1, 1);
    pane.add(tplayoffGrid(), 0, 2, 1, 1);
    pane.add(pplayoffGrid(), 0, 3, 1, 1);
    
    return pane;
  }
  
  private <A> CustomTableView statGrid(LeagueTime t, Double prefHeight, Double prefWidth,
      boolean addTeamCol, boolean addUpdateButton, Collection<?> children) {
    CustomTableView<A> table = new CustomTableView<>(year);
    table.setPrefHeight(prefHeight);
    table.setPrefWidth(prefWidth);
    
    if(addTeamCol) {
      table.addTeamColumn();
      table.setPrefWidth(540);
    }
    table.addNameColumn()
        .addBySeasonStatColumn(t, MethodSelector.PTS)
        .addBySeasonStatColumn(t, MethodSelector.P1ST)
        .addBySeasonStatColumn(t, MethodSelector.P2ND)
        .addBySeasonStatColumn(t, MethodSelector.P3RD)
        .addBySeasonStatColumn(t, MethodSelector.P4TH)
        .addBySeasonStatColumn(t, MethodSelector.P5TH)
        .addBySeasonStatColumn(t, MethodSelector.P6TH)
        .addBySeasonStatColumn(t, MethodSelector.PER);
    
    table.getItems().addAll((Collection<? extends A>)children);
    table.sortByColumns(2,3,4,5,6,7,8);
    
    return table;
  }
  
  private CustomGridPane tplayoffGrid() {
    LeagueTime time = LeagueTime.TPLAYOFF;
    CustomGridPane tplayoffPane = new CustomGridPane(Pos.CENTER);
    tplayoffPane.add(updateBut(time), 0, 0, 3, 1);
    tplayoffPane.add(statGrid(time, 170.0, 420.0, false, true, l.season(year).pilots(time)), 0, 1, 2, 1);
    
    CustomTableView<Team> table = new CustomTableView<>(year);
    table.setPrefHeight(100);
    table.setPrefWidth(220);
    table.addNameColumn()
        .addBySeasonStatColumn(time, MethodSelector.PTS)
        .addBySeasonStatColumn(time, MethodSelector.P1ST);
    
    table.getItems().addAll(l.season(year).teams(time));
    table.sortByColumns(1,2);
    tplayoffPane.add(table, 2, 1, 1, 1);
    return tplayoffPane;
  }
  
  private CustomGridPane pplayoffGrid() {
    LeagueTime time = LeagueTime.PPLAYOFF;
    CustomGridPane pplayoffPane = new CustomGridPane(Pos.CENTER);
    pplayoffPane.add(updateBut(time), 0,0,1,1);
    pplayoffPane.add(statGrid(time, 170.0, 420.0, false, true, l.season(year).pilots(time)), 0, 1, 1, 1);
    return pplayoffPane;
  }
  
  private Button updateBut(LeagueTime time) {
    Button but = new Button("Update");
    but.setMaxWidth(Double.MAX_VALUE);
    if(l.season(year).readySync(time, null)) {
      but.setOnAction(e ->{
        l.season(year).simulate(time, null);
        App.navigate(Front.SEASON);
      });
    }else{
      but.setDisable(true);
    }
    return but;
  }
  
  private CustomGridPane groups() {
    CustomGridPane grid = new CustomGridPane(Pos.CENTER);
    Group[] gs = l.season(year).getSeason();
    
    Separator sep = new Separator();
    sep.setOrientation(Orientation.HORIZONTAL);
    sep.setPrefHeight(10);
    sep.setVisible(false);
    grid.add(sep, 0,2,3,1);
    
    for (int i=0; i < gs.length; i++) {
      CustomTableView<Pilot> table = new CustomTableView<>(year);
      table.setPrefHeight(170);
      table.setPrefWidth(371);
      
      table.addTeamColumn()
          .addNameColumn()
          .addAgeColumn()
          .addlifeStatColumn(LeagueTime.SEASON, MethodSelector.PTR)
          .addBySeasonStatColumn(LeagueTime.SEASON, MethodSelector.PTS);
      
      table.getItems().addAll(gs[i].pilots());
      if(gs[i].isEmpty()) {
        table.sortByColumns(0);
      }else {
        table.sortByColumns(4);
      }
      
      Button but = new Button("Update");
      but.prefWidthProperty().bind(table.prefWidthProperty());
      if(gs[i].isEmpty()) {
        int f = i;
        but.setOnAction(e ->{
          l.season(year).simulate(LeagueTime.SEASON, f);
          App.navigate(Front.SEASON);
        });
      }else {
        but.setDisable(true);
      }
      
      grid.add(but, i-((i/3)*3), (i/3)*3);
      grid.add(table, i-((i/3)*3), ((i/3)*3)+1);
    }
    return grid;
  }
}
