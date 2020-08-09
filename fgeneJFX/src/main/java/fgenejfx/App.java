package fgenejfx;

import fgenejfx.controllers.League;
import fgenejfx.models.enums.BackgroundSelector;
import fgenejfx.models.enums.Front;
import fgenejfx.models.enums.SideType;
import fgenejfx.utils.Utils;
import fgenejfx.view.SeasonView;
import fgenejfx.view.engine.Structure;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
  public static Stage stage;
  
  private static Structure view;
  
  public static Front currentPage;
  public static BackgroundSelector theme;

  @Override
  public void start(Stage primaryStage) throws Exception {
    League.get();
    Utils.begin();
    App.theme = BackgroundSelector.getRandom();
    App.stage = primaryStage;
    
    App.view = new Structure();
//    App.view.set(new SeasonView(1), SideType.TEAMSIDE);
    App.navigate(Front.SEASON);
    
    Scene scene = new Scene(App.view);
    
    scene.getStylesheets().add(getClass().getResource("/css/text.css").toString());
    scene.getStylesheets().add(getClass().getResource("/css/background.css").toString());
    primaryStage.setScene(scene);
    primaryStage.setMaximized(true);
    primaryStage.show();
  }
  
  public static void navigate() {
    navigate(currentPage);
  }
  public static void navigate(Front view) {
    switch (view) {
    case SEASON:
      App.currentPage = view;
      App.view.set(new SeasonView(League.get().getYear()), SideType.TEAMSIDE);
      break;

    default:
      break;
    }
  }
  
  public static void main(String[] args) {
    Application.launch(args);
  }
}