package fgenejfx.utils;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class ViewUtils {
  
  public static void tooltip(TableColumn col) {
    tooltip(col, col.getText());
  }
  
  public static void tooltip(TableColumn col, String text) {
    Label lbl = new Label(col.getText());
    col.setText("");
    Tooltip tooltip = new Tooltip(text);
    tooltip.setShowDelay(Duration.millis(50));
    lbl.setTooltip(tooltip);
    col.setGraphic(lbl);
  }
}
