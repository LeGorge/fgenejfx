package fgenejfx.view;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.kordamp.ikonli.javafx.FontIcon;

import fgenejfx.App;
import fgenejfx.controllers.League;
import fgenejfx.controllers.NewsController;
import fgenejfx.models.Group;
import fgenejfx.models.Pilot;
import fgenejfx.models.Powers;
import fgenejfx.models.Season;
import fgenejfx.models.Team;
import fgenejfx.models.enums.Front;
import fgenejfx.models.enums.LeagueTime;
import fgenejfx.models.enums.MethodSelector;
import fgenejfx.models.enums.State;
import fgenejfx.utils.ViewUtils;
import fgenejfx.view.engine.CustomGridPane;
import fgenejfx.view.engine.CustomHyperlink;
import fgenejfx.view.engine.CustomTableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

public class SeasonView extends CustomGridPane {
  
  private League l = League.get();
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
  
  // ============================================================================================
  // Main Panel - Groups and playoffs
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
    }else if(season.isEnded()){
      after.setOnAction(e ->{
        League.get().changeSeason();
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
    tplayoffPane.add(playoffStatTable(time, season.pilots(time)), 0, 1, 2, 1);
    
    CustomTableView<Team> table = new CustomTableView<>(year);
    table.setPrefHeight(100);
    table.setPrefWidth(220);
    table.addNameColumn()
        .addBySeasonStatColumn(time, MethodSelector.PTS)
        .addBySeasonStatColumn(time, MethodSelector.P1ST);
    
    table.getItems().addAll(season.teams(time));
    table.sortByColumns(1,2);
    tplayoffPane.add(table, 2, 1, 1, 1);
    return tplayoffPane;
  }
  
  private CustomGridPane pplayoffGrid() {
    LeagueTime time = LeagueTime.PPLAYOFF;
    CustomGridPane pplayoffPane = new CustomGridPane(Pos.CENTER);
    pplayoffPane.add(updateBut(time), 0,0);
    pplayoffPane.add(playoffStatTable(time, season.pilots(time)), 0, 1);
    return pplayoffPane;
  }
  
  private CustomGridPane pSeasonGrid() {
    CustomGridPane pane = new CustomGridPane(Pos.CENTER);
    pane.add(statTable(LeagueTime.SEASON, 420.0, 840.0, true, false, season.pilots()), 0, 1);
    return pane;
  }
  
  private CustomGridPane groups() {
    CustomGridPane grid = new CustomGridPane(Pos.CENTER);
    Group[] gs = season.getSeason();
    
    Separator sep = new Separator();
    sep.setOrientation(Orientation.HORIZONTAL);
    sep.setPrefHeight(10);
    sep.setVisible(false);
    grid.add(sep, 0,2,3,1);
    
    for (int i=0; i < gs.length; i++) {
      CustomTableView<Pilot> table = new CustomTableView<>(year);
      table.setPrefHeight(170);
      table.setPrefWidth(500);
      
      table.addTeamColumn()
          .addNameColumn()
          .addTopPosColumn()
          .addScoutReportColumn();
      
      if(season.isEnded()) {
        table.addBySeasonStatColumn(LeagueTime.SEASON, MethodSelector.PTS)
            .addBySeasonStatColumn(LeagueTime.SEASON, MethodSelector.P1ST)
            .addBySeasonStatColumn(LeagueTime.SEASON, MethodSelector.PER);
      }else {
        table.addAgeColumn()
            .addPerRoundColumn(LeagueTime.SEASON, MethodSelector.PPR)
            .addPerRoundColumn(LeagueTime.SEASON, MethodSelector.PERPR)
            .addBySeasonStatColumn(LeagueTime.SEASON, MethodSelector.PTS);
      }
      
      table.getItems().addAll(gs[i].pilots());
      if(gs[i].isEmpty()) {
        table.sortByColumns(0);
      }else {
        if(season.isEnded()) {
          table.sortByColumns(4);
        }else{
          table.sortByColumns(7);
        }
      }
      
      if(this.season.getState() != State.SEASON) {
        table.colorRow(Collections.singletonMap(0, "gold"));
      }
      
      Button but = new Button("Update");
      but.prefWidthProperty().bind(table.prefWidthProperty());
      if(gs[i].isEmpty()) {
        int f = i;
        but.setOnAction(e ->{
//          l.season(year).simulate(LeagueTime.SEASON, f);
          season.sync(f);
          App.update();
        });
      }else {
        but.setDisable(true);
      }
      
      grid.add(but, i-((i/3)*3), (i/3)*3);
      grid.add(table, i-((i/3)*3), ((i/3)*3)+1);
    }
    return grid;
  }
  
 // ============================================================================================
 // News Panel 
 // ============================================================================================
  private CustomGridPane newsPane() {
    CustomGridPane pane = new CustomGridPane(Pos.CENTER);
    List<String> news = NewsController.get().get(this.season);
    if(news != null) {
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
            text.setStyle(" -fx-font-size: 12pt;" +
                          " -fx-text-wrap: true;" +
//                          " -fx-font-weight: bold;;" +
                          " -fx-text-alignment:center;");
            text.setWrappingWidth(col.getPrefWidth() - 15);
            this.setPrefHeight(text.getLayoutBounds().getHeight()+10);
            this.setGraphic(text);
          }
        }
      });
      
      table.getColumns().add(col);
      table.getItems().addAll(news);
      
      pane.add(table, 0, 0);
    }else {
      NewsController.get().add(this.season, "teste Season "+this.season.getYear());
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
    
    table.addNameColumn()
        .addGroupColumn()
        .addPowersColumn(Powers.AIR)
        .addPowersColumn(Powers.POWER)
        .addPowersColumn(Powers.SLOWDOWN)
        .addPowersColumn(Powers.GRIP)
        .addPowersColumn(null);
    
    table.getItems().addAll(season.teams());
    table.sortByColumns(6,0);
    pane.add(statTable(LeagueTime.SEASON, 460.0, 840.0, false, false, l.season(year).teams()), 0, 0);
    pane.add(table, 1, 0);
    return pane;
  }
  
  // ============================================================================================
  // privates
  // ============================================================================================
  private <A> CustomTableView playoffStatTable(LeagueTime t, Collection<?> children) {
    CustomTableView<A> table = new CustomTableView<>(year);
    table.setPrefHeight(170.0);
    table.setPrefWidth(600.0);
    
    table.addTeamColumn()
    .addNameColumn();
    
    if(!season.isEnded()) {
      table.addPerRoundColumn(t, MethodSelector.PPR)
      .addPerRoundColumn(t, MethodSelector.PERPR);
    }
    
    table.addBySeasonStatColumn(t, MethodSelector.PTS)
    .addBySeasonStatColumn(t, MethodSelector.P1ST)
    .addBySeasonStatColumn(t, MethodSelector.P2ND)
    .addBySeasonStatColumn(t, MethodSelector.P3RD)
    .addBySeasonStatColumn(t, MethodSelector.P4TH)
    .addBySeasonStatColumn(t, MethodSelector.P5TH)
    .addBySeasonStatColumn(t, MethodSelector.P6TH)
    .addBySeasonStatColumn(t, MethodSelector.PER);
    
    table.getItems().addAll((Collection<? extends A>)children);
    if(season.isEnded()) {
      table.sortByColumns(2,3,4,5,6,7,8);
    }else {
      table.sortByColumns(4,5,6,7,8,9,10);
    }
    
    return table;
  }
  
  private <A> CustomTableView statTable(LeagueTime t, Double prefHeight, Double prefWidth,
      boolean addTeamCol, boolean addUpdateButton, Collection<?> children) {
    CustomTableView<A> table = new CustomTableView<>(year);
    table.setPrefHeight(prefHeight);
    table.setPrefWidth(prefWidth);
    
    int control = 0;
    if(addTeamCol) {
      table.addTeamColumn();
      control++;
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
        .addBySeasonStatColumn(t, MethodSelector.PER)
        .addGroupColumn();
    
    table.getItems().addAll((Collection<? extends A>)children);
    table.sortByColumns(1+control,2+control,3+control,4+control,5+control,6+control,7+control);
    
    return table;
  }
  
  private Button updateBut(LeagueTime time) {
    Button but = new Button("Update");
    but.setMaxWidth(Double.MAX_VALUE);
    if(season.readySync(time, null)) {
      but.setOnAction(e ->{
//        l.season(year).simulate(time, null);
        season.sync(time);
        
//        if(time.equals(LeagueTime.PPLAYOFF)) {
//          League.get().changeSeason();
//        }
        
        App.update();
      });
    }else{
      but.setDisable(true);
    }
    return but;
  }
}
