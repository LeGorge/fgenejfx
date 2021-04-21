package fgenejfx.view.components;

import fgenejfx.controllers.ContractsController;
import fgenejfx.controllers.HistoryController;
import fgenejfx.controllers.League;
import fgenejfx.controllers.NewsController;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class CustomGridPane extends GridPane {
	protected League l = League.get();
	protected HistoryController historyController = HistoryController.get();
	protected ContractsController contractsController = ContractsController.get();
	protected NewsController newsController = NewsController.get();

	public CustomGridPane(Pos p) {
		super();
		this.setAlignment(p);
		this.setPadding(new Insets(10, 10, 10, 10));
		this.setVgap(5);
		this.setHgap(5);
//      this.setStyle("-fx-padding: 10;" + 
//                  "-fx-border-style: solid inside;" + 
//                  "-fx-border-width: 2;" +
//                  "-fx-border-insets: 5;" + 
//                  "-fx-border-radius: 5;" + 
//                  "-fx-border-color: blue;");
//      this.setGridLinesVisible(true);
	}

	public CustomGridPane(Pos p, Node n) {
		this(p);
		this.add(n, 0, 0);
	}

	public CustomGridPane(Node n) {
		this(Pos.CENTER);
		this.add(n, 0, 0);
	}
	
	public CustomGridPane() {
		this(Pos.CENTER);
	}
	
	public void allignAllH() {
		allignAllH(HPos.CENTER);
	}
	public void allignAllH(HPos pos) {
		this.getChildren().stream().forEach(node -> CustomGridPane.setHalignment(node, pos));
	}
}