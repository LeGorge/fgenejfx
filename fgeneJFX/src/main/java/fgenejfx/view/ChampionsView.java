package fgenejfx.view;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import fgenejfx.App;
import fgenejfx.dtos.ChampionsDto;
import fgenejfx.models.enums.LeagueTime;
import fgenejfx.view.components.CustomGridPane;
import fgenejfx.view.components.tableViews.ChampsTableView;
import javafx.geometry.Pos;
import javafx.scene.text.Text;

public class ChampionsView extends CustomGridPane {
  
  public ChampionsView() {
    super(Pos.TOP_CENTER);
    this.minHeightProperty().bind(App.stage.heightProperty().subtract(98));
    
    this.add(championsPane(), 0, 0, 1, 1);
//    this.add(recordsPane(), 0, 1, 1, 1);
  }
  
  // ============================================================================================
  // Champions Pane
  // ============================================================================================
  private CustomGridPane championsPane() {
    CustomGridPane pane = new CustomGridPane(Pos.CENTER);
    
    CustomGridPane titlePane = new CustomGridPane(Pos.CENTER);
    Text title = new Text("Champions ");
    title.getStyleClass().add("title");
    titlePane.add(title, 0, 0);
    
    pane.add(titlePane, 0, 0, 2, 1);
    pane.add(championsTable(LeagueTime.PPLAYOFF, true, 800), 0, 1);
    pane.add(championsTable(LeagueTime.PPLAYOFF, false, 800), 1, 1);
    pane.add(championsTable(LeagueTime.TPLAYOFF, false, 500), 0, 2);
    pane.add(championsTable(LeagueTime.TPLAYOFF, true, 500), 1, 2);

    return pane;
  }
  
  private CustomGridPane championsTable(LeagueTime time, Boolean pilotName, Integer width) {
	  CustomGridPane pane = new CustomGridPane(Pos.CENTER);
	  
	  ChampsTableView table = new ChampsTableView(0);
	  table.setPrefHeight(1000);
	  table.setPrefWidth(width);
	  table.addYearColumn()
		  .addCardColumn("Gold", "c1", time, pilotName)
		  .addCardColumn("Silver", "c2", time, pilotName)
		  .addCardColumn("Bronze", "c3", time, pilotName);
	  
	  if(time.equals(LeagueTime.PPLAYOFF)) {
		  table.addCardColumn("4th", "c4", time, pilotName)
			  .addCardColumn("5th", "c5", time, pilotName)
			  .addCardColumn("6th", "c6", time, pilotName);
	  }
	  
	  Integer year = l.getYear();
	  var counter = new AtomicInteger(year);
	  List<ChampionsDto> dtos = IntStream.generate(counter::decrementAndGet).limit(year-1).boxed()
			  .map(i-> new ChampionsDto(i, time))
			  .collect(Collectors.toList());
	  table.getItems().addAll(dtos);
	  pane.add(table, 0, 0);
	  return pane;
  }
  
}
