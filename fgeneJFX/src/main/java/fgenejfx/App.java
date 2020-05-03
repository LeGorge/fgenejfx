package fgenejfx;

import java.lang.reflect.Field;

import fgenejfx.controllers.League;
import fgenejfx.models.enums.Front;
import fgenejfx.models.enums.SideType;
import fgenejfx.utils.Utils;
import fgenejfx.view.SeasonView;
import fgenejfx.view.engine.Structure;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {
  public static Stage stage;
  
  private static Structure view;

  @Override
  public void start(Stage primaryStage) throws Exception {
    League.get();
    Utils.begin();
    App.stage = primaryStage;
    
    
    App.view = new Structure();
    App.view.set(new SeasonView(1), SideType.TEAMSIDE);
    
    Scene scene = new Scene(App.view);
    
    scene.getStylesheets().add(getClass().getResource("/css/text.css").toString());
    scene.getStylesheets().add(getClass().getResource("/css/background.css").toString());
    primaryStage.setScene(scene);
    primaryStage.setMaximized(true);
    primaryStage.show();
  }
  
  public static void navigate(Front view) {
    switch (view) {
    case SEASON:
      App.view.set(new SeasonView(1), SideType.TEAMSIDE);
      break;

    default:
      break;
    }
  }
  
  public static void main(String[] args) {
    Application.launch(args);
  }
}