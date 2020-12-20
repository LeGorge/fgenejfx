package fgenejfx.view;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import fgenejfx.App;
import fgenejfx.controllers.League;
import fgenejfx.dtos.ChampionsDto;
import fgenejfx.models.enums.LeagueTime;
import fgenejfx.view.engine.CustomGridPane;
import fgenejfx.view.engine.CustomTableView;
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
    
    pane.add(titlePane, 0, 0);
    pane.add(ChampionsPane(LeagueTime.PPLAYOFF), 0, 1);
    pane.add(ChampionsPane(LeagueTime.TPLAYOFF), 0, 2);
    
    return pane;
  }
  
  private CustomGridPane ChampionsPane(LeagueTime time) {
	  CustomGridPane pane = new CustomGridPane(Pos.CENTER);
	  
	  CustomTableView<ChampionsDto> table = new CustomTableView<>(0);
	  table.setPrefHeight(500);
	  table.setPrefWidth(750);
	  table.addYearColumn()
		  .addCardColumn("Gold", "c1", time)
		  .addCardColumn("Silver", "c2", time)
		  .addCardColumn("Bronze", "c3", time);
	  
	  if(time.equals(LeagueTime.PPLAYOFF)) {
		  table.setPrefWidth(1520);
		  table.addCardColumn("4th", "c4", time)
			  .addCardColumn("5th", "c5", time)
			  .addCardColumn("6th", "c6", time);
	  }
	  
	  Integer year = League.get().getYear();
	  var counter = new AtomicInteger(year);
	  List<ChampionsDto> dtos = IntStream.generate(counter::decrementAndGet).limit(year-1).boxed()
			  .map(i-> new ChampionsDto(i, time))
			  .collect(Collectors.toList());
	  table.getItems().addAll(dtos);
	  pane.add(table, 0, 0);
	  return pane;
  }
  
}
