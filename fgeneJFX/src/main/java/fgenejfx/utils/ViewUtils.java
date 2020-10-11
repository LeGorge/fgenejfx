package fgenejfx.utils;

import java.lang.reflect.Field;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
    lbl.setTooltip(tooltip);
    col.setGraphic(lbl);
    try {
      Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
      fieldBehavior.setAccessible(true);
      Object objBehavior = fieldBehavior.get(tooltip);
      
      Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
      fieldTimer.setAccessible(true);
      Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);
      
      objTimer.getKeyFrames().clear();
      objTimer.getKeyFrames().add(new KeyFrame(new Duration(50)));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
