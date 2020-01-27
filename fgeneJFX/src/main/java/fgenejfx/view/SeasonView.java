package fgenejfx.view;

import fgenejfx.controllers.League;
import fgenejfx.models.Pilot;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SeasonView extends CustomGridPane {

  public SeasonView() {
    super(Pos.CENTER);
    this.add(season(1), 0, 0);
  }

  // private CustomGridPane groups() {
  //
  // }
  private CustomGridPane season(int year) {
    TableView<Pilot> tableView = new TableView<>();
    
    TableColumn<Pilot, Number> nameCol = new TableColumn<>("Name");
    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    tableView.getColumns().add(nameCol);

    // TableColumn<Pilot, Number> ptsCol = new TableColumn<>("Points");
    // ptsCol.setCellValueFactory(Callbacks.stats("getPts", year));
    // tableView.getColumns().add(ptsCol);

    // TableColumn<Pilot, Number> p1stCol = new TableColumn<>("1st");
    // p1stCol.setCellValueFactory(Callbacks.stats("getP1st", year));
    // tableView.getColumns().add(p1stCol);

    TableColumn<Pilot, Number> ptrCol = new TableColumn<>("Point Rate");
    ptrCol.setCellValueFactory(Callbacks.stats("getPtRate", year));
    tableView.getColumns().add(ptrCol);

    TableColumn<Pilot, Number> wrCol = new TableColumn<>("Point Rate");
    wrCol.setCellValueFactory(Callbacks.stats("getWinRate", year));
    tableView.getColumns().add(wrCol);

    tableView.getItems().addAll(League.get().getSeason().pilots());

    return new CustomGridPane(Pos.CENTER, tableView);
 }
}
