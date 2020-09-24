package fgenejfx.view;

import java.util.Collection;

import org.kordamp.ikonli.javafx.FontIcon;

import fgenejfx.App;
import fgenejfx.controllers.League;
import fgenejfx.models.Group;
import fgenejfx.models.Pilot;
import fgenejfx.models.Powers;
import fgenejfx.models.Team;
import fgenejfx.models.enums.Front;
import fgenejfx.models.enums.LeagueTime;
import fgenejfx.models.enums.MethodSelector;
import fgenejfx.view.engine.CustomGridPane;
import fgenejfx.view.engine.CustomTableView;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.text.Text;

public class SeasonView extends CustomGridPane {
  
  private League l = League.get();
  private Integer year;
  
  public SeasonView(int year) {
    super(Pos.TOP_CENTER);
    this.year = year;
    this.minHeightProperty().bind(App.stage.heightProperty().subtract(98));
    
    this.add(mainPane(), 0, 0, 1, 1);
    this.add(pSeasonGrid(), 0, 1, 1, 1);
    this.add(this.carTable(), 0, 2, 1, 1);
  }
  
  // ============================================================================================
  // Panels
  // ============================================================================================
  private CustomGridPane mainPane() {
    CustomGridPane pane = new CustomGridPane(Pos.CENTER);
    
//    CustomGridPane titlePane = new CustomGridPane(Pos.CENTER);
//    Text title = new Text("Season "+ year);
//    title.getStyleClass().add("title");
//    titlePane.add(title, 0, 0);
    
    pane.add(titlePane(), 0, 0);
    pane.add(groups(), 0, 1, 1, 1);
    pane.add(tplayoffGrid(), 0, 2, 1, 1);
    pane.add(pplayoffGrid(), 0, 3, 1, 1);
    
    return pane;
  }
  
  private CustomGridPane titlePane() {
    CustomGridPane titlePane = new CustomGridPane(Pos.CENTER);
    Text title = new Text("Season "+ year);
    title.getStyleClass().add("title");
    
    Button before = new Button("", new FontIcon("fa-arrow-left"));
    before.setPrefSize(25,40);
    if(this.year != 1) {
      before.setOnAction(e ->{
        App.navigate(Front.SEASON, this.year-1);
      });
    }else {
      before.setDisable(true);
    }
    
    Button after = new Button("", new FontIcon("fa-arrow-right"));
    after.setPrefSize(25,40);
    if(this.year != this.l.getYear()) {
      after.setOnAction(e ->{
        App.navigate(Front.SEASON, this.year+1);
      });
    }else {
      after.setDisable(true);
    }
    
    titlePane.add(before, 0, 0);
    titlePane.add(title, 1, 0);
    titlePane.add(after, 2, 0);
    return titlePane;
  }
  
  private CustomGridPane tplayoffGrid() {
    LeagueTime time = LeagueTime.TPLAYOFF;
    CustomGridPane tplayoffPane = new CustomGridPane(Pos.CENTER);
    tplayoffPane.add(updateBut(time), 0, 0, 3, 1);
    tplayoffPane.add(statTable(time, 170.0, 420.0, false, true, l.season(year).pilots(time)), 0, 1, 2, 1);
    
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
    pplayoffPane.add(updateBut(time), 0,0);
    pplayoffPane.add(statTable(time, 170.0, 420.0, false, true, l.season(year).pilots(time)), 0, 1);
    return pplayoffPane;
  }
  
  private CustomGridPane pSeasonGrid() {
    CustomGridPane pane = new CustomGridPane(Pos.CENTER);
    pane.add(statTable(LeagueTime.SEASON, 420.0, 840.0, true, false, l.season(year).pilots()), 0, 1);
    return pane;
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
      table.setPrefWidth(430);
      
      table.addTeamColumn()
          .addNameColumn()
          .addTopPosColumn()
          .addScoutReportColumn()
          .addAgeColumn()
          .addlifeStatColumn(LeagueTime.SEASON, MethodSelector.PTR)
          .addBySeasonStatColumn(LeagueTime.SEASON, MethodSelector.PTS);
      
      table.getItems().addAll(gs[i].pilots());
      if(gs[i].isEmpty()) {
        table.sortByColumns(0);
      }else {
        table.sortByColumns(6);
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
  
  private CustomGridPane carTable() {
    CustomGridPane pane = new CustomGridPane(Pos.CENTER);
    pane.setHgap(25);
    pane.setPadding(new Insets(25));
    CustomTableView<Team> table = new CustomTableView<>(year);
    table.setPrefHeight(460);
    table.setPrefWidth(640);
    
    table.addNameColumn()
        .addPowersColumn(Powers.AIR)
        .addPowersColumn(Powers.POWER)
        .addPowersColumn(Powers.SLOWDOWN)
        .addPowersColumn(Powers.GRIP)
        .addPowersColumn(null);
    
    table.getItems().addAll(l.season(year).teams());
//    table.sortByColumns(4);
    pane.add(statTable(LeagueTime.SEASON, 460.0, 840.0, false, false, l.season(year).teams()), 0, 0);
    pane.add(table, 1, 0);
    return pane;
  }
  
  // ============================================================================================
  // privates
  // ============================================================================================
  private <A> CustomTableView statTable(LeagueTime t, Double prefHeight, Double prefWidth,
      boolean addTeamCol, boolean addUpdateButton, Collection<?> children) {
    CustomTableView<A> table = new CustomTableView<>(year);
    table.setPrefHeight(prefHeight);
    table.setPrefWidth(prefWidth);
    
    if(addTeamCol) {
      table.addTeamColumn();
      table.setPrefWidth(prefWidth+100.0D);
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
  
  private Button updateBut(LeagueTime time) {
    Button but = new Button("Update");
    but.setMaxWidth(Double.MAX_VALUE);
    if(l.season(year).readySync(time, null)) {
      but.setOnAction(e ->{
        l.season(year).simulate(time, null);
        
        if(time.equals(LeagueTime.PPLAYOFF)) {
          League.get().changeSeason();
        }
        
        App.navigate(Front.SEASON);
      });
    }else{
      but.setDisable(true);
    }
    return but;
  }
}
