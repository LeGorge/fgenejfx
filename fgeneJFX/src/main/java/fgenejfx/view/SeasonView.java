package fgenejfx.view;

import java.util.Collection;
import java.util.LinkedHashMap;

import fgenejfx.controllers.League;
import fgenejfx.interfaces.StatsMonitorable;
import fgenejfx.models.Pilot;
import fgenejfx.models.Team;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SeasonView extends CustomGridPane {

  public SeasonView() {
    super(Pos.CENTER);
    this.add(pilotSeason(1, Pilot.class), 0, 0);
//    this.add(pilotSeason(1, Team.class), 0, 1);
  }

  // private CustomGridPane groups() {
  //
  // }
  private <A> CustomGridPane pilotSeason(int year, Class<A> c) {
    TableView<A> tableView = new TableView<>();
    
    TableColumn<A, String> nameCol = new TableColumn<>("Name");
    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    tableView.getColumns().add(nameCol);
    
    TableColumn<A, String> teamCol = new TableColumn<>("Team");
    
    teamCol.setCellValueFactory(new Callbacks<A,String>().stringCol(League.get(),
        MethodMapper.teamOf(year)));
    tableView.getColumns().add(teamCol);

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
      tableView.getItems().addAll((Collection<? extends A>) League.get().getSeason().pilots());
    }
    if(c.equals(Team.class)) {
      tableView.getItems().addAll((Collection<? extends A>) League.get().getSeason().teams());
    }

    return new CustomGridPane(Pos.CENTER, tableView);
 }
  
}
