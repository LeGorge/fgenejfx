package fgenejfx.view.engine;

import fgenejfx.controllers.League;
import fgenejfx.models.enums.LeagueTime;
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
    this.getColumns().add(nameCol);
    return this;
  }
  
  public CustomTableView<A> addTeamColumn() {
    TableColumn<A, String> teamCol = new TableColumn<>("Team");
    teamCol.setPrefWidth(100);
    teamCol.setCellValueFactory(new Callbacks<A,String>().stringCol(League.get(),
        MethodMapper.teamOf(year)));
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
  // Absolute Stats
  // ============================================================================================
  public CustomTableView<A> addPtsColumn() {
    TableColumn<A, Number> ptsCol = new TableColumn<>("Points");
    ptsCol.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get(),
        MethodMapper.pts(year, LeagueTime.SEASON)));
    this.getColumns().add(ptsCol);
    return this;
  }
  
  public CustomTableView<A> addP1stColumn() {
    TableColumn<A, Number> p1stCol = new TableColumn<>("1st");
    p1stCol.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get(),
        MethodMapper.p1st(year)));
    this.getColumns().add(p1stCol);
    return this;
  }
  
  public CustomTableView<A> addP2ndColumn() {
    TableColumn<A, Number> p2ndCol = new TableColumn<>("2nd");
    p2ndCol.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get(),
        MethodMapper.p2nd(year)));
    this.getColumns().add(p2ndCol);
    return this;
  }
  
  public CustomTableView<A> addP3rdColumn() {
    TableColumn<A, Number> p3rdCol = new TableColumn<>("3rd");
    p3rdCol.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get(),
        MethodMapper.p3rd(year)));
    this.getColumns().add(p3rdCol);
    return this;
  }
  
  public CustomTableView<A> addP4thColumn() {
    TableColumn<A, Number> p4thCol = new TableColumn<>("4th");
    p4thCol.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get(),
        MethodMapper.p4th(year)));
    this.getColumns().add(p4thCol);
    return this;
  }
  
  public CustomTableView<A> addP5thColumn() {
    TableColumn<A, Number> p5thCol = new TableColumn<>("5th");
    p5thCol.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get(),
        MethodMapper.p5th(year)));
    this.getColumns().add(p5thCol);
    return this;
  }
  
  public CustomTableView<A> addP6thColumn() {
    TableColumn<A, Number> p6thCol = new TableColumn<>("6th");
    p6thCol.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get(),
        MethodMapper.p6th(year)));
    this.getColumns().add(p6thCol);
    return this;
  }
  
  // ============================================================================================
  // Dynamic Stats
  // ============================================================================================
  public CustomTableView<A> addPtrColumn() {
    TableColumn<A, Number> ptrCol = new TableColumn<>("Point Rate");
    ptrCol.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get(),
        MethodMapper.ptr(year)));
    this.getColumns().add(ptrCol);
    return this;
  }
  
  public CustomTableView<A> addWrColumn() {
    TableColumn<A, Number> wrCol = new TableColumn<>("Win Rate");
    wrCol.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get(),
        MethodMapper.winr(year)));
    this.getColumns().add(wrCol);
    return this;
  }
  
  public CustomTableView<A> addPerColumn() {
    TableColumn<A, Number> perCol = new TableColumn<>("PER");
    perCol.setCellValueFactory(new Callbacks<A,Number>().stringCol(League.get(),
        MethodMapper.per(year)));
    this.getColumns().add(perCol);
    return this;
  }
}
