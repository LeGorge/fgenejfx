package fgenejfx.view.engine;

import javafx.scene.control.Hyperlink;

public class CustomHyperlink extends Hyperlink{
	
	public CustomHyperlink(String s, String css) {
		super(s);
		setBorder(null);
		getStyleClass().add(css);
	}
}
