package fgenejfx.view;

import java.util.Collection;

import fgenejfx.controllers.League;
import fgenejfx.models.Group;
import fgenejfx.models.Pilot;
import fgenejfx.models.Team;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;

public class SeasonView extends CustomGridPane {

  public SeasonView(int year) {
    super(Pos.CENTER);
    this.add(pilotSeason(year, Pilot.class), 0, 0);
    this.add(groups(year), 1, 0);
  }

  // private CustomGridPane groups() {
  //
  // }
  private <A> CustomGridPane pilotSeason(int year, Class<A> c) {
    TableView<A> tableView = new TableView<>();
    tableView.setPrefHeight(520);
    
    TableColumn<A, String> nameCol = new TableColumn<>("Name");
    nameCol.setPrefWidth(100);
    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    tableView.getColumns().add(nameCol);
    
    TableColumn<A, Number> ptrCol = new TableColumn<>("Point Rate");
    ptrCol.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get(),
        MethodMapper.ptr(year)));
    tableView.getColumns().add(ptrCol);

    TableColumn<A, Number> wrCol = new TableColumn<>("Win Rate");
    wrCol.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get(),
        MethodMapper.winr(year)));
    tableView.getColumns().add(wrCol);
    
    TableColumn<A, Number> perCol = new TableColumn<>("PER");
    perCol.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get(),
        MethodMapper.per(year)));
    tableView.getColumns().add(perCol);

    if(c.equals(Pilot.class)) {
      tableView.getItems().addAll((Collection<? extends A>) League.get().season(year).pilots());
    }
    if(c.equals(Team.class)) {
      tableView.getItems().addAll((Collection<? extends A>) League.get().season(year).teams());
    }

    return new CustomGridPane(Pos.CENTER, tableView);
 }
  
  private CustomGridPane groups(int year) {
    CustomGridPane grid = new CustomGridPane(Pos.CENTER);
    Group[] gs = League.get().season(year).getSeason();
    for (int i=0; i < gs.length; i++) {
      TableView<Pilot> tableView = new TableView<>();
      tableView.setPrefHeight(170);
      
      TableColumn<Pilot, String> teamCol = new TableColumn<>("Team");
      teamCol.setPrefWidth(100);
      teamCol.setCellValueFactory(new Callbacks<Pilot,String>().stringCol(League.get(),
          MethodMapper.teamOf(year)));
      tableView.getColumns().add(teamCol);
      
      TableColumn<Pilot, String> nameCol = new TableColumn<>("Name");
      nameCol.setPrefWidth(100);
      nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
      tableView.getColumns().add(nameCol);
      
      TableColumn<Pilot, Number> ageCol = new TableColumn<>("Years");
      ageCol.setCellValueFactory(new PropertyValueFactory<>("yearsInTheLeague"));
      tableView.getColumns().add(ageCol);
      
      TableColumn<Pilot, Number> ptsCol = new TableColumn<>("Points");
      ptsCol.setCellValueFactory(new Callbacks<Pilot,Number>().stringCol(League.get(),
          MethodMapper.pts(year)));
      tableView.getColumns().add(ptsCol);
      
      tableView.getItems().addAll(gs[i].pilots());
      tableView.getSortOrder().addAll(teamCol);
      
      grid.add(tableView, i-((i/2)*2), i/2);
    }
    return grid;
  }
}
