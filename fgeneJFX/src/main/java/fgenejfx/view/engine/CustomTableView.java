package fgenejfx.view.engine;

import fgenejfx.controllers.League;
import fgenejfx.models.Pilot;
import fgenejfx.models.Powers;
import fgenejfx.models.Team;
import fgenejfx.models.enums.LeagueTime;
import fgenejfx.models.enums.MethodSelector;
import fgenejfx.utils.ViewUtils;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CustomTableView<A> extends TableView<A>{
  
  private int year;
  
  public CustomTableView(int year) {
    this.year = year;
    setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
  }

  // ============================================================================================
  // Info
  // ============================================================================================
  public CustomTableView<A> addNameColumn() {
    TableColumn<A, String> nameCol = new TableColumn<>("Name");
    ViewUtils.tooltip(nameCol);
    nameCol.setMinWidth(70);
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
    ViewUtils.tooltip(teamCol);
    teamCol.setMinWidth(70);
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
    ViewUtils.tooltip(ageCol);
    ageCol.setCellValueFactory(new PropertyValueFactory<>("yearsInTheLeague"));
    ageCol.setSortType(SortType.DESCENDING);
    this.getColumns().add(ageCol);
    return this;
  }
  
  // ============================================================================================
  // By Season Columns
  // ============================================================================================
  public CustomTableView<A> addTopPosColumn() {
    TableColumn<A, String> col = new TableColumn<>("#");
    ViewUtils.tooltip(col, "Position last season");
    col.setCellValueFactory(new Callbacks<A,String>().stringCol(League.get(),
        MethodMapper.posOf(year-1)));
    col.setCellFactory(c -> new TableCell<A, String>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty && item != "-") {
          Integer itemValue = Integer.parseInt(item.substring(4));
          if(itemValue <= 6) {
            this.setTextFill(Color.RED);
          }
          if(itemValue == 12) {
            this.setTextFill(Color.ORANGE);
          }
          if(itemValue > 12) {
            this.setTextFill(Color.LIGHTGRAY);
          }
        } 
        setText(item);
      }
      });
    this.getColumns().add(col);
    return this;
  }
  
  public CustomTableView<A> addScoutReportColumn() {
    TableColumn<A, String> col = new TableColumn<>("Scout Report");
    ViewUtils.tooltip(col);
    col.setCellValueFactory(new Callbacks<A,String>().stringCol(League.get(),
        MethodMapper.scoutReportOf(year)));
    col.setCellFactory(c -> new TableCell<A, String>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty && item != "-") {
          if(item.equals("||")) {
            this.setTextFill(Color.GRAY);
            this.setFont(Font.font("Cooper Black", FontWeight.BOLD, 11));
          }else {
            this.setTextFill(Color.GREEN);
            this.setFont(Font.font("Cooper Black", FontWeight.BOLD, 14));
          }
        } 
        setText(item);
      }
    });
    this.getColumns().add(col);
    return this;
  }
  
  public CustomTableView<A> addBySeasonStatColumn(LeagueTime time, MethodSelector sel) {
    TableColumn<A, Number> col = new TableColumn<>(sel.getName());
    ViewUtils.tooltip(col);
    col.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get(),
        MethodMapper.bySeasonStat(year, time, sel)));
    col.setSortType(SortType.DESCENDING);
    this.getColumns().add(col);
    return this;
  }
  
  public CustomTableView<A> addPowersColumn(Powers p) {
    TableColumn<A, Number> col;
    if(p != null) {
      col = new TableColumn<>(p.toString());
    }else {
      col = new TableColumn<>("Grade");
    }
    ViewUtils.tooltip(col);
    col.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get(),
        MethodMapper.bySeasonPower(year, p)));
    col.setSortType(SortType.DESCENDING);
    this.getColumns().add(col);
    return this;
  }
  
  // ============================================================================================
  // Life Columns
  // ============================================================================================
  public CustomTableView<A> addlifeStatColumn(LeagueTime time, MethodSelector sel) {
    TableColumn<A, Number> col = new TableColumn<>(sel.getName());
    ViewUtils.tooltip(col);
    col.setCellValueFactory(new Callbacks<A,Number>().stringCol(null,
        MethodMapper.lifeStat(year, time, sel)));
    col.setSortType(SortType.DESCENDING);
    this.getColumns().add(col);
    return this;
  }
  
  // ============================================================================================
  // Engine
  // ============================================================================================
  public void sortByColumns(Integer... cols) {
    getSortOrder().clear();
    for (Integer i : cols) {
      getSortOrder().add(getColumns().get(i));
    }
    getSortOrder().clear();
  }
}

