package fgenejfx.view.engine;

import fgenejfx.controllers.League;
import fgenejfx.models.Team;
import fgenejfx.models.enums.LeagueTime;
import fgenejfx.models.enums.MethodSelector;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CustomTableView<A> extends TableView<A>{
  
  private int year;
  
  public CustomTableView(int year) {
    this.year = year;
  }

  // ============================================================================================
  // Info
  // ============================================================================================
  public CustomTableView<A> addNameColumn() {
    TableColumn<A, String> nameCol = new TableColumn<>("Name");
    nameCol.setPrefWidth(100);
    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    nameCol.setCellFactory(col -> new TableCell<A, String>() {
		@Override
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			if (empty) {
                setGraphic(null);
            } else {
                setGraphic(new CustomHyperlink(item, "name"));
            }
		}
    });
    this.getColumns().add(nameCol);
    return this;
  }
  
  public CustomTableView<A> addTeamColumn() {
    TableColumn<A, Team> teamCol = new TableColumn<>("Team");
    teamCol.setPrefWidth(100);
    teamCol.setCellValueFactory(new Callbacks<A,Team>().stringCol(League.get(),
        MethodMapper.teamOf(year)));
    teamCol.setCellFactory(col -> new TableCell<A, Team>() {
		@Override
		protected void updateItem(Team item, boolean empty) {
			super.updateItem(item, empty);
			if (empty) {
                setGraphic(null);
            } else {
                setGraphic(new CustomHyperlink(item.getName(), "name"));
            }
		}
    });
    this.getColumns().add(teamCol);
    return this;
  }
  
  public CustomTableView<A> addAgeColumn() {
    TableColumn<A, Number> ageCol = new TableColumn<>("Age");
    ageCol.setCellValueFactory(new PropertyValueFactory<>("yearsInTheLeague"));
    this.getColumns().add(ageCol);
    return this;
  }
  
  // ============================================================================================
  // By Season Columns
  // ============================================================================================
  public CustomTableView<A> addBySeasonStatColumn(LeagueTime time, MethodSelector sel) {
    TableColumn<A, Number> col = new TableColumn<>(sel.getName());
    col.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get(),
        MethodMapper.bySeasonStat(year, time, sel)));
    this.getColumns().add(col);
    return this;
  }
  
  // ============================================================================================
  // Life Columns
  // ============================================================================================
  public CustomTableView<A> addlifeStatColumn(LeagueTime time, MethodSelector sel) {
    TableColumn<A, Number> col = new TableColumn<>(sel.getName());
    col.setCellValueFactory(new Callbacks<A,Number>().stringCol(null,
        MethodMapper.lifeStat(year, time, sel)));
    this.getColumns().add(col);
    return this;
  }
  
}
