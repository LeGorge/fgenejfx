package fgenejfx.view.components;

import javafx.scene.control.Label;
import javafx.scene.text.Text;

public abstract class LabelFactory{
	
	public static Text title(String text) {
		Text l = new Text(text);
		return l;
	}
	
	public static Label normal(String text) {
		Label l = new Label(text);
		l.getStyleClass().add("effect-text");
		return l;
	}
	
	public static Text subtitle32(String text) {
		Text l = new Text(text);
		l.getStyleClass().add("effect-subtitle32");
		return l;
	}
	
	public static Text subtitle24(String text) {
		Text l = new Text(text);
		l.getStyleClass().add("effect-subtitle24");
		return l;
	}
}
