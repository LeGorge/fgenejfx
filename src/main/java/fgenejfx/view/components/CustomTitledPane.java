package fgenejfx.view.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class CustomTitledPane extends StackPane {

	public CustomTitledPane(String titleString, Node content) {
		Label title = new Label("  " + titleString + "  ");
		title.getStyleClass().add("bordered-titled-title");
		StackPane.setAlignment(title, Pos.TOP_LEFT);

		StackPane contentPane = new StackPane();
		content.getStyleClass().add("bordered-titled-content");
		contentPane.getChildren().add(content);

		getStyleClass().add("bordered-titled-border");
		getChildren().addAll(title, contentPane);
	}
}
