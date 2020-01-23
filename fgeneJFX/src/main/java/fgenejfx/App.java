package fgenejfx;

import fgenejfx.controllers.League;
import fgenejfx.models.enums.SideType;
import fgenejfx.view.SeasonView;
import fgenejfx.view.Structure;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {
  public static Stage stage;
  
  private static Structure view;

  @Override
  public void start(Stage primaryStage) throws Exception {
    League.get();
    App.stage = primaryStage;
    
    App.view = new Structure();
    App.view.set(new SeasonView(), SideType.TEAMSIDE);
    Scene scene = new Scene(App.view);
    
    scene.getStylesheets().add(getClass().getResource("/css/text.css").toString());
    scene.getStylesheets().add(getClass().getResource("/css/background.css").toString());
    primaryStage.setScene(scene);
    primaryStage.setMaximized(true);
    primaryStage.show();
  }
  
  public static void navigate(Pane to) {
//    App.view.set(null);
  }
  
  public static void main(String[] args) {
    Application.launch(args);
  }
}