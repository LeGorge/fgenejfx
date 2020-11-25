package fgenejfx.view.engine;

import java.util.Map;

import fgenejfx.controllers.League;
import fgenejfx.models.Powers;
import fgenejfx.models.Team;
import fgenejfx.models.enums.LeagueTime;
import fgenejfx.models.enums.MethodSelector;
import fgenejfx.utils.Utils;
import fgenejfx.utils.ViewUtils;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableRow;
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
  // Row
  // ============================================================================================
  public void colorRow(Map<Integer, String> map){
    this.setRowFactory(table ->{
      TableRow<A> row = new TableRow<A>() {
        @Override
        protected void updateItem(A item, boolean empty){
          super.updateItem(item, empty);
          for (Integer i : map.keySet()) {
            if(getIndex() == i) {
              setStyle("-fx-background-color: "+map.get(i)+";");
//              System.out.println(getIndex() + " - "+ getStyle());
            }
          }
        }
      };
//      row.setStyle("-fx-background-color: red;");
      
      return row;
    });
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
        MethodMapper.teamOf(year),null));
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
//    ageCol.setCellValueFactory(new PropertyValueFactory<>("yearsInTheLeague"));
    ageCol.setCellValueFactory(new Callbacks<A,Number>().stringCol(null,
        MethodMapper.age(year), null));
    ageCol.setSortType(SortType.DESCENDING);
    this.getColumns().add(ageCol);
    return this;
  }
  
  public CustomTableView<A> addGroupColumn() {
    TableColumn<A, Number> groupCol = new TableColumn<>("Group");
    ViewUtils.tooltip(groupCol);
    groupCol.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get().season(year),
        MethodMapper.group(), null));
    groupCol.setSortType(SortType.DESCENDING);
    this.getColumns().add(groupCol);
    return this;
  }
  
  // ============================================================================================
  // By Season Columns
  // ============================================================================================
  public CustomTableView<A> addTopPosColumn() {
    TableColumn<A, String> col = new TableColumn<>("#");
    ViewUtils.tooltip(col, "Position last season");
    col.setMinWidth(25);
    col.setCellValueFactory(new Callbacks<A,String>().stringCol(League.get(),
        MethodMapper.posOf(year-1), null));
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
        MethodMapper.scoutReportOf(year), null));
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
    ViewUtils.tooltip(col,sel.getTip());
    if(sel == MethodSelector.PER) {
      col.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get(),
          MethodMapper.bySeasonStat(year, time, sel), Utils.perFormat));
    }else {
      col.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get(),
          MethodMapper.bySeasonStat(year, time, sel), null));
    }
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
        MethodMapper.bySeasonPowerRelative(year, p), null));
    col.setCellFactory(c -> new TableCell<A, Number>() {
      @Override
      protected void updateItem(Number item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
          if(p == null) {
            setText(item.toString());
            return;
          }
          if(item.intValue() == 0) {
            this.setTextFill(Color.GRAY);
            this.setFont(Font.font("Cooper Black", FontWeight.BOLD, 11));
          }else {
            if(item.intValue() > 0) {
              this.setTextFill(Color.GREEN);
              this.setFont(Font.font("Cooper Black", FontWeight.BOLD, 14));
            }else {
              this.setTextFill(Color.RED);
              this.setFont(Font.font("Cooper Black", FontWeight.BOLD, 14));
            }
          }
          setText(Utils.powerValueToStr(item.intValue()));
        } 
      }
    });
    col.setSortType(SortType.DESCENDING);
    this.getColumns().add(col);
    return this;
  }
  
  // ============================================================================================
  // Life Columns
  // ============================================================================================
  public CustomTableView<A> addlifeStatColumn(LeagueTime time, MethodSelector sel) {
    TableColumn<A, Number> col = new TableColumn<>(sel.getName());
    ViewUtils.tooltip(col,sel.getTip());
    col.setCellValueFactory(new Callbacks<A,Number>().stringCol(null,
        MethodMapper.lifeStat(year, time, sel), Utils.perFormat));
    col.setSortType(SortType.DESCENDING);
    this.getColumns().add(col);
    return this;
  }
  
  public CustomTableView<A> addPerRoundColumn(LeagueTime time, MethodSelector sel) {
    TableColumn<A, Number> col = new TableColumn<>(sel.getName());
    ViewUtils.tooltip(col,sel.getTip());
    if(sel == MethodSelector.PERPR) {
      col.setCellValueFactory(new Callbacks<A,Number>().stringCol(null,
          MethodMapper.perRoundStat(time, MethodSelector.PERPR), Utils.perFormat));
    }else {
      col.setCellValueFactory(new Callbacks<A,Number>().stringCol(null,
          MethodMapper.perRoundStat(time, MethodSelector.PPR), Utils.pprFormat));
    }
    
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

