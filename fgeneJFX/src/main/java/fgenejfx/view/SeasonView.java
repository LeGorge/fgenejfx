package fgenejfx.view;

import java.util.Collection;

import fgenejfx.App;
import fgenejfx.controllers.League;
import fgenejfx.models.Group;
import fgenejfx.models.Pilot;
import fgenejfx.models.Team;
import fgenejfx.models.enums.LeagueTime;
import fgenejfx.models.enums.MethodSelector;
import fgenejfx.view.engine.CustomGridPane;
import fgenejfx.view.engine.CustomTableView;
import javafx.geometry.Pos;

public class SeasonView extends CustomGridPane {

  public SeasonView(int year) {
    super(Pos.TOP_CENTER);
    this.minHeightProperty().bind(App.stage.heightProperty().subtract(98));
    this.add(pilotSeason(year, Pilot.class), 0, 0);
    this.add(groups(year), 1, 0);
    this.add(pilotSeason(year, Team.class), 0, 1, 2, 1);
  }

  // private CustomGridPane groups() {
  //
  // }
  private <A> CustomGridPane pilotSeason(int year, Class<A> c) {
    CustomTableView<A> table = new CustomTableView<>(year);
    table.setPrefHeight(520);
    
    if(c.equals(Pilot.class)) {
      table.addTeamColumn();
    }
    LeagueTime t = LeagueTime.SEASON;
    table.addNameColumn()
        .addBySeasonStatColumn(t, MethodSelector.PTS)
        .addBySeasonStatColumn(t, MethodSelector.P1ST)
        .addBySeasonStatColumn(t, MethodSelector.P2ND)
        .addBySeasonStatColumn(t, MethodSelector.P3RD)
        .addBySeasonStatColumn(t, MethodSelector.P4TH)
        .addBySeasonStatColumn(t, MethodSelector.P5TH)
        .addBySeasonStatColumn(t, MethodSelector.P6TH)
        .addBySeasonStatColumn(t, MethodSelector.PER);
    
    if(c.equals(Pilot.class)) {
      table.getItems().addAll((Collection<? extends A>) League.get().season(year).pilots());
    }
    if(c.equals(Team.class)) {
      table.getItems().addAll((Collection<? extends A>) League.get().season(year).teams());
    }

    return new CustomGridPane(Pos.CENTER, table);
 }
  
  private CustomGridPane groups(int year) {
    CustomGridPane grid = new CustomGridPane(Pos.CENTER);
    Group[] gs = League.get().season(year).getSeason();
    for (int i=0; i < gs.length; i++) {
      CustomTableView<Pilot> table = new CustomTableView<>(year);
      table.setPrefHeight(170);
      
      table.addTeamColumn()
          .addNameColumn()
          .addAgeColumn()
          .addlifeStatColumn(LeagueTime.SEASON, MethodSelector.PTR)
          .addBySeasonStatColumn(LeagueTime.SEASON, MethodSelector.PTS);
      
      table.getItems().addAll(gs[i].pilots());
      table.getSortOrder().add(table.getColumns().get(0));
      
      grid.add(table, i-((i/2)*2), i/2);
    }
    return grid;
  }
}
