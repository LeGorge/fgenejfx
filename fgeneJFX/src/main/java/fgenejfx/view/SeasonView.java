package fgenejfx.view;

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
    this.add(pilotSeason(1, Team.class), 0, 1);
  }

  // private CustomGridPane groups() {
  //
  // }
  private CustomGridPane pilotSeason(int year, Class<? extends StatsMonitorable> c) {
    TableView<StatsMonitorable> tableView = new TableView<>();
    
    TableColumn<StatsMonitorable, Number> nameCol = new TableColumn<>("Name");
    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    tableView.getColumns().add(nameCol);

    TableColumn<StatsMonitorable, Number> ptrCol = new TableColumn<>("Point Rate");
    ptrCol.setCellValueFactory(Callbacks.stats("getPtRate", year));
    tableView.getColumns().add(ptrCol);

    TableColumn<StatsMonitorable, Number> wrCol = new TableColumn<>("Win Rate");
    wrCol.setCellValueFactory(Callbacks.stats("getWinRate", year));
    tableView.getColumns().add(wrCol);

    if(c.equals(Pilot.class)) {
      tableView.getItems().addAll(League.get().getSeason().pilots());
    }
    if(c.equals(Team.class)) {
      tableView.getItems().addAll(League.get().getSeason().teams());
    }

    return new CustomGridPane(Pos.CENTER, tableView);
 }
  
}
